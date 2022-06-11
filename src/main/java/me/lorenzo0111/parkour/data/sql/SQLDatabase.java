package me.lorenzo0111.parkour.data.sql;

import com.glyart.mystral.database.AsyncDatabase;
import com.glyart.mystral.database.Credentials;
import com.glyart.mystral.database.Mystral;
import me.lorenzo0111.parkour.ParkourPlugin;
import me.lorenzo0111.parkour.utils.Reloadable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Types;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SQLDatabase implements Reloadable {
    private static final SQLDatabase instance = new SQLDatabase();
    private AsyncDatabase database;

    public void connect(ParkourPlugin plugin, Credentials credentials) {
        this.database = Mystral.newAsyncDatabase(credentials, (command) -> Bukkit.getScheduler().runTaskAsynchronously(plugin, command));
        this.database.update("CREATE TABLE IF NOT EXISTS times ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT, "
                + "parkour TEXT NOT NULL, "
                + "uuid CHAR(36) CHARACTER SET ascii NOT NULL, "
                + "time INTEGER NOT NULL);", false);
    }

    public void save(Time object) {
        this.ensureConnected();

        this.get(object.player(), object.parkour()).whenComplete((previous,error) -> {
            if (previous == null || previous.time() > object.time()) {
                this.remove(previous).whenComplete((unused,ex) -> {
                    this.database.update("INSERT INTO `times`(`parkour`,`uuid`,`time`) VALUES (?,?,?);", (a) -> {
                        a.setString(1, object.parkour());
                        a.setString(2, object.player().toString());
                        a.setLong(3, object.time());
                    }, false);
                });
            }
        });
    }

    public CompletableFuture<Time> get(int key) {
        this.ensureConnected();

        return this.database.queryForObject("SELECT * FROM times WHERE `id` = ?;",
                new Object[]{key},
                (s,n) -> new Time(s.getString("parkour"), UUID.fromString(s.getString("uuid")), s.getLong("time")),
                Types.VARCHAR);
    }

    public CompletableFuture<List<Time>> get(Player player) {
        this.ensureConnected();

        return this.database.queryForList("SELECT * FROM times WHERE `uuid` = ?;",
                new Object[]{player.getUniqueId().toString()},
                (s,n) -> new Time(s.getString("parkour"), UUID.fromString(s.getString("uuid")), s.getLong("time")),
                Types.VARCHAR);
    }

    public CompletableFuture<List<Time>> get(String parkour) {
        this.ensureConnected();

        return this.database.queryForList("SELECT * FROM times WHERE `parkour` = ?;",
                new Object[]{parkour},
                (s,n) -> new Time(s.getString("parkour"), UUID.fromString(s.getString("uuid")), s.getLong("time")),
                Types.VARCHAR);
    }

    public CompletableFuture<Time> get(UUID player, String parkour) {
        this.ensureConnected();

        return this.database.queryForObject("SELECT * FROM times WHERE `parkour` = ? AND `uuid` = ?;",
                new Object[]{parkour,player.toString()},
                (s,n) -> new Time(s.getString("parkour"), UUID.fromString(s.getString("uuid")), s.getLong("time")),
                Types.VARCHAR,Types.VARCHAR);
    }

    public CompletableFuture<List<Time>> getTop(String parkour, int limit, int offset) {
        this.ensureConnected();

        return this.database.queryForList("SELECT * FROM times WHERE `parkour` = ? ORDER BY time LIMIT " + limit + " OFFSET " + offset + ";",
                new Object[]{parkour},
                (s,n) -> new Time(s.getString("parkour"), UUID.fromString(s.getString("uuid")), s.getLong("time")),
                Types.VARCHAR);
    }

    public CompletableFuture<Integer> remove(Time time) {
        this.ensureConnected();

        return this.database.update("DELETE FROM times WHERE `uuid` = ? AND `time` = ? AND `parkour` = ?;", (s) -> {
            s.setString(1,time.player().toString());
            s.setLong(2,time.time());
            s.setString(3,time.parkour());
        }, false);
    }

    protected void ensureConnected() {
        if (this.database == null) {
            throw new IllegalStateException("Database is not connected!");
        }
    }

    public static SQLDatabase getInstance() {
        return instance;
    }

    @Override
    public void reload() {
        this.database = null;

        ParkourPlugin plugin = ParkourPlugin.getPlugin(ParkourPlugin.class);
        Credentials credentials = plugin.buildCredentials();

        this.connect(plugin, credentials);
    }
}
