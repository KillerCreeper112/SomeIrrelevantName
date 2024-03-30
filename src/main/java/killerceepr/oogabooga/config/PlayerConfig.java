package killerceepr.oogabooga.config;

import com.google.gson.JsonObject;
import killerceepr.oogabooga.OogaBoogaDeluxe;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.UUID;

public class PlayerConfig extends CruxJson {
    public PlayerConfig(@NotNull UUID uuid) {
        super(OogaBoogaDeluxe.inst(),"player/" + uuid);
    }

    public PlayerConfig(@NotNull File file) {
        super(file);
    }

    public @Nullable UUID uuid(){
        try{ return UUID.fromString(file.getName().replace(".json", "")); }
        catch (IllegalArgumentException ignored){ return null; }
    }

    public @Nullable Location enteredNetherRoof(){
        if(json==null) return null;
        if(json.get("entered_nether_roof") instanceof JsonObject o){
            return new Location(null,
                    o.get("x").getAsDouble(),
                    o.get("y").getAsDouble(),
                    o.get("z").getAsDouble());
        }
        return null;
    }

    public PlayerConfig enteredNetherRoof(@Nullable Location value){
        reloadIfNeeded();
        if(value == null){
            json.remove("entered_nether_roof");
            return this;
        }
        JsonObject o = new JsonObject();
        o.addProperty("x", value.getX());
        o.addProperty("y", value.getY());
        o.addProperty("z", value.getZ());
        json.add("entered_nether_roof", o);
        return this;
    }
}
