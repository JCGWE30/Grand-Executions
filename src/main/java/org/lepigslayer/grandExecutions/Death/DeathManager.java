package org.lepigslayer.grandExecutions.Death;

import com.comphenix.protocol.wrappers.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.lepigslayer.grandExecutions.GrandExecutions;

import java.util.*;

public class DeathManager {
    private static List<UUID> dead;
    private static List<UUID> bodyUUIDs;

    public static void init(){
        dead = GrandExecutions.getSaver().loadList();
    }

    public static List<Player> getDeadPlayers(){
        List<Player> online = new ArrayList<>();
        for(UUID uuid : dead){
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            if(player.isOnline()){
                online.add(player.getPlayer());
            }
        }
        return online;
    }

    public static void kill(Player p, Location location, EntityDamageEvent.DamageCause cause,String killer){
        dead.add(p.getUniqueId());
        GrandExecutions.getSaver().saveList(dead);
        spawnDeadBody(p,location, cause,killer);
    }

    public static boolean isAlive(UUID id){
        return !dead.contains(id);
    }

    public static boolean  shouldSee(WrappedGameProfile profile, Player p){
        if(DeadBody.isCorpse(profile))
            return false;
        if(!isAlive(p.getUniqueId()))
            return true;
        return isAlive(profile.getUUID());
    }

    private static void spawnDeadBody(Player owner, Location location, EntityDamageEvent.DamageCause cause,String killer){
        new DeadBody(owner,location,cause,killer);
    }

}
