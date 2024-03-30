package killerceepr.oogabooga.config.data;

import killerceepr.oogabooga.config.CruxConfig;
import killerceepr.oogabooga.data.CreateSound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateSoundValue extends ConfigValue<CreateSound>{


    public CreateSoundValue(@Nullable CreateSound defaultValue) {
        super(CreateSound.class, defaultValue);
    }

    public CreateSoundValue() {
        super(CreateSound.class);
    }

    @Override
    public @Nullable CreateSound get(@NotNull CruxConfig cfg, @NotNull String path) {
        return cfg.sound(CruxConfig.addDot(path));
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable CreateSound object) {
        cfg.sound(CruxConfig.addDot(path), object);
    }
}
