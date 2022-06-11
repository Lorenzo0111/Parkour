package me.lorenzo0111.parkour.commands.subcommands;

import me.lorenzo0111.parkour.commands.SubCommand;
import me.lorenzo0111.parkour.data.flat.Checkpoint;
import me.lorenzo0111.parkour.data.flat.Parkour;
import me.lorenzo0111.parkour.data.flat.ParkourFile;
import me.lorenzo0111.parkour.data.flat.gui.GuiFile;
import me.lorenzo0111.parkour.data.flat.gui.GuiItem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InfoCommand extends SubCommand {

    @Override
    public void perform(Player sender, String[] args) {
        Parkour parkour = fromArgs(sender,"info {name}",args);
        if (parkour == null) return;

        GuiFile file = GuiFile.get("info")
                .registerPlaceholder("name", parkour.getName())
                .bind('S', "start")
                .bind('E', "end");

        List<GuiItem> items = new ArrayList<>();

        for (int i = 0; i < parkour.getCheckpoints().size(); i++) {
            Checkpoint checkpoint = parkour.getCheckpoints().get(i);

            String name = String.valueOf(i+1);

            items.add(new GuiItem(file,"checkpoint")
                    .registerPlaceholder("name", name)
                    .registerPlaceholder("x", String.valueOf(checkpoint.location().getBlockX()))
                    .registerPlaceholder("y", String.valueOf(checkpoint.location().getBlockY()))
                    .registerPlaceholder("z", String.valueOf(checkpoint.location().getBlockZ()))
            );
        }

        file.setItems(items).toGUI(sender).show();
    }

    @Override
    public @Nullable String permission() {
        return null;
    }

    @Override
    public List<String> complete() {
        return new ArrayList<>(ParkourFile.getInstance().getParkours().keySet());
    }

}
