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

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import me.lorenzo0111.parkour.data.flat.gui.GuiItem;
import me.lorenzo0111.parkour.data.sql.Time;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class TimeItem extends BaseItem {
    private final GuiItem item;
    private Time time;
    private int rank;

    public TimeItem(GuiItem item) {
        this.item = item;
    }

    @Override
    public ItemProvider getItemProvider() {
        if (rank == -1 || time == null) {
            return new ItemBuilder(Material.AIR);
        }

        return item.registerPlaceholder("name", Bukkit.getOfflinePlayer(time.player()).getName())
                .registerPlaceholder("player", time.player().toString())
                .registerPlaceholder("time", String.valueOf(time.time()))
                .registerPlaceholder("rank", String.valueOf(rank))
                .toProvider();
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        event.setCancelled(true);
    }

    public TimeItem setTime(Time time) {
        this.time = time;
        return this;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void reset() {
        this.time = null;
        this.rank = -1;

        this.notifyWindows();
    }
}
