/*
 * This file is part of Parkour, licensed under the MIT License.
 *
 *  Copyright (c) Lorenzo0111
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.lorenzo0111.parkour.commands.subcommands;

import me.lorenzo0111.parkour.commands.SubCommand;
import me.lorenzo0111.parkour.data.flat.Checkpoint;
import me.lorenzo0111.parkour.data.flat.MessagesFile;
import me.lorenzo0111.parkour.data.flat.Parkour;
import me.lorenzo0111.parkour.data.flat.ParkourFile;
import me.lorenzo0111.parkour.hologram.HologramManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DeleteCommand extends SubCommand {

    @Override
    public void perform(Player sender, String[] args) {
        Parkour parkour = fromArgs(sender,"delete {name} [checkpoint]",args);
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

                HologramManager.getInstance().remove(parkour.getCheckpoints().remove(position - 1).hologram());

                for (int i = 0; i < parkour.getCheckpoints().size(); i++) {
                    parkour.getCheckpoints().get(i).hologram().setText(MessagesFile.getInstance().getMessage("hologram.checkpoint")
                            .replace("{name}", parkour.getName())
                            .replace("{count}", String.valueOf(i+1)));
                }

                sender.sendMessage(MessagesFile.getInstance().getMessage("commands.delete-checkpoint").replace("{name}", parkour.getName()).replace("{position}", String.valueOf(position)));
            } catch (NumberFormatException e) {
                sender.sendMessage(MessagesFile.getInstance().getMessage("errors.not-checkpoint"));
                return;
            }

            ParkourFile.getInstance().add(parkour);
            return;
        }

        ParkourFile.getInstance().remove(parkour.getName());
        sender.sendMessage(MessagesFile.getInstance().getMessage("commands.delete").replace("{name}", parkour.getName()));
    }

    @Override
    public @Nullable String permission() {
        return "parkour.admin.delete";
    }

    @Override
    public List<String> complete() {
        return new ArrayList<>(ParkourFile.getInstance().getParkours().keySet());
    }

}
