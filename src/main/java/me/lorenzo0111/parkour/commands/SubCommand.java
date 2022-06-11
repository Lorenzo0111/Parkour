package me.lorenzo0111.parkour.commands;

import me.lorenzo0111.parkour.data.flat.MessagesFile;
import me.lorenzo0111.parkour.data.flat.Parkour;
import me.lorenzo0111.parkour.data.flat.ParkourFile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SubCommand {

    public abstract void perform(Player sender, String[] args);
    public abstract @Nullable String permission();
    public List<String> complete() {
        return null;
    }

    public Parkour fromArgs(Player sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(MessagesFile.getInstance().getMessage("errors.usage").replace("{usage}", "/parkour top {name}"));
            return null;
        }

        Parkour parkour = ParkourFile.getInstance().getParkour(args[0]);
        if (parkour == null) {
            sender.sendMessage(MessagesFile.getInstance().getMessage("errors.not-exists").replace("{name}", args[0]));
            return null;
        }

        return parkour;
    }
}