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
