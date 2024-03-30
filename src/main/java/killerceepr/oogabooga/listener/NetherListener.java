package killerceepr.oogabooga.listener;

import io.papermc.paper.event.entity.EntityMoveEvent;
import killerceepr.oogabooga.OogaBoogaDeluxe;
import killerceepr.oogabooga.config.Config;
import killerceepr.oogabooga.data.PlayerMemory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class NetherListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        //128 (maybe 127)
        Player p = event.getPlayer();
        if(event.getTo().getWorld().getEnvironment() != World.Environment.NETHER) return;
        if(!event.hasChangedPosition()) return;
        if(roofCheckLogic(p, event.getTo(), event.getFrom())){
            event.setTo(clamp(p, event.getTo()));
            Bukkit.getScheduler().runTask(OogaBoogaDeluxe.inst(), task ->{
                Location c = PlayerMemory.getOrCreate(p).getEnteredNetherRoof();
                if(c == null) return;
                Vector dir = c.toVector().subtract(event.getFrom().toVector()).normalize();
                p.setVelocity(p.getVelocity().add(dir.multiply(Config.V.NETHER_ROOF_BORDER_PUSH_BACK.getDouble())));
            });
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityMove(EntityMoveEvent event) {
        Entity e = event.getEntity();
        if(e.getWorld().getEnvironment() != World.Environment.NETHER) return;
        e.getPassengers().forEach(entity ->{
            //if it's not a player get it the hell outta here
            if(!(entity instanceof Player p)) return;
            PlayerMemory data = PlayerMemory.getOrCreate(p);
            if(data.getEnteredNetherRoof() == null) return;
            if(!check(data.getEnteredNetherRoof(), p.getLocation())){
                p.teleport(clamp(data.getEnteredNetherRoof(), p.getLocation()));
            }
        });
    }

    //i know what you're thinking... BOILER PLATE CODE AHHHHHHHHHHH
    //and you would be right but are you going to do anything about it? no
    @EventHandler(ignoreCancelled = true)
    public void onVehicleMove(VehicleMoveEvent event) {
        Entity e = event.getVehicle();
        if(e.getWorld().getEnvironment() != World.Environment.NETHER) return;
        e.getPassengers().forEach(entity ->{
            //if it's not a player get it the hell outta here
            if(!(entity instanceof Player p)) return;
            PlayerMemory data = PlayerMemory.getOrCreate(p);
            if(data.getEnteredNetherRoof() == null) return;
            if(!check(data.getEnteredNetherRoof(), p.getLocation())){
                p.teleport(clamp(data.getEnteredNetherRoof(), p.getLocation()));
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        PlayerMemory data = PlayerMemory.getOrCreate(p);
        Location c = data.getEnteredNetherRoof();
        if(c == null) return;
        Entity e = event.getRightClicked();
        if(!check(c, e.getLocation())) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player p = event.getPlayer();
        if(event.getFrom().getEnvironment() == World.Environment.NETHER){
            PlayerMemory.getOrCreate(p).setEnteredNetherRoof(null);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player p = event.getPlayer();
        if(event.getTo().getWorld().getEnvironment() != World.Environment.NETHER) return;
        if(!event.hasChangedPosition()) return;
        if(roofCheckLogic(p, event.getTo(), event.getFrom())){
            event.setTo(event.getFrom());
        }
    }

    /**
     * @return Whether the event should be cancelled.
     */
    public boolean roofCheckLogic(@NotNull Player p, @NotNull Location to, @NotNull Location from){
        String perm = Config.V.IGNORE_ROOF_TRAVEL_RESTRICTION_PERMISSION.getString();
        if(perm != null && p.hasPermission(perm)) return false;
        PlayerMemory data = PlayerMemory.getOrCreate(p);
        Location center = data.getEnteredNetherRoof();
        double MAX_Y = Config.V.NETHER_ROOF_Y.getDouble();
        if(center != null){
            if(to.getY() < MAX_Y){
                data.setEnteredNetherRoof(null);
                return false;
            }
            if(!check(center, to)){
                Config.V.NO_ROOF_TRAVEL_MSG.use(p, OogaBoogaDeluxe.tags(from, center));
                return true;
            }
            return false;
        }
        if(to.getY() >= MAX_Y) data.setEnteredNetherRoof(to.clone());
        return false;
    }

    private Location clamp(@NotNull Player p, @NotNull Location to){
        PlayerMemory data = PlayerMemory.getOrCreate(p);
        Location center = data.getEnteredNetherRoof();
        if(center == null) return to;
        return clamp(center, to);
    }

    private Location clamp(@NotNull Location center, @NotNull Location to) {
        double maxDistance = Config.V.NETHER_ROOF_MAX_DISTANCE.getDouble();

        // Clamp the location "to" inside the cube if it is outside
        double clampedX = Math.min(center.getX() + maxDistance, Math.max(center.getX() - maxDistance, to.getX()));
        double clampedZ = Math.min(center.getZ() + maxDistance, Math.max(center.getZ() - maxDistance, to.getZ()));

        // Update the location "to" with the clamped values
        to.setX(clampedX);
        to.setZ(clampedZ);

        // Check if the distances along each axis are within the allowed range
        return to;
    }

    private boolean check(@NotNull Location center, @NotNull Location to){
        double maxDistance = Config.V.NETHER_ROOF_MAX_DISTANCE.getDouble();

        double deltaX = Math.abs(center.getX() - to.getX());
        double deltaZ = Math.abs(center.getZ() - to.getZ());

        // Check if the distances along each axis are within the allowed range
        return deltaX <= maxDistance && deltaZ <= maxDistance;
    }
}
