package me.lorenzo0111.parkour.commands.subcommands;

import me.lorenzo0111.parkour.commands.SubCommand;
import me.lorenzo0111.parkour.data.flat.MessagesFile;
import me.lorenzo0111.parkour.data.flat.Parkour;
import me.lorenzo0111.parkour.data.flat.ParkourFile;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class CreateCommand extends SubCommand {

    @Override
    public void perform(Player sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(MessagesFile.getInstance().getMessage("errors.usage").replace("{usage}", "/parkour create {name}"));
            return;
        }

        String name = args[0];
        Location start = sender.getLocation();

        if (ParkourFile.getInstance().getParkour(name) != null) {
            sender.sendMessage(MessagesFile.getInstance().getMessage("errors.exists").replace("{name}", name));
            return;
        }

        Parkour parkour = new Parkour(name,start);
        ParkourFile.getInstance().add(parkour);

        sender.sendMessage(MessagesFile.getInstance().getMessage("commands.create").replace("{name}", name));
    }

    @Override
    public @Nullable String permission() {
        return "parkour.admin.create";
    }

}
