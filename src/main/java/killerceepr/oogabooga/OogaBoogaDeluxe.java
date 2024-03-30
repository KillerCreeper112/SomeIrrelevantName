package killerceepr.oogabooga;

import killerceepr.oogabooga.config.Config;
import killerceepr.oogabooga.config.CruxConfig;
import killerceepr.oogabooga.data.PlayerMemory;
import killerceepr.oogabooga.hooks.PlaceholderAPIHook;
import killerceepr.oogabooga.listener.NetherListener;
import killerceepr.oogabooga.listener.PlayerDataListener;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Level;

public class OogaBoogaDeluxe extends JavaPlugin {
    public static final MiniMessage FORMAT = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolvers(TagResolver.standard())
                    .build()
            ).build();
    private static OogaBoogaDeluxe instance;
    public static OogaBoogaDeluxe inst(){ return instance; }

    private PlaceholderAPIHook placeholderAPIHook;

    public @Nullable PlaceholderAPIHook getPlaceholderAPIHook(){ return placeholderAPIHook; }

    @Override
    public void onEnable() {
        instance = this;
        reloadConfigs();
        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")){
            placeholderAPIHook = new PlaceholderAPIHook();
        }else placeholderAPIHook = null;

        registerListeners(new PlayerDataListener(), new NetherListener());
    }

    public void reloadConfigs(){
        CruxConfig cfg = new Config(this);
        cfg.createDefault();
        cfg.register();
    }

    public void registerListeners(@NotNull Listener... listeners){
        for(Listener l : listeners){
            getServer().getPluginManager().registerEvents(l, this);
        }
    }


    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        for(Player p : Bukkit.getOnlinePlayers()){
            PlayerMemory.get(p).ifPresent(data -> data.saveAndUnregister(p, false));
        }
    }

    public static void log(@NotNull Level level, @NotNull String msg){
        inst().getLogger().log(level, msg);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        switch (cmd.getName()){
            case "reloadoogabooga" ->{
                Config.V.RELOADING_CONFIGS_MSG.use(sender);
                reloadConfigs();
            }
            case "roof" ->{
                if(!(sender instanceof Player p)){
                    Config.V.PLAYER_ONLY_COMMAND_MSG.use(sender);
                    return true;
                }
                PlayerMemory data = PlayerMemory.getOrCreate(p);
                Location center = data.getEnteredNetherRoof();
                if(center == null){
                    Config.V.NOT_ON_NETHER_ROOF_MSG.use(sender);
                    return true;
                }
                Config.V.CHECK_ROOF_DISTANCE_MSG.use(sender, tags(p.getLocation(), center));
            }
        }
        return true;
    }

    public static @NotNull TagResolver[] tags(@NotNull Location l, @NotNull Location center){
        double distance = getDistanceCuboid(center, l);
        double maxDistance = Config.V.NETHER_ROOF_MAX_DISTANCE.getDouble();
        NumberFormat format = new DecimalFormat("#,###");
        return new TagResolver[]{
                Placeholder.parsed("roof_x", format.format(center.getX())),
                Placeholder.parsed("roof_z", format.format(center.getZ())),
                Placeholder.parsed("distance", format.format(distance)),
                Placeholder.parsed("max_distance", format.format(maxDistance))
        };
    }

    public static double getDistanceCuboid(Location center, Location to) {
        double deltaX = Math.abs(center.getX() - to.getX());
        double deltaZ = Math.abs(center.getZ() - to.getZ());
        return Math.max(deltaX, deltaZ);
    }
}
