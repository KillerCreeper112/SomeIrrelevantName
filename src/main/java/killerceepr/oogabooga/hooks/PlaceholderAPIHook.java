package killerceepr.oogabooga.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPIHook {
    public @NotNull String setPlaceholders(@Nullable Player p, @NotNull String text){
        return PlaceholderAPI.setPlaceholders(p, text);
    }

    public @NotNull String setPlaceholders(@Nullable OfflinePlayer p, @NotNull String text){
        return PlaceholderAPI.setPlaceholders(p, text);
    }
}
