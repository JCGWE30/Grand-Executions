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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class GrandExecutions extends JavaPlugin implements Listener {
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

    public static boolean enabled = false;

    @Override
    public void onEnable() {
        getCommand("toggledeath").setExecutor(new ToggleCommand());
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void death(EntityDamageEvent e){
        if(!enabled) return;
        if(!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();

        if(player.getHealth()-e.getFinalDamage()>0) return;

        if(totemCheck(e.getCause(),player)) return;

        e.setDamage(0);

        ItemStack[] items = player.getInventory().getContents();

        player.getInventory().clear();

        for(ItemStack item : items){
            if(item==null) continue;
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }

        player.getWorld().strikeLightningEffect(player.getLocation());
        player.setGameMode(GameMode.SPECTATOR);

        Location skullSpot = castToGround(player.getLocation());
        Block skullBlock = skullSpot.getBlock();
        skullBlock.setType(Material.PLAYER_HEAD);
        applySkullData(skullBlock,player);
        skullBlock.getRelative(BlockFace.DOWN).setType(Material.COARSE_DIRT);

        for(Player p: Bukkit.getOnlinePlayers()){
            p.sendTitle(String.format("§c§l%s",player.getName()),"§7Has met their fate!",10,70,20);
        }
    }

    public static void applySkullData(Block block, Player player){
        Skull skull = (Skull) block.getState();
        skull.setOwnerProfile(player.getPlayerProfile());
        skull.update();
        Rotatable rotatable = (Rotatable) block.getBlockData();
        BlockFace face = faces[new Random().nextInt(faces.length)];
        rotatable.setRotation(face);
        block.setBlockData(rotatable);
    }

    public static Location castToGround(Location location){
        Vector vector = new Vector(location.getBlockX(),Math.max(location.getBlockY(),location.getWorld().getMinHeight()+1),location.getBlockZ());
        Location currentLocation = vector.toLocation(location.getWorld());
        while(List.of(whitelist).contains(currentLocation.getBlock().getType())){
            currentLocation = currentLocation.getBlock().getRelative(BlockFace.DOWN).getLocation();
        }
        return currentLocation.getBlock().getRelative(BlockFace.UP).getLocation();
    }

    public static boolean totemCheck(EntityDamageEvent.DamageCause cause, Player player){
        ItemStack hand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        if(hand.getType()!=Material.TOTEM_OF_UNDYING&&offHand.getType()!=Material.TOTEM_OF_UNDYING) return false;

        return cause != EntityDamageEvent.DamageCause.VOID;
    }
}
