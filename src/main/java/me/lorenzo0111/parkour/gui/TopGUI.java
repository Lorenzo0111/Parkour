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

package me.lorenzo0111.parkour.gui;

import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.gui.impl.SimplePagedItemsGUI;
import de.studiocode.invui.gui.structure.Markers;
import de.studiocode.invui.item.Item;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import me.lorenzo0111.parkour.data.flat.Parkour;
import me.lorenzo0111.parkour.data.flat.gui.GuiFile;
import me.lorenzo0111.parkour.data.flat.gui.GuiItem;
import me.lorenzo0111.parkour.gui.items.TimeItem;
import me.lorenzo0111.parkour.gui.items.TimeMoveItem;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TopGUI extends GuiFile {
    private final Parkour parkour;
    private int current = 0;

    public TopGUI(Parkour parkour) {
        super("top");

        this.parkour = parkour;
    }

    @Override
    public SimpleWindow toGUI(Player player) {
        List<String> structure = this.getConfig().getStringList("structure");
        List<Item> timeItems = new ArrayList<>();
        for (String line : structure) {
            for (char c : line.toCharArray()) {
                if (c == 'x') {
                    timeItems.add(new TimeItem(new GuiItem(this, "item")));
                }
            }
        }

        GUIBuilder<SimplePagedItemsGUI> builder = new GUIBuilder<>(GUIType.PAGED_ITEMS)
                .setStructure(structure.toArray(new String[0]))
                .addIngredient('#', getItem("filler"))
                .addIngredient('x', Markers.ITEM_LIST_SLOT_HORIZONTAL)
                .addIngredient('<', new TimeMoveItem(false, this))
                .addIngredient('>', new TimeMoveItem(true, this))
                .setItems(timeItems);

        for (char c : ingredients.keySet()) {
            builder.addIngredient(c, ingredients.get(c).toItem());
        }

        return new SimpleWindow(player, replacePlaceholders(this.getConfig().getString("title", "&cCan't find title")), builder.build());
    }

    public Parkour getParkour() {
        return parkour;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
