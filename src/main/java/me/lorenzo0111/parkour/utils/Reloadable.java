package me.lorenzo0111.parkour.utils;

public interface Reloadable {
    void reload();
    default void reloadAndSave() {
        reload();
    }
}
