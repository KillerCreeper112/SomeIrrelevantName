package killerceepr.oogabooga.config.data;

import killerceepr.oogabooga.config.CruxConfig;
import killerceepr.oogabooga.data.CreateTitle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateTitleValue extends ConfigValue<CreateTitle>{
    public CreateTitleValue(@Nullable CreateTitle defaultValue) {
        super(CreateTitle.class, defaultValue);
    }

    public CreateTitleValue() {
        super(CreateTitle.class);
    }

    @Override
    public @Nullable CreateTitle get(@NotNull CruxConfig cfg, @NotNull String path) {
        return cfg.createTitle(CruxConfig.addDot(path));
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable CreateTitle object) {
        cfg.createTitle(CruxConfig.addDot(path), object);
    }
}
