package me.lorenzo0111.parkour.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.lorenzo0111.parkour.data.flat.Checkpoint;
import me.lorenzo0111.parkour.data.flat.MessagesFile;
import me.lorenzo0111.parkour.data.flat.Parkour;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.stream.Collectors;

public class InfoGUI extends PaginatedGui {
    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacySection();

    public InfoGUI(Player player, Parkour parkour) {
        super(3,9, MessagesFile.getInstance().getMessage("gui.info.title").replace("{name}", parkour.getName()), EnumSet.noneOf(InteractionModifier.class));

        this.setDefaultClickAction(e -> e.setCancelled(true));

        this.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE)
                .name(Component.empty())
                .asGuiItem());

        this.setItem(1,5, ItemBuilder.from(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)
                .name(SERIALIZER.deserialize(replacePlaceholders(MessagesFile.getInstance().getMessage("gui.info.start.name"), parkour.getStart(), parkour.getName())))
                .lore(MessagesFile.getInstance()
                                .getMessages("gui.info.start.lore")
                                .stream()
                                .map(s -> replacePlaceholders(s,parkour.getStart(),parkour.getName()))
                                .map(SERIALIZER::deserialize)
                                .collect(Collectors.toList())
                )
                .asGuiItem());

        this.setItem(3,4, ItemBuilder.from(Material.STONE_BUTTON)
                .name(SERIALIZER.deserialize(MessagesFile.getInstance().getMessage("gui.back")))
                .asGuiItem(e -> this.previous()));
        this.setItem(3,6, ItemBuilder.from(Material.STONE_BUTTON)
                .name(SERIALIZER.deserialize(MessagesFile.getInstance().getMessage("gui.next")))
                .asGuiItem(e -> this.next()));

        for (int i = 0; i < parkour.getCheckpoints().size(); i++) {
            Checkpoint checkpoint = parkour.getCheckpoints().get(i);

            String name = String.valueOf(i+1);

            this.addItem(ItemBuilder.from(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)
                    .name(SERIALIZER.deserialize(replacePlaceholders(MessagesFile.getInstance().getMessage("gui.info.checkpoint.name"), checkpoint.location(), name)))
                    .lore(MessagesFile.getInstance()
                            .getMessages("gui.info.checkpoint.lore")
                            .stream()
                            .map(s -> replacePlaceholders(s,checkpoint.location(),name))
                            .map(SERIALIZER::deserialize)
                            .collect(Collectors.toList())
                    )
                    .amount(i+1)
                    .asGuiItem());
        }

        this.open(player);
    }

    private String replacePlaceholders(String string, Location location, String name) {
        return string.replace("{x}", String.valueOf(location.getBlockX()))
                .replace("{y}", String.valueOf(location.getBlockY()))
                .replace("{z}", String.valueOf(location.getBlockZ()))
                .replace("{name}", name);
    }

}
