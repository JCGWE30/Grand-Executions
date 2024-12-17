package org.lepigslayer.grandExecutions;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.lepigslayer.grandExecutions.Death.DeadBody;
import org.lepigslayer.grandExecutions.Death.DeathManager;
import org.lepigslayer.grandExecutions.Discord.DiscordGateway;
import org.lepigslayer.grandExecutions.Statistics.StatisticGenerator;
import org.lepigslayer.grandExecutions.Utils.FileSaver;

import java.util.*;

public final class GrandExecutions extends JavaPlugin {

    private static GrandExecutions instance;
    private FileSaver fileSaver;

    @Override
    public void onEnable() {
        instance = this;
        fileSaver = new FileSaver(this);
        setupIntercepters();
        DeathManager.init();
        DeadBody.loadCorpses();
        getServer().getPluginManager().registerEvents(new ExecutionEvents(this), this);
        StatisticGenerator.generateStatistics();
        DiscordGateway.start(this);
    }

    @Override
    public void onDisable() {
        DiscordGateway.saveSettings();
    }

    public static FileSaver getSaver() {
        return instance.fileSaver;
    }

    private static void setupIntercepters() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.MONITOR, PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent e) {
                return; // Fix this shit later
//                List<PlayerInfoData> uids = e.getPacket().getPlayerInfoDataLists().read(1).stream()
//                        .filter(d->!DeathManager.shouldSee(d.getProfile(),e.getPlayer())).toList();
//
//                if(uids.isEmpty()) return;
//
//                PacketContainer removePacket = new PacketContainer(PacketType.Play.Server.PLAYER_INFO_REMOVE);
//                removePacket.getPlayerInfoDataLists().write(0, uids);
//
//                ProtocolLibrary.getProtocolManager().sendServerPacket(e.getPlayer(), removePacket);
            }
        });

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.HIGHEST, PacketType.Play.Server.CHAT) {
            @Override
            public void onPacketSending(PacketEvent e) {
                UUID sender = e.getPacket().getUUIDs().read(0);
                if(!DeathManager.isAlive(e.getPlayer().getUniqueId()))
                    return;
                if (!DeathManager.isAlive(sender))
                    e.setCancelled(true);
            }
        });
    }
}
