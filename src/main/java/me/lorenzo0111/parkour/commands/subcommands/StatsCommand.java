package me.lorenzo0111.parkour.commands.subcommands;

import de.studiocode.invui.window.impl.single.SimpleWindow;
import me.lorenzo0111.parkour.ParkourPlugin;
import me.lorenzo0111.parkour.commands.SubCommand;
import me.lorenzo0111.parkour.data.flat.MessagesFile;
import me.lorenzo0111.parkour.data.flat.gui.GuiFile;
import me.lorenzo0111.parkour.data.flat.gui.GuiItem;
import me.lorenzo0111.parkour.data.sql.SQLDatabase;
import me.lorenzo0111.parkour.data.sql.Time;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StatsCommand extends SubCommand {

    @Override
    public void perform(Player sender, String[] args) {
        Player target = sender;

        if (args.length > 0) {
            target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(MessagesFile.getInstance().getMessage("errors.player-not-found"));
                return;
            }
        }

        final Player finalTarget = target;

        SQLDatabase.getInstance().get(target).whenComplete((stats,ex) -> {
            if (ex != null) {
                sender.sendMessage(MessagesFile.getInstance().getMessage("errors.database"));
                return;
            }

            try {
                if (stats == null) stats = new ArrayList<>();

                GuiFile file = GuiFile.get("stats")
                        .registerPlaceholder("name", finalTarget.getName())
                        .registerPlaceholder("player", finalTarget.getUniqueId().toString())
                        .registerPlaceholder("completed", String.valueOf(stats.size()))
                        .bind('P', "player");

                List<GuiItem> items = new ArrayList<>();
                for (Time time : stats) {
                    items.add(new GuiItem(file, "parkour")
                            .registerPlaceholder("name", time.parkour())
                            .registerPlaceholder("time", String.valueOf(time.time())));
                }

                SimpleWindow window = file.setItems(items).toGUI(sender);

                Bukkit.getScheduler().runTask(ParkourPlugin.getInstance(), window::show);
            } catch (Exception e) {
                sender.sendMessage(MessagesFile.getInstance().getMessage("errors.database"));
                e.printStackTrace();
            }
        });
    }

    @Override
    public @Nullable String permission() {
        return "parkour.stats";
    }

}
