package me.lorenzo0111.parkour.commands.subcommands;

import me.lorenzo0111.parkour.commands.SubCommand;
import me.lorenzo0111.parkour.data.cache.Challenge;
import me.lorenzo0111.parkour.data.cache.ChallengeHandler;
import me.lorenzo0111.parkour.data.flat.MessagesFile;
import me.lorenzo0111.parkour.data.flat.Parkour;
import me.lorenzo0111.parkour.data.flat.ParkourFile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeleportCommand extends SubCommand {

    @Override
    public void perform(Player sender, String[] args) {
        Parkour parkour = fromArgs(sender,"teleport {name} [checkpoint]",args);
        if (parkour == null) {
            return;
        }

        if (args.length == 2) {
            try {
                int position = Integer.parseInt(args[1]);
                if (position < 1 || position > parkour.getCheckpoints().size()) {
                    sender.sendMessage(MessagesFile.getInstance().getMessage("errors.not-checkpoint"));
                    return;
                }

                sender.teleport(parkour.getCheckpoints().get(position - 1).location());
                sender.sendMessage(MessagesFile.getInstance().getMessage("commands.teleport-checkpoint").replace("{name}", parkour.getName()).replace("{position}", String.valueOf(position)));
            } catch (NumberFormatException e) {
                sender.sendMessage(MessagesFile.getInstance().getMessage("errors.not-checkpoint"));
                return;
            }
            return;
        }

        Challenge challenge = new Challenge(sender,parkour);
        ChallengeHandler.getInstance().start(sender,challenge);

        sender.teleport(parkour.getStart());
        sender.sendMessage(MessagesFile.getInstance().getMessage("commands.teleport").replace("{name}", parkour.getName()));
    }

    @Override
    public @Nullable String permission() {
        return "parkour.admin.teleport";
    }

    @Override
    public List<String> complete() {
        return new ArrayList<>(ParkourFile.getInstance().getParkours().keySet());
    }
}
