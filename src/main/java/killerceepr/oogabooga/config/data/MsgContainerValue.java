package killerceepr.oogabooga.config.data;

import killerceepr.oogabooga.config.CruxConfig;
import killerceepr.oogabooga.data.CreateSound;
import killerceepr.oogabooga.data.CreateTitle;
import killerceepr.oogabooga.data.MsgContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MsgContainerValue extends ConfigValue<MsgContainer>{
    public MsgContainerValue(@Nullable MsgContainer defaultValue) {
        super(MsgContainer.class, defaultValue);
    }

    public MsgContainerValue() {
        super(MsgContainer.class);
    }

    @Override
    public @Nullable MsgContainer get(@NotNull CruxConfig cfg, @NotNull String path) {
        List<String> chat = cfg.config().getStringList(addDot(path) + "chat");
        String actionBar = cfg.config().getString(addDot(path) + "action_bar");
        CreateTitle title = new CreateTitleValue().get(cfg, addDot(path) + "title");
        CreateSound sound = new CreateSoundValue().get(cfg, addDot(path) + "sound");
        MsgContainer container = new MsgContainer(chat.isEmpty() ? null : chat,
                actionBar,
                title, sound);
        return container.isEmpty() ? null : container.setBroadcast(cfg.config().getBoolean(addDot(path) + "broadcast"));
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable MsgContainer object) {
        cfg.set(removeDot(path), null);
        if(object == null || object.isEmpty()) return;
        cfg.set(addDot(path) + "chat", object.getChat());
        cfg.set(addDot(path) + "action_bar", object.getActionBar());
        new CreateTitleValue().set(cfg, addDot(path) + "title", object.getTitle());
        new CreateSoundValue().set(cfg, addDot(path) + "sound", object.getSound());
        if(object.isBroadcast()) cfg.set(addDot(path) + "broadcast", object.isBroadcast());
    }
}
