package me.lorenzo0111.parkour.utils;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.builder.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.UUID;

public final class GuiItemBuilder {

    public static ItemProvider getProvider(ConfigurationSection section, PlaceholderReplacer replacer) {
        if (section == null) return null;

        ItemProvider stack;

        if (section.getString("material", "STONE").equals("{player}")) {
            List<String> lore = section.getStringList("lore")
                    .stream()
                    .map(replacer::replacePlaceholders).toList();

            SkullBuilder builder = new SkullBuilder(UUID.fromString(replacer.replacePlaceholders("{player}")));
            builder.setDisplayName(replacer.replacePlaceholders(section.getString("name", "&cUnknown")));
            builder.setLegacyLore(lore);

            stack = builder;
        } else {
            ItemBuilder builder = new ItemBuilder(Material.valueOf(section.getString("material", "STONE")))
                    .setDisplayName(replacer.replacePlaceholders(section.getString("name", "&cUnknown")));

            for (String line : section.getStringList("lore")) {
                builder.addLoreLines(replacer.replacePlaceholders(line));
            }

            stack = builder;
        }

        return stack;
    }

    public interface PlaceholderReplacer {
        String replacePlaceholders(String line);
    }
}
