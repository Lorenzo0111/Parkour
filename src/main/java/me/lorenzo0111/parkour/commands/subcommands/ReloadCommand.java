package me.lorenzo0111.parkour.commands.subcommands;

import me.lorenzo0111.parkour.ParkourPlugin;
import me.lorenzo0111.parkour.commands.SubCommand;
import me.lorenzo0111.parkour.data.flat.MessagesFile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class ReloadCommand extends SubCommand {

    @Override
    public void perform(Player sender, String[] args) {
        ParkourPlugin.getInstance().reload();
        sender.sendMessage(MessagesFile.getInstance().getMessage("commands.reload"));
    }

    @Override
    public @Nullable String permission() {
        return "parkour.admin.reload";
    }

}
