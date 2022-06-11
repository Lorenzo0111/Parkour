package me.lorenzo0111.parkour.commands.subcommands;

import me.lorenzo0111.parkour.commands.SubCommand;
import me.lorenzo0111.parkour.data.flat.Checkpoint;
import me.lorenzo0111.parkour.data.flat.MessagesFile;
import me.lorenzo0111.parkour.data.flat.Parkour;
import me.lorenzo0111.parkour.data.flat.ParkourFile;
import me.lorenzo0111.parkour.hologram.Hologram;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CheckpointCommand extends SubCommand {

    @Override
    public void perform(Player sender, String[] args) {
        Parkour parkour = fromArgs(sender,"checkpoint",args);
        if (parkour == null) {
            return;
        }

        parkour.getCheckpoints().add(new Checkpoint(sender.getLocation(),new Hologram(sender.getLocation(), MessagesFile.getInstance().getMessage("hologram.checkpoint")
                .replace("{name}", parkour.getName())
                .replace("{count}", String.valueOf(parkour.getCheckpoints().size()+1)))));
        ParkourFile.getInstance().add(parkour);

        sender.sendMessage(MessagesFile.getInstance().getMessage("commands.checkpoint").replace("{name}", parkour.getName()));
    }

    @Override
    public @Nullable String permission() {
        return "parkour.admin.checkpoint";
    }

    @Override
    public List<String> complete() {
        return new ArrayList<>(ParkourFile.getInstance().getParkours().keySet());
    }
}
