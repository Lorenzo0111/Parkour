/*
 * This file is part of Parkour, licensed under the MIT License.
 *
 *  Copyright (c) Lorenzo0111
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.lorenzo0111.parkour;

import com.glyart.mystral.database.Credentials;
import me.lorenzo0111.parkour.commands.ParkourCommand;
import me.lorenzo0111.parkour.data.cache.ChallengeHandler;
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
    private final List<Reloadable> reloadables = new ArrayList<>();
    private SQLDatabase database;
    private ParkourFile parkours;
    private MessagesFile messages;
    private HologramManager holograms;
    private ChallengeHandler challenges;

    @Override
    public void onEnable() {
        Instant start = Instant.now();
        this.saveDefaultConfig();

        this.getLogger().info("----------------------------------------------------");
        this.getLogger().info("Parkour v" + this.getDescription().getVersion());
        this.getLogger().info("----------------------------------------------------");
        this.getLogger().info("Connecting to the database...");

        reloadables.add(database = new SQLDatabase().connect(this, buildCredentials()));

        this.getLogger().info("Database connected!");
        this.getLogger().info("----------------------------------------------------");
        this.getLogger().info("Loading messages...");

        reloadables.add(messages = new MessagesFile(this.getDataFolder()));

        this.getLogger().info("Loaded messages!");
        this.getLogger().info("----------------------------------------------------");

        this.getLogger().info("Loading parkours...");

        reloadables.add(holograms = new HologramManager());

        try {
            reloadables.add(parkours = new ParkourFile(this,this.getDataFolder()));
        } catch (IOException e) {
            this.getLogger().severe("An error occurred while loading parkours!");
            e.printStackTrace();
            this.setEnabled(false);
            return;
        }
        this.getLogger().info("Loaded " + parkours.getParkours().size() + " parkours!");
        this.getLogger().info("----------------------------------------------------");

        PluginCommand command = this.getCommand("parkour");
        if (command == null) {
            this.getLogger().severe("Could not register the command, please do not edit the plugin.yml file manually.");
            this.setEnabled(false);
            return;
        }

        command.setExecutor(new ParkourCommand(this));

        challenges = new ChallengeHandler(this);

        Bukkit.getPluginManager().registerEvents(new MovementListener(this), this);

        Instant end = Instant.now();
        this.getLogger().info("Parkour v" + this.getDescription().getVersion() + " enabled in " + Duration.between(start, end).getSeconds() + " seconds.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Saving parkours...");

        if (parkours != null) {
            parkours.save();
        }

        this.getLogger().info("Saved parkours!");

        holograms.reload();
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

    public MessagesFile getMessages() {
        return messages;
    }

    public ParkourFile getParkours() {
        return parkours;
    }

    public HologramManager getHolograms() {
        return holograms;
    }

    public SQLDatabase getDatabase() {
        return database;
    }

    public ChallengeHandler getChallenges() {
        return challenges;
    }
}
