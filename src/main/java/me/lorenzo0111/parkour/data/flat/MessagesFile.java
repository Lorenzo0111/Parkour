package me.lorenzo0111.parkour.data.flat;

import org.bukkit.ChatColor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesFile extends AbstractFile {
    private static MessagesFile instance;
    private Map<String,Object> messages;

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
        return (String) messages.computeIfAbsent(path, key -> ChatColor.translateAlternateColorCodes('&',  this.getConfig().getString(key, "&cCould not find a message with the key: " + key)));
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
