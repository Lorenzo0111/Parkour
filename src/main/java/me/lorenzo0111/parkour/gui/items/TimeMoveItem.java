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

package me.lorenzo0111.parkour.gui.items;

import de.studiocode.invui.gui.SlotElement;
import de.studiocode.invui.gui.impl.SimplePagedItemsGUI;
import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.impl.controlitem.ControlItem;
import de.studiocode.invui.window.Window;
import me.lorenzo0111.parkour.ParkourPlugin;
import me.lorenzo0111.parkour.data.sql.Time;
import me.lorenzo0111.parkour.gui.TopGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class TimeMoveItem extends ControlItem<SimplePagedItemsGUI> {
    private final boolean forward;
    private final TopGUI gui;
    private final ItemProvider provider;
    private final ParkourPlugin plugin;
    private List<TimeItem> items = null;

    public TimeMoveItem(ParkourPlugin plugin, boolean forward, @NotNull TopGUI gui) {
        this.forward = forward;
        this.provider = gui.getItem(forward ? "next" : "back");
        this.gui = gui;
        this.plugin = plugin;
    }

    @Override
    public ItemProvider getItemProvider(SimplePagedItemsGUI g) {
        return provider;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (forward) {
            gui.setCurrent(gui.getCurrent() + 9);
        } else if (gui.getCurrent() >= 9) {
            gui.setCurrent(gui.getCurrent() - 9);
        }

        fetch(player);
    }

    private void fetch(Player player) {
        if (items == null) {
            items = Arrays.stream(this.getGui().getSlotElements())
                    .filter(e -> e instanceof SlotElement.ItemSlotElement element && element.getItem() instanceof TimeItem)
                    .map(e -> ((TimeItem) ((SlotElement.ItemSlotElement) e).getItem()))
                    .toList();
        }

        items.forEach(TimeItem::reset);

        plugin.getDatabase().getTop(gui.getParkour().getName(), 9, gui.getCurrent()).whenComplete((times, error) -> {
            try {
                if (error != null) {
                    error.printStackTrace();
                    player.sendMessage(plugin.getMessages().getMessage("errors.database"));
                    return;
                }

                if (times == null) times = new ArrayList<>();

                for (int i = 0; i < times.size(); i++) {
                    Time time = times.get(i);

                    final int position = i + 1;
                    items.get(i).setTime(time).setRank(position);
                }

                Bukkit.getScheduler().runTask(plugin, () -> items.forEach(TimeItem::notifyWindows));
            } catch (Error | Exception e) {
                plugin.getLogger().log(Level.SEVERE, "An error has occurred", e);
            }
        });
    }

    @Override
    public void addWindow(Window window) {
        super.addWindow(window);

        fetch(window.getCurrentViewer());
    }
}
