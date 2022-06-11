package me.lorenzo0111.parkour.commands.subcommands;

import me.lorenzo0111.parkour.commands.SubCommand;
import me.lorenzo0111.parkour.data.flat.Parkour;
import me.lorenzo0111.parkour.data.flat.ParkourFile;
import me.lorenzo0111.parkour.data.flat.gui.GuiFile;
import me.lorenzo0111.parkour.gui.TopGUI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TopCommand extends SubCommand {

    @Override
    public void perform(Player sender, String[] args) {
        Parkour parkour = fromArgs(sender,"top {name}",args);
        if (parkour == null) {
            return;
        }

        GuiFile file = new TopGUI(parkour)
                .registerPlaceholder("name",parkour.getName())
                .bind('I', "parkour");

        file.toGUI(sender).show();
    }

    @Override
    public @Nullable String permission() {
        return "parkour.top";
    }

    @Override
    public List<String> complete() {
        return new ArrayList<>(ParkourFile.getInstance().getParkours().keySet());
    }


}
