package org.lepigslayer.grandExecutions.Discord;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.lepigslayer.grandExecutions.GrandExecutions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class DiscordGateway {
    private static Plugin plugin;

    public static void start(Plugin plg) {
        plugin = plg;
        DiscordSettings settings = new DiscordSettings(plugin.getServer().getOfflinePlayers());
        DiscordMain.start(settings);
        try {
            DiscordMain.jda.awaitReady();
            DiscordEmojiLoader.load(settings);
            DiscordBoard.postBoard();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveSettings() {
        GrandExecutions.getSaver().saveDiscordSettings(DiscordMain.settings.getJson());
    }

    public static void reloadBoard() {
        new Thread(() -> {
            saveSettings();
            DiscordSettings settings = new DiscordSettings(plugin.getServer().getOfflinePlayers());
            DiscordMain.settings = settings;
            DiscordBoard.editBoard();
        }).run();
    }

    public static void kill(UUID uid) {
        Thread t = new Thread(() -> {
            OfflinePlayer op = Bukkit.getOfflinePlayer(uid);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy 'at' hh:mm a");
            DiscordMain.settings.deathText.put(uid, "Died on " + format.format(new Date()));
            DiscordBoard.editBoard();
            DiscordBoard.sendMessage(op);
            DiscordMain.settings.mostRecentDeath = op.getName();
        });
        t.run();
    }
}

