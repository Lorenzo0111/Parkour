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
