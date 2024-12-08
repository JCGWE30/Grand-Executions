package org.lepigslayer.grandExecutions;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import org.bukkit.plugin.java.JavaPlugin;

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
        getCommand("corpse").setExecutor(new CorpseTestCommand());
        StatisticGenerator.generateStatistics();
    }

    public static FileSaver getSaver() {
        return instance.fileSaver;
    }

    private static void setupIntercepters() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.HIGHEST, PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent e) {
                List<PlayerInfoData> data = e.getPacket().getPlayerInfoDataLists().read(1)
                        .stream()
                        .map(d-> new PlayerInfoData(
                                d.getProfileId(),
                                d.getLatency(),
                                DeathManager.shouldSee(d.getProfile(),e.getPlayer()),
                                d.getGameMode(),
                                d.getProfile(),
                                d.getDisplayName(),
                                d.getRemoteChatSessionData()
                        ))
                        .toList();
                e.getPacket().getPlayerInfoDataLists().write(1, data);
                Set<EnumWrappers.PlayerInfoAction> actions = e.getPacket().getPlayerInfoActions().read(0);
                actions.add(EnumWrappers.PlayerInfoAction.UPDATE_LISTED);
                e.getPacket().getPlayerInfoActions().write(0, actions);
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
