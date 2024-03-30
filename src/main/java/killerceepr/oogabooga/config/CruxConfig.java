package killerceepr.oogabooga.config;

import killerceepr.oogabooga.data.CreateSound;
import killerceepr.oogabooga.data.CreateTitle;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class CruxConfig extends CruxFolder {
    protected final FileConfiguration cfg;

    public CruxConfig(@NotNull Plugin plugin, @NotNull String path){
        super(plugin, path + ".yml");
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public CruxConfig(@NotNull File file){
        super(file);
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public final @NotNull FileConfiguration config(){
        return cfg;
    }

    public void register(){}

    public final boolean save(){
        try{
            cfg.save(file);
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public final boolean createDefault(){
        if(file.exists()) return false;
        setDefaults();
        return save();
    }

    protected void setDefaults(){}

    public void set(@NotNull String path, @Nullable Object value){
        cfg.set(path, value);
    }

    public void set(@NotNull String path, @Nullable Object value, @Nullable String... comments){
        cfg.set(path, value);
        setComments(path, comments);
    }

    public void set(@NotNull String path, @Nullable Object value, @Nullable List<String> comments){
        cfg.set(path, value);
        cfg.setComments(path, comments);
    }

    public void setComments(@NotNull String path, @Nullable String... comments){
        cfg.setComments(path, comments == null || comments.length < 1 ? null : List.of(comments));
    }

    public static @NotNull String addDot(@NotNull String s){
        return s.isBlank() ? s : s.endsWith(".") ? s : (s+".");
    }

    public static @NotNull String removeDot(@NotNull String s){
        return s.endsWith(".") ? s.substring(1) : s;
    }

    public CruxConfig sound(@NotNull String path, @Nullable CreateSound sound){
        if(sound == null) cfg.set(removeDot(path), null);
        else{
            cfg.set(path + "sound", sound.getSound().name().asString());
            cfg.set(path + "volume", sound.getSound().volume());
            cfg.set(path + "pitch", sound.getSound().pitch());
            cfg.set(path + "source", sound.getSound().source().toString());
        }
        return this;
    }

    public @Nullable CreateSound sound(@NotNull String path){
        final String keyName = cfg.getString(path + "sound");
        if(keyName == null || keyName.isBlank()) return null;
        net.kyori.adventure.sound.Sound.Source source;
        try{ source = net.kyori.adventure.sound.Sound.Source.valueOf(cfg.getString("source", "").toUpperCase()); }
        catch (IllegalArgumentException e){ source = net.kyori.adventure.sound.Sound.Source.MASTER; }
        try{
            Sound s = Sound.valueOf(keyName.toUpperCase());
            return new CreateSound(s, source,
                    (float) cfg.getDouble(path + "volume", 2f),
                    (float) cfg.getDouble(path + "pitch", 1f));
        }catch (IllegalArgumentException e){
            String[] args = keyName.split(":");
            return new CreateSound(
                    net.kyori.adventure.sound.Sound.sound(
                            args.length > 1 ? Key.key(args[0], args[1]) : Key.key(Key.MINECRAFT_NAMESPACE, args[0]),
                            source,
                            (float) cfg.getDouble(path + "volume", 2f),
                            (float) cfg.getDouble(path + "pitch", 1f))
            );
        }
    }

    public CruxConfig createTitle(@NotNull String path, @Nullable CreateTitle title){
        if(title == null){
            cfg.set(removeDot(path), null);
            return this;
        }
        cfg.set(path + "upper", title.getTitle());
        cfg.set(path + "lower", title.getSubTitle());
        cfg.set(path + "fade_in", title.getTimes().fadeIn().toMillis()/50L);
        cfg.set(path + "stay", title.getTimes().stay().toMillis()/50L);
        cfg.set(path + "fade_out", title.getTimes().fadeOut().toMillis()/50L);
        return this;
    }

    public @Nullable CreateTitle createTitle(@NotNull String path){
        String upper = cfg.getString(path + "upper");
        String lower = cfg.getString(path + "lower");
        if(upper == null && lower == null) return null;
        return new CreateTitle(upper, lower,
                Title.Times.times(Duration.ofMillis(cfg.getInt(path + "fade_in")*50L),
                        Duration.ofMillis(cfg.getInt(path + "stay")*50L),
                        Duration.ofMillis(cfg.getInt(path + "fade_out")*50L)));
    }
}
