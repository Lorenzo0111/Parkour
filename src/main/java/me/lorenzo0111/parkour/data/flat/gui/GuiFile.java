package me.lorenzo0111.parkour.data.flat.gui;

import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.gui.impl.SimplePagedItemsGUI;
import de.studiocode.invui.gui.structure.Markers;
import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import me.lorenzo0111.parkour.ParkourPlugin;
import me.lorenzo0111.parkour.data.flat.AbstractFile;
import me.lorenzo0111.parkour.gui.items.MoveItem;
import me.lorenzo0111.parkour.utils.GuiItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class GuiFile extends AbstractFile {
    private static final Map<String,GuiFile> FILES = new HashMap<>();
    private static final File GUIS = new File(ParkourPlugin.getInstance().getDataFolder(), "guis");
    protected final Map<String,String> placeholders = new HashMap<>();
    protected final Map<Character,String> ingredients = new HashMap<>();
    private boolean created = false;
    protected List<GuiItem> items = new ArrayList<>();

    static {
        if (!GUIS.exists()) {
            GUIS.mkdirs();
        }
    }

    protected GuiFile(String name) {
        super(new File(GUIS, name + ".yml"), "guis/" + name + ".yml");

        created = true;
    }

    public GuiFile registerPlaceholder(String placeholder, String value) {
        placeholders.put("{" + placeholder + "}", value);
        return this;
    }

    public ItemProvider getItem(String key) {
        return GuiItemBuilder.getProvider(this.getConfig().getConfigurationSection(key), this::replacePlaceholders);
    }

    public SimpleWindow toGUI(Player player) {
        GUIBuilder<SimplePagedItemsGUI> builder = new GUIBuilder<>(GUIType.PAGED_ITEMS)
                .setStructure(this.getConfig().getStringList("structure").toArray(new String[0]))
                .addIngredient('x', Markers.ITEM_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', getItem("filler"))
                .addIngredient('<', new MoveItem(false, this))
                .addIngredient('>', new MoveItem(true, this))
                .setItems(items.stream().map(GuiItem::toItem).collect(Collectors.toList()));

        for (char c : ingredients.keySet()) {
            builder.addIngredient(c, getItem(ingredients.get(c)));
        }

        return new SimpleWindow(player, replacePlaceholders(this.getConfig().getString("title", "&cCan't find title")),builder.build());
    }

    public GuiFile setItems(List<GuiItem> items) {
        this.items = items;
        return this;
    }

    public GuiFile bind(char key, String ingredient) {
        ingredients.put(key,ingredient);
        return this;
    }

    protected String replacePlaceholders(String string) {
        for (Map.Entry<String,String> entry : placeholders.entrySet()) {
            string = string.replace(entry.getKey(), entry.getValue());
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    private void clearPlaceholders() {
        placeholders.clear();
    }


    public static GuiFile get(String name) {
        GuiFile file = FILES.computeIfAbsent(name, GuiFile::new);
        file.clearPlaceholders();
        return file;
    }

    @Override
    public void reload() {
        if (created) FILES.clear();
        else super.reload();
    }
}
