package me.lorenzo0111.parkour.commands.subcommands;

import me.lorenzo0111.parkour.commands.SubCommand;
import me.lorenzo0111.parkour.data.flat.MessagesFile;
import me.lorenzo0111.parkour.data.flat.Parkour;
import me.lorenzo0111.parkour.data.flat.ParkourFile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EndCommand extends SubCommand {

    @Override
    public void perform(Player sender, String[] args) {
        Parkour parkour = fromArgs(sender,"end {name} [checkpoint]",args);
        if (parkour == null) {
            return;
        }

        parkour.setEnd(sender.getLocation());
        ParkourFile.getInstance().add(parkour);

        sender.sendMessage(MessagesFile.getInstance().getMessage("commands.end").replace("{name}", parkour.getName()));
    }

    @Override
    public @Nullable String permission() {
        return "parkour.admin.end";
    }

    @Override
    public List<String> complete() {
        return new ArrayList<>(ParkourFile.getInstance().getParkours().keySet());
    }
}
