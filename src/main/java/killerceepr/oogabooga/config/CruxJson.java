package killerceepr.oogabooga.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CruxJson extends CruxFolder {
    protected JsonObject json;
    protected FileReader reader;
    protected final Gson parser = new Gson();
    protected final boolean existedBefore;

    public CruxJson(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path + ".json");
        existedBefore = file.exists();
        reloadIfExists();
    }

    public CruxJson(@NotNull File file){
        super(file);
        existedBefore = file.exists();
        reloadIfExists();
    }

    public void reloadIfNeeded(){
        if(json == null) reload();
    }

    public JsonObject reloadIfExists(){
        if(!file.exists()) return null;
        reload();
        return json;
    }

    public void reload() {
        try {
            if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if(!file.exists()) {
                PrintWriter pw = new PrintWriter(file, StandardCharsets.UTF_8);
                pw.print("{");
                pw.print("}");
                pw.flush();
                pw.close();
            }
            if(reader == null) reader = new FileReader(file);
            json = parser.fromJson(reader, JsonObject.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public final boolean createDefault(){
        return createDefault(false);
    }

    public final boolean createDefault(boolean pretty){
        if(existedBefore) return false;
        setDefaults();
        return save(pretty);
    }

    public void setDefaults(){}

    public boolean save(){
        return save(false);
    }

    public boolean existedBefore(){ return existedBefore; }

    public boolean save(boolean pretty) {
        if(json == null) return false;
        if(json.size() < 1){
            try{ reader.close(); } catch (IOException ignored){}
            return file.delete();
        }
        try {
            String json = pretty ? new GsonBuilder().setPrettyPrinting().create().toJson(this.json) :
                    new GsonBuilder().create().toJson(this.json);
            FileWriter fw = new FileWriter(file);
            fw.write(json);
            fw.flush();
            fw.close();
            reader.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void close(){
        if(json == null) return;
        try{ reader.close(); } catch (IOException ignored){}
    }
}
