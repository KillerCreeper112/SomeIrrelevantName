package killerceepr.oogabooga.data;

import killerceepr.oogabooga.OogaBoogaDeluxe;
import killerceepr.oogabooga.hooks.PlaceholderAPIHook;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MsgContainer {
    protected final List<String> chat;
    protected final String actionBar;
    protected final CreateTitle title;
    protected final CreateSound sound;
    protected boolean broadcast;
    public MsgContainer(@Nullable List<String> chat, @Nullable String actionBar, @Nullable CreateTitle title, @Nullable CreateSound sound) {
        this.chat = chat;
        this.actionBar = actionBar;
        this.title = title;
        this.sound = sound;
    }

    public MsgContainer(@Nullable String chat, @Nullable String actionBar, @Nullable CreateTitle title, @Nullable CreateSound sound){
        this(chat == null ? null : List.of(chat), actionBar, title, sound);
    }
    public MsgContainer(@Nullable List<String> chat, @Nullable String actionBar, @Nullable CreateTitle title) {
        this(chat, actionBar, title, null);
    }
    public MsgContainer(@Nullable String chat, @Nullable String actionBar, @Nullable CreateTitle title) {
        this(chat == null ? null : List.of(chat), actionBar, title, null);
    }

    public MsgContainer(@Nullable List<String> chat, @Nullable String actionBar) {
        this(chat, actionBar, null);
    }
    public MsgContainer(@Nullable String chat, @Nullable String actionBar) {
        this(chat == null ? null : List.of(chat), actionBar, null);
    }
    public MsgContainer(@Nullable List<String> chat) {
        this(chat, null);
    }
    public MsgContainer(@Nullable String chat) {
        this(chat == null ? null : List.of(chat), null);
    }

    public MsgContainer use(@NotNull Audience a, @NotNull TagResolver@Nullable... tags){
        return use(a, null, tags);
    }

    public MsgContainer use(@NotNull Audience a, @Nullable Player placeholders, @NotNull TagResolver@Nullable... tags){
        if(isBroadcast()) return broadcast(tags);
        if(a instanceof Player p) return use(p, placeholders, tags);
        if(chat != null){
            PlaceholderAPIHook hook = OogaBoogaDeluxe.inst().getPlaceholderAPIHook();
            for(String s : chat){
                if(hook != null) s = hook.setPlaceholders(placeholders, s);
                a.sendMessage(deserialize(s, tags));
            }
        }
        return this;
    }

    public MsgContainer use(@NotNull Player p, @Nullable Player placeholders, @NotNull TagResolver@Nullable... tags){
        return use(p, placeholders, true, tags);
    }

    public MsgContainer use(@NotNull Player p, @Nullable Player placeholders, boolean broadcastCheck, @NotNull TagResolver@Nullable... tags){
        if(broadcastCheck && broadcast){
            return broadcast(tags);
        }
        PlaceholderAPIHook hook = OogaBoogaDeluxe.inst().getPlaceholderAPIHook();
        if(chat != null){
            for(String s : chat){
                if(hook != null) s = hook.setPlaceholders(placeholders == null ? p : placeholders, s);
                p.sendMessage(deserialize(s, tags));
            }
        }
        if(actionBar != null){
            String s = actionBar;
            if(hook != null) s = hook.setPlaceholders(placeholders == null ? p : placeholders, s);
            p.sendActionBar(deserialize(s, tags));
        }
        if(title != null) p.showTitle(title.build(p, tags));
        if(sound != null) sound.play(p, false);
        return this;
    }

    public MsgContainer broadcast(@NotNull TagResolver@Nullable... tags){
        for(Player p : Bukkit.getOnlinePlayers()){
            use(p, null, false, tags);
        }
        return this;
    }

    protected @NotNull Component deserialize(@Nullable String input, @NotNull TagResolver@Nullable... tags){
        if(input == null) return Component.empty();
        if(tags == null) return OogaBoogaDeluxe.FORMAT.deserialize(input);
        return OogaBoogaDeluxe.FORMAT.deserialize(input, tags);
    }

    public @Nullable List<String> getChat() {
        return chat;
    }

    public @Nullable  String getActionBar() {
        return actionBar;
    }

    public @Nullable CreateTitle getTitle() {
        return title;
    }

    public @Nullable CreateSound getSound() {
        return sound;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public MsgContainer setBroadcast(boolean broadcast) {
        this.broadcast = broadcast; return this;
    }

    public boolean isEmpty(){
        return chat == null && actionBar == null && title == null && sound == null;
    }
}
