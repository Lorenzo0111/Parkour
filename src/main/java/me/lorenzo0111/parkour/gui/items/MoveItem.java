package me.lorenzo0111.parkour.gui.items;

import de.studiocode.invui.gui.impl.PagedGUI;
import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.impl.controlitem.PageItem;
import me.lorenzo0111.parkour.data.flat.gui.GuiFile;
import org.jetbrains.annotations.NotNull;

public class MoveItem extends PageItem {
    private final ItemProvider provider;

    public MoveItem(boolean forward, @NotNull GuiFile gui) {
        super(forward);

        this.provider = gui.getItem(forward ? "next" : "back");
    }

    @Override
    public ItemProvider getItemProvider(PagedGUI gui) {
        return provider;
    }
}
