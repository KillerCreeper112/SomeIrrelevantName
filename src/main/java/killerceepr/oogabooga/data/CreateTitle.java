package killerceepr.oogabooga.data;

import killerceepr.oogabooga.OogaBoogaDeluxe;
import killerceepr.oogabooga.hooks.PlaceholderAPIHook;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateTitle {
    protected final String title;
    protected final String subTitle;
    protected final Title.Times times;

    public CreateTitle(@Nullable String title, @Nullable String subTitle, @NotNull Title.Times times) {
        this.title = title;
        this.subTitle = subTitle;
        this.times = times;
    }

    public @NotNull Title build(@NotNull Player p, @NotNull TagResolver@Nullable... tags){
        PlaceholderAPIHook hook = OogaBoogaDeluxe.inst().getPlaceholderAPIHook();
        String title = this.title == null ? "" : this.title;
        String subTitle = this.subTitle == null ? "" : this.subTitle;
        if(hook != null){
            title = hook.setPlaceholders(p, title);
            subTitle = hook.setPlaceholders(p, subTitle);
        }
        if(tags == null) return Title.title(
                OogaBoogaDeluxe.FORMAT.deserialize(title),
                OogaBoogaDeluxe.FORMAT.deserialize(subTitle),
                times
        );
        return Title.title(
                OogaBoogaDeluxe.FORMAT.deserialize(title, tags),
                OogaBoogaDeluxe.FORMAT.deserialize(subTitle, tags),
                times
        );
    }

    public @Nullable String getTitle() {
        return title;
    }

    public @Nullable String getSubTitle() {
        return subTitle;
    }

    public @NotNull Title.Times getTimes() {
        return times;
    }
}
