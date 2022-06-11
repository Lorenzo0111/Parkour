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
