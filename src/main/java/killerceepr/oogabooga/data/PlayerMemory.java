package killerceepr.oogabooga.data;

import killerceepr.oogabooga.OogaBoogaDeluxe;
import killerceepr.oogabooga.config.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class PlayerMemory {
    private static final Map<UUID, PlayerMemory> REGISTRY = new HashMap<>();
    public static @NotNull Map<UUID, PlayerMemory> getRegistry(){ return REGISTRY; }

    public static @NotNull Optional<PlayerMemory> get(@NotNull Player p){
        return get(p.getUniqueId());
    }

    public static @NotNull Optional<PlayerMemory> get(@NotNull UUID uuid){
        return Optional.ofNullable(REGISTRY.getOrDefault(uuid, null));
    }

    public static @NotNull PlayerMemory getOrCreate(@NotNull Player p){
        return getOrCreate(p.getUniqueId());
    }

    public static @NotNull PlayerMemory getOrCreate(@NotNull UUID uuid){
        if(REGISTRY.containsKey(uuid)) return REGISTRY.get(uuid);
        PlayerMemory data = new PlayerMemory(uuid).load();
        REGISTRY.put(data.getUUID(), data);
        return data;
    }

    protected final UUID uuid;
    protected boolean loaded = false;

    protected @Nullable Location enteredNetherRoof;

    public @Nullable Location getEnteredNetherRoof() {
        return enteredNetherRoof;
    }

    public void setEnteredNetherRoof(@Nullable Location enteredNetherRoof) {
        this.enteredNetherRoof = enteredNetherRoof;
    }

    public PlayerMemory(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    public @Nullable Player getPlayer(){
        return Bukkit.getPlayer(uuid);
    }

    public @NotNull UUID getUUID() {
        return uuid;
    }

    protected boolean unregister = false;
    public PlayerMemory saveAndUnregister(@Nullable Player p, boolean async){
        unregister = true;
        if(async){
            Bukkit.getScheduler().runTaskAsynchronously(OogaBoogaDeluxe.inst(), x -> save(p));
        }else save(p);
        REGISTRY.remove(uuid);
        return this;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void save(@Nullable Player p, boolean async){
        if(async) Bukkit.getScheduler().runTaskAsynchronously(OogaBoogaDeluxe.inst(), x -> save(p));
        else save(p);
    }

    public @NotNull CompletableFuture<PlayerMemory> load(boolean async){
        if(async) return CompletableFuture.supplyAsync(this::load);
        else return CompletableFuture.completedFuture(load());
    }

    public PlayerMemory save(@Nullable Player p){
        if(!loaded){
            OogaBoogaDeluxe.log(Level.WARNING, "Player data of '" + uuid + "' tried to save before being loaded!");
            return this;
        }
        PlayerConfig cfg = new PlayerConfig(uuid);
        cfg.enteredNetherRoof(enteredNetherRoof);
        cfg.save();
        return this;
    }

    public PlayerMemory load(){
        PlayerConfig cfg = new PlayerConfig(uuid);
        setEnteredNetherRoof(cfg.enteredNetherRoof());
        cfg.close();
        loaded = true;
        return this;
    }
}
