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

package me.lorenzo0111.parkour.data.flat;

import org.bukkit.ChatColor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesFile extends AbstractFile {
    private static MessagesFile instance;
    private Map<String, Object> messages;

    public MessagesFile(File dataFolder) {
        super(new File(dataFolder, "messages.yml"), true);

        instance = this;
    }

    @Override
    public void reload() {
        super.reload();

        messages = new HashMap<>();
    }

    public String getMessage(String path) {
        return (String) messages.computeIfAbsent(path, key -> ChatColor.translateAlternateColorCodes('&', this.getConfig().getString(key, "&cCould not find a message with the key: " + key)));
    }

    @SuppressWarnings("unchecked")
    public List<String> getMessages(String path) {
        return (List<String>) messages.computeIfAbsent(path, key -> this.getConfig().getStringList(key)
                .stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .toList());
    }

    public static MessagesFile getInstance() {
        return instance;
    }
}
