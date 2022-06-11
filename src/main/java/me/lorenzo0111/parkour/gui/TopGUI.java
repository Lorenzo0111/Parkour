package me.lorenzo0111.parkour.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.lorenzo0111.parkour.ParkourPlugin;
import me.lorenzo0111.parkour.data.flat.MessagesFile;
import me.lorenzo0111.parkour.data.flat.Parkour;
import me.lorenzo0111.parkour.data.sql.SQLDatabase;
import me.lorenzo0111.parkour.data.sql.Time;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class TopGUI extends Gui {
    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacySection();

    public TopGUI(Player player, Parkour parkour) {
        super(3, MessagesFile.getInstance().getMessage("gui.top.title").replace("{name}", parkour.getName()), EnumSet.noneOf(InteractionModifier.class));

        this.setDefaultClickAction(e -> e.setCancelled(true));
        this.fetch(player,parkour,0);

        this.open(player);
    }

    public void fetch(Player player, Parkour parkour, int current) {
        for (int i = 1; i < 10; i++) {
            this.removeItem(2, i);
        }

        GuiItem filler = ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE)
                .name(Component.empty())
                .asGuiItem();
        this.getFiller().fillTop(filler);
        this.getFiller().fillBottom(filler);

        SQLDatabase.getInstance().getTop(parkour.getName(), 9, current).whenComplete((times, error) -> {
            try {
                if (error != null) {
                    error.printStackTrace();
                    player.sendMessage(MessagesFile.getInstance().getMessage("errors.database"));
                    return;
                }

                if (times == null) times = new ArrayList<>();

                List<GuiItem> items = new ArrayList<>();

                for (int i = 0; i < times.size(); i++) {
                    Time time = times.get(i);
                    OfflinePlayer target = Bukkit.getOfflinePlayer(time.player());

                    final int position = i+1;
                    items.add(ItemBuilder.skull()
                            .owner(target)
                            .name(SERIALIZER.deserialize(replacePlaceholders(MessagesFile.getInstance().getMessage("gui.top.user"),target.getName(),position,time.time())))
                            .lore(MessagesFile.getInstance()
                                    .getMessages("gui.top.lore")
                                    .stream()
                                    .map(s -> replacePlaceholders(s,target.getName(),position,time.time()))
                                    .map(SERIALIZER::deserialize)
                                    .collect(Collectors.toList())
                            )
                            .asGuiItem());
                }

                Bukkit.getScheduler().runTask(ParkourPlugin.getInstance(), () -> {
                    this.setItem(3,4, ItemBuilder.from(Material.STONE_BUTTON)
                            .name(SERIALIZER.deserialize(MessagesFile.getInstance().getMessage("gui.back")))
                            .asGuiItem(e -> {
                                if (current >= 9) {
                                    this.fetch(player, parkour, current - 9);
                                }
                            }));
                    this.setItem(3,6, ItemBuilder.from(Material.STONE_BUTTON)
                            .name(SERIALIZER.deserialize(MessagesFile.getInstance().getMessage("gui.next")))
                            .asGuiItem(e -> this.fetch(player,parkour,current + 9)));

                    items.forEach(this::addItem);
                    this.update();
                });
            } catch (Error | Exception e) {
                ParkourPlugin.getInstance().getLogger().log(Level.SEVERE, "An error has occurred", e);
            }
        });
    }

    private String replacePlaceholders(String string, String name, int position, long time) {
        return string.replace("{name}", name)
                .replace("{time}", String.valueOf(time))
                .replace("{rank}", String.valueOf(position));
    }
}
