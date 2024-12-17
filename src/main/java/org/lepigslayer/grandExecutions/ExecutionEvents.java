package org.lepigslayer.grandExecutions;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.lepigslayer.grandExecutions.Death.DeadBody;
import org.lepigslayer.grandExecutions.Death.DeathManager;
import org.lepigslayer.grandExecutions.Discord.DiscordGateway;

import java.util.List;
import java.util.Random;

public class ExecutionEvents implements Listener {
    private static final Material[] whitelist = {
            Material.SHORT_GRASS,
            Material.TALL_GRASS,
            Material.AIR
    };
    private static final BlockFace[] faces = {
            BlockFace.NORTH,
            BlockFace.NORTH_EAST,
            BlockFace.EAST,
            BlockFace.SOUTH_EAST,
            BlockFace.SOUTH,
            BlockFace.SOUTH_WEST,
            BlockFace.WEST,
            BlockFace.NORTH_WEST,
    };

    private Plugin plugin;

    public ExecutionEvents(Plugin plguin){
        plugin = plguin;
    }

    @EventHandler
    public void hideJoin(PlayerJoinEvent e) {
        if(!DeathManager.isAlive(e.getPlayer().getUniqueId())){
            e.setJoinMessage(null);
            for(Player p:DeathManager.getDeadPlayers()){
                p.sendMessage(String.format("§e%s joined the game",e.getPlayer().getName()));
            }
        }
    }
    @EventHandler
    public void hideLeave(PlayerQuitEvent e) {
        if(!DeathManager.isAlive(e.getPlayer().getUniqueId())){
            e.setQuitMessage(null);
            for(Player p:DeathManager.getDeadPlayers()){
                p.sendMessage(String.format("§e%s left the game",e.getPlayer().getName()));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void death(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();

        if (player.getHealth() - e.getFinalDamage() > 0) return;

        if (totemCheck(e.getCause(), player)) return;

        e.setDamage(0);

        Location deathSpot = castToGround(player.getLocation());

        player.getWorld().strikeLightningEffect(player.getLocation());
        player.setGameMode(GameMode.SPECTATOR);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(String.format("§c§l%s", player.getName()), "§7Has met their fate!", 10, 70, 20);
        }

        String killer = "";
        if(e.getDamageSource().getCausingEntity()!=null) killer = e.getDamageSource().getCausingEntity().getName();
        DeathManager.kill(player,deathSpot,e.getCause(),killer);
        DiscordGateway.kill(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void joinSetup(PlayerJoinEvent e) {
        DeadBody.syncCorpses(e.getPlayer());
        DiscordGateway.reloadBoard();
        setPermission(e.getPlayer(), "voicechat.groups", false);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void digCorpse(BlockBreakEvent e) {
        e.setCancelled(DeadBody.checkHits(e.getBlock().getLocation()));
    }

    @EventHandler
    public void dimensionChange(PlayerChangedWorldEvent e){
        DeadBody.syncCorpses(e.getPlayer());
    }

    private boolean totemCheck(EntityDamageEvent.DamageCause cause, Player player) {
        ItemStack hand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        if (hand.getType() != Material.TOTEM_OF_UNDYING && offHand.getType() != Material.TOTEM_OF_UNDYING) return false;

        return cause != EntityDamageEvent.DamageCause.VOID;
    }

    private void setPermission(Player p, String perm, boolean state) {
        PermissionAttachment attachment = p.addAttachment(plugin);
        attachment.setPermission(perm, state);
    }

    public static void applySkullData(Block block, Player player) {
        Skull skull = (Skull) block.getState();
        skull.setOwnerProfile(player.getPlayerProfile());
        skull.update();
        Rotatable rotatable = (Rotatable) block.getBlockData();
        BlockFace face = faces[new Random().nextInt(faces.length)];
        rotatable.setRotation(face);
        block.setBlockData(rotatable);
    }



    public static Location castToGround(Location location) {
        Vector vector = new Vector(location.getBlockX(), Math.max(location.getBlockY(), location.getWorld().getMinHeight() + 1), location.getBlockZ());
        Location currentLocation = vector.toLocation(location.getWorld());
        while (List.of(whitelist).contains(currentLocation.getBlock().getType())) {
            currentLocation = currentLocation.getBlock().getRelative(BlockFace.DOWN).getLocation();
        }
        return currentLocation.getBlock().getRelative(BlockFace.UP).getLocation().add(0.5,0,0.5);
    }
}
