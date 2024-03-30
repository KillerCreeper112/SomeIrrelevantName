package killerceepr.oogabooga.config;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class CruxFolder {
    public static @NotNull File file(@NotNull Plugin plugin, @NotNull String path){
        return new File(plugin.getDataFolder().getAbsolutePath() + "/" + path);
    }
    protected final @Nullable Plugin plugin;
    protected final File file;

    public CruxFolder(@NotNull Plugin plugin, @NotNull String path){
        this.plugin = plugin;
        file = file(plugin, path);
    }
    public CruxFolder(@NotNull File file){
        this.plugin = null;
        this.file = file;
    }

    public final @NotNull File file(){
        return file;
    }
}
