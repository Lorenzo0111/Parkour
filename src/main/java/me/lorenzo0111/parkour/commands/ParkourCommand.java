package me.lorenzo0111.parkour.commands;

import me.lorenzo0111.parkour.commands.subcommands.*;
import me.lorenzo0111.parkour.data.flat.MessagesFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkourCommand implements TabExecutor {
    private final Map<String,SubCommand> commands = new HashMap<>();

    public ParkourCommand() {
        commands.put("reload", new ReloadCommand());
        commands.put("create", new CreateCommand());
        commands.put("checkpoint", new CheckpointCommand());
        commands.put("end", new EndCommand());
        commands.put("delete", new DeleteCommand());
        commands.put("teleport", new TeleportCommand());
        commands.put("top",new TopCommand());
        commands.put("info",new InfoCommand());
        commands.put("stats",new StatsCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessagesFile.getInstance().getMessage("errors.player"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(MessagesFile.getInstance().getMessage("errors.usage").replace("{usage}", "/parkour <create|checkpoint|end|delete|top|info|stats>"));
            return true;
        }

        if (!commands.containsKey(args[0])) {
            sender.sendMessage(MessagesFile.getInstance().getMessage("errors.usage").replace("{usage}", "/parkour <create|checkpoint|end|delete|top|info|stats>"));
            return true;
        }

        SubCommand subCommand = commands.get(args[0]);
        if (subCommand.permission() != null && !sender.hasPermission(subCommand.permission())) {
            sender.sendMessage(MessagesFile.getInstance().getMessage("errors.no-permission"));
            return true;
        }

        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);
        subCommand.perform((Player) sender, newArgs);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();

        if (args.length != 1) {

            SubCommand subCommand = commands.get(args[0]);
            if (subCommand != null) {
                return subCommand.complete();
            }

           return tab;
        }

        for (String s : commands.keySet()) {
            if (s.startsWith(args[0])) {
                tab.add(s);
            }
        }

        return tab;
    }

}
