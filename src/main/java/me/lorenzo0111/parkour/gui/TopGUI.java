package me.lorenzo0111.parkour.gui;

import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.gui.impl.SimpleGUI;
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
            builder.addIngredient(c, getItem(ingredients.get(c)));
        }

        return new SimpleWindow(player, replacePlaceholders(this.getConfig().getString("title", "&cCan't find title")),builder.build());
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
