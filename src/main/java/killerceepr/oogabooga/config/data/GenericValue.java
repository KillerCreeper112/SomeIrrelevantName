package killerceepr.oogabooga.config.data;

import killerceepr.oogabooga.config.CruxConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenericValue extends ConfigValue<Object> {

    public GenericValue(@Nullable Object defaultValue) {
        super(Object.class, defaultValue);
    }

    public GenericValue() {
        super(Object.class);
    }

    @Nullable
    @Override
    public Object get(@NotNull CruxConfig cfg, @NotNull String path) {
        return cfg.config().get(path);
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable Object object) {
        cfg.config().set(path, object);
    }
}
