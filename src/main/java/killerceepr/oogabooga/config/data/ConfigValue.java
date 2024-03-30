package killerceepr.oogabooga.config.data;

import killerceepr.oogabooga.config.CruxConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ConfigValue <T> {
    protected final Class<T> type;
    protected final T defaultValue;
    protected T value;

    public ConfigValue(@NotNull Class<T> type, @Nullable T defaultValue) {
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public ConfigValue(@NotNull Class<T> type) {
        this.type = type;
        this.defaultValue = null;
    }

    public @NotNull Class<T> getType() {
        return type;
    }

    public @Nullable T getDefaultValue() {
        return defaultValue;
    }

    public @Nullable T getValue() {
        return value;
    }

    public T getOrDefaultValue(@Nullable T defaultValue){
        if(value == null) return defaultValue;
        return value;
    }

    public @Nullable <I> I getValue(@NotNull Class<I> tryClass){
        return getValue(tryClass, null);
    }

    public <I> I getValue(@NotNull Class<I> tryClass, @Nullable I defaultValue){
        if(value == null) return defaultValue;
        return tryClass.isAssignableFrom(value.getClass()) ? tryClass.cast(value) : defaultValue;
    }

    public void setValue(@Nullable T value) {
        this.value = value;
    }

    public @Nullable T get(@Nullable Object o){
        if(o == null) return null;
        return o.getClass().isAssignableFrom(type) ? type.cast(o) : null;
    }

    public @Nullable T register(@NotNull CruxConfig cfg, @NotNull String path){
        value = get(cfg, path);
        return value;
    }

    public abstract @Nullable T get(@NotNull CruxConfig cfg, @NotNull String path);
    public abstract void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable T object);

    public void setDefault(@NotNull CruxConfig cfg, @NotNull String path){
        set(cfg, path, defaultValue);
    }

    //Convenience methods.
    public @Nullable String getString(){
        if(value instanceof String x) return x;
        return null;
    }

    public @Nullable Component getComponent(){
        String s = getString();
        return s == null ? null : MiniMessage.miniMessage().deserialize(s);
    }

    public @Nullable Number getNumber(){
        return getNumber(value);
    }

    public @Nullable Number getNumber(@Nullable Object o){
        if(o instanceof Number x) return x;
        return null;
    }

    public int getInt(){
        Number x = getNumber();
        if(x == null){
            Number d = getNumber(defaultValue);
            return d == null ? 0 : d.intValue();
        }
        return x.intValue();
    }

    public double getDouble(){
        Number x = getNumber();
        if(x == null){
            Number d = getNumber(defaultValue);
            return d == null ? 0D : d.doubleValue();
        }
        return x.doubleValue();
    }

    public float getFloat(){
        Number x = getNumber();
        if(x == null){
            Number d = getNumber(defaultValue);
            return d == null ? 0f : d.floatValue();
        }
        return x.floatValue();
    }

    public long getLong(){
        Number x = getNumber();
        if(x == null){
            Number d = getNumber(defaultValue);
            return d == null ? 0L : d.longValue();
        }
        return x.longValue();
    }

    public boolean getBoolean(){
        if(value instanceof Boolean x) return x;
        return defaultValue instanceof Boolean x ? x : false;
    }

    //Convenience methods
    protected @NotNull String addDot(@NotNull String s){ return CruxConfig.addDot(s); }
    protected @NotNull String removeDot(@NotNull String s){ return CruxConfig.removeDot(s); }
}
