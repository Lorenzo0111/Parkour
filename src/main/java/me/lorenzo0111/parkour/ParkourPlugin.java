package me.lorenzo0111.parkour;

import com.glyart.mystral.database.Credentials;
import me.lorenzo0111.parkour.commands.ParkourCommand;
import me.lorenzo0111.parkour.data.flat.MessagesFile;
import me.lorenzo0111.parkour.data.flat.ParkourFile;
import me.lorenzo0111.parkour.data.sql.SQLDatabase;
import me.lorenzo0111.parkour.hologram.HologramManager;
import me.lorenzo0111.parkour.listeners.MovementListener;
import me.lorenzo0111.parkour.utils.Reloadable;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class ParkourPlugin extends JavaPlugin {
    private static ParkourPlugin instance;
    private final List<Reloadable> reloadables = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;

        Instant start = Instant.now();
        this.saveDefaultConfig();

        this.getLogger().info("----------------------------------------------------");
        this.getLogger().info("Parkour v" + this.getDescription().getVersion());
        this.getLogger().info("----------------------------------------------------");
        this.getLogger().info("Connecting to the database...");

        SQLDatabase.getInstance().connect(this, buildCredentials());

        reloadables.add(SQLDatabase.getInstance());

        this.getLogger().info("Database connected!");
        this.getLogger().info("----------------------------------------------------");
        this.getLogger().info("Loading messages...");

        reloadables.add(new MessagesFile(this.getDataFolder()));

        this.getLogger().info("Loaded messages!");
        this.getLogger().info("----------------------------------------------------");

        this.getLogger().info("Loading parkours...");

        reloadables.add(HologramManager.getInstance());

        try {
            reloadables.add(new ParkourFile(this.getDataFolder()));
        } catch (IOException e) {
            this.getLogger().severe("An error occurred while loading parkours!");
            e.printStackTrace();
            this.setEnabled(false);
            return;
        }
        this.getLogger().info("Loaded " + ParkourFile.getInstance().getParkours().size() + " parkours!");
        this.getLogger().info("----------------------------------------------------");

        PluginCommand command = this.getCommand("parkour");
        if (command == null) {
            this.getLogger().severe("Could not register the command, please do not edit the plugin.yml file manually.");
            this.setEnabled(false);
            return;
        }

        command.setExecutor(new ParkourCommand());

        Bukkit.getPluginManager().registerEvents(new MovementListener(),this);

        Instant end = Instant.now();
        this.getLogger().info("Parkour v" + this.getDescription().getVersion() + " enabled in " + Duration.between(start,end).getSeconds() + " seconds.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Saving parkours...");

        if (ParkourFile.getInstance() != null) {
            ParkourFile.getInstance().save();
        }

        this.getLogger().info("Saved parkours!");

        HologramManager.getInstance().reload();
    }

    public void reload() {
        this.reloadConfig();

        reloadables.forEach(Reloadable::reloadAndSave);
    }

    public Credentials buildCredentials() {
        return Credentials.builder()
                .host(this.getConfig().getString("mysql.host"))
                .port(this.getConfig().getInt("mysql.port"))
                .schema(this.getConfig().getString("mysql.database"))
                .user(this.getConfig().getString("mysql.username"))
                .password(this.getConfig().getString("mysql.password"))
                .pool("Parkour")
                .build();
    }

    public static ParkourPlugin getInstance() {
        return instance;
    }
}