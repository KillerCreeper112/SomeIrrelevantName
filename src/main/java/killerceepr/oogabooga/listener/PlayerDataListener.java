package killerceepr.oogabooga.listener;

import killerceepr.oogabooga.data.PlayerMemory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDataListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        PlayerMemory.get(p).ifPresent(data -> data.saveAndUnregister(p, true));
    }
}
