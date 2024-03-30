package killerceepr.oogabooga.config;

import killerceepr.oogabooga.config.data.ConfigValue;
import killerceepr.oogabooga.config.data.GenericValue;
import killerceepr.oogabooga.config.data.MsgContainerValue;
import killerceepr.oogabooga.data.CreateSound;
import killerceepr.oogabooga.data.CreateTitle;
import killerceepr.oogabooga.data.MsgContainer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Config extends CruxConfig {
    public enum V{
        NETHER_ROOF_Y(new GenericValue(128D)),
        NETHER_ROOF_MAX_DISTANCE(new GenericValue(50D)),

        IGNORE_ROOF_TRAVEL_RESTRICTION_PERMISSION(new GenericValue("oogaboogadeluxe.ignore_roof_travel_restriction"),
                "Players who have this permission will ignore the roof restrictions.",
                "You can remove this parameter entirely to remove the permission and therefor make it so it's not possible to ignore",
                "the restriction."),

        NETHER_ROOF_BORDER_PUSH_BACK(new GenericValue(.2D),
                "This is the amount of force that the player will be pushed back with when they try to",
                "leave the roof border.",
                "I recommend keeping it around 0.1 to 0.2. If you set it too low or have none at all, players will be able",
                "to negate fall damage by sticking to the edge of the border while falling.",
                "This is due to a limitation that Minecraft has."),

        NO_ROOF_TRAVEL_MSG(new MsgContainerValue(new MsgContainer(List.of("<red>You cannot go any further.", "Go below the roof to reset your distance.")))),

        NOT_ON_NETHER_ROOF_MSG(new MsgContainerValue(new MsgContainer("<red>You must be on the nether roof to use this."))),
        CHECK_ROOF_DISTANCE_MSG(new MsgContainerValue(new MsgContainer(List.of(
                "Your distance from <red><roof_x> <roof_z> <reset>is <blue><distance>/<max_distance> <reset>blocks.",
                "",
                "<red>No Roof Travel! <reset>Go below the roof to reset your distance.",
                "Join <click:open_url:\"https://docs.advntr.dev/minimessage/format.html\"><hover:show_text:\"Click to join the Discord!\">" +
                        "<aqua><u>Discord<reset> for more info."
        )))),

        PLAYER_ONLY_COMMAND_MSG(new MsgContainerValue(new MsgContainer("<red>Only players may use this command."))),

        RELOADING_CONFIGS_MSG(new MsgContainerValue(new MsgContainer("Reloading configs.",
                "Reloading configs...",
                new CreateTitle("Reloading", "configs!", Title.Times.times(Duration.ofMillis(50*3), Duration.ofMillis(50*15), Duration.ofMillis(50*3))),
                new CreateSound(Sound.BLOCK_NOTE_BLOCK_PLING, 1.5f))),
                "All of the messages have optional chat, action_bar, title, and sound parameters.",
                "This reload configs message has all of them set so you can get an idea of the formatting."),
        ;
        private final ConfigValue<?> type;
        private final String path;
        private final String[] comments;

        V(@NotNull ConfigValue<?> type, @Nullable String... comments) {
            this.type = type;
            this.path = this.toString().toLowerCase();
            this.comments = comments;
        }

        V(@NotNull String path, @NotNull ConfigValue<?> type, @Nullable String... comments) {
            this.type = type;
            this.path = path;
            this.comments = comments;
        }

        public @NotNull ConfigValue<?> getType() {
            return type;
        }

        public <T extends ConfigValue<?>> @NotNull Optional<T> getType(@NotNull Class<T> clazz) {
            if(clazz.isAssignableFrom(type.getClass())) return Optional.of((T) type);
            return Optional.empty();
        }

        public <T extends ConfigValue<?>> @NotNull T getTypeOrThrow(@NotNull Class<T> clazz) {
            return getType(clazz).orElseThrow();
        }

        public @Nullable String[] getComments() {
            return comments;
        }

        public @NotNull String getPath() {
            return path;
        }

        //Convenience methods
        public @Nullable Object getValue() {
            return type.getValue();
        }

        public @Nullable String getString(){
            return type.getString();
        }

        public MsgContainer use(@NotNull Consumer<MsgContainer> consumer){
            if(type instanceof MsgContainerValue v){
                MsgContainer msg = v.getValue();
                if(msg != null) consumer.accept(msg);
                return msg;
            }
            return null;
        }
        public MsgContainer use(@NotNull Audience a){
            return use(a, (TagResolver[]) null);
        }
        public MsgContainer use(@NotNull Audience a, @NotNull TagResolver@Nullable... tags){
            if(type instanceof MsgContainerValue v){
                MsgContainer msg = v.getValue();
                if(msg != null) msg.use(a, null, tags);
                return msg;
            }
            return null;
        }

        public @Nullable Number getNumber(){
            return type.getNumber();
        }

        public int getInt(){
            return type.getInt();
        }

        public double getDouble(){
            return type.getDouble();
        }

        public float getFloat(){
            return type.getFloat();
        }

        public long getLong(){
            return type.getLong();
        }

        public boolean getBoolean(){
            return type.getBoolean();
        }

        public static void setDefaultValues(@NotNull CruxConfig cfg){
            for(V v : V.values()){
                v.getType().setDefault(cfg, v.getPath());
                cfg.setComments(v.getPath(), v.getComments());
            }
        }

        public static void registerValues(@NotNull CruxConfig cfg){
            for(V v : V.values()){
                v.getType().register(cfg, v.getPath());
            }
        }
    }

    public Config(@NotNull Plugin plugin) {
        super(plugin,"config");
    }

    @Override
    protected void setDefaults() {
        V.setDefaultValues(this);
    }

    @Override
    public void register() {
        V.registerValues(this);
    }
}
