package killerceepr.oogabooga.data;


import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CreateSound {
    private final Sound sound;

    public CreateSound(@NotNull Sound sound) {
        this.sound = sound;
    }

    public CreateSound(@NotNull org.bukkit.Sound sound, float volume, float pitch) {
        this(sound, Sound.Source.MASTER, volume, pitch);
    }

    public CreateSound(@NotNull org.bukkit.Sound sound, float pitch) {
        this(sound, 2f, pitch);
    }

    public CreateSound(@NotNull org.bukkit.Sound sound) {
        this(sound,1f);
    }

    public CreateSound(@NotNull org.bukkit.Sound sound, @NotNull Sound.Source source, float volume, float pitch) {
        this.sound = Sound.sound(sound.getKey(), source, volume, pitch);
    }

    public @NotNull Sound getSound() { return sound; }

    public CreateSound play(@NotNull Location l){
        l.getWorld().playSound(sound, l.getX(), l.getY(), l.getZ());
        return this;
    }

    public CreateSound play(@NotNull Entity l){
        l.getWorld().playSound(sound, l.getLocation().getX(), l.getLocation().getY(), l.getLocation().getZ());
        return this;
    }

    public CreateSound play(@NotNull Player l, boolean global){
        if(global) l.getWorld().playSound(sound, l.getLocation().getX(), l.getLocation().getY(), l.getLocation().getZ());
        else l.playSound(sound);
        return this;
    }
}
