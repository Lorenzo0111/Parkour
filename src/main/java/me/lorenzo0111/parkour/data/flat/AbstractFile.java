package me.lorenzo0111.parkour.data.flat;

import me.lorenzo0111.parkour.ParkourPlugin;
import me.lorenzo0111.parkour.utils.Reloadable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class AbstractFile implements Reloadable {
    private final File file;
    private FileConfiguration config;

    public AbstractFile(File file) throws IOException {
        this.file = file;

        if (!this.file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        this.reload();
    }

    public AbstractFile(File file, boolean extract) {
        if (extract) {
            ParkourPlugin.getPlugin(ParkourPlugin.class).saveResource(file.getName(), false);
        }

        this.file = file;
        this.reload();
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void resetConfig() {
        this.config = new YamlConfiguration();
    }
}
