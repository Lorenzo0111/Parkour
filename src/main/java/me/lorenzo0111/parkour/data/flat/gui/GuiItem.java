package me.lorenzo0111.parkour.data.flat.gui;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.builder.SkullBuilder;
import de.studiocode.invui.item.impl.SimpleItem;
import me.lorenzo0111.parkour.utils.GuiItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GuiItem {
    private final GuiFile file;
    private final Map<String,String> placeholders = new HashMap<>();
    private final String key;

    public GuiItem(GuiFile file, String key) {
        this.file = file;
        this.key = key;
    }

    public GuiItem registerPlaceholder(String placeholder, String value) {
        placeholders.put("{" + placeholder + "}",value);
        return this;
    }

    public SimpleItem toItem() {
        return new SimpleItem(toProvider());
    }

    public ItemProvider toProvider() {
        return GuiItemBuilder.getProvider(file.getConfig().getConfigurationSection(key), this::replacePlaceholders);
    }

    private String replacePlaceholders(String line) {
        for (Map.Entry<String,String> entry : placeholders.entrySet()) {
            line = line.replace(entry.getKey(), entry.getValue());
        }

        return file.replacePlaceholders(line);
    }
}
