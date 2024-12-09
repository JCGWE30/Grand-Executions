package org.lepigslayer.grandExecutions;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticGenerator {
    public static final List<StatisticPair> statistics = new ArrayList<>();
    private static final Map<String, String> statisticNames = new HashMap<>() {{
        put("DAMAGE_DEALT", "Damage Dealt");
        put("DAMAGE_TAKEN", "Damage Taken");
        put("DEATHS", "Deaths");
        put("MOB_KILLS", "Mob Kills");
        put("PLAYER_KILLS", "Player Kills");
        put("FISH_CAUGHT", "Fish Caught");
        put("ANIMALS_BRED", "Animals Bred");
        put("LEAVE_GAME", "Games Left");
        put("JUMP", "Jumps");
        put("DROP_COUNT", "Items Dropped");
        put("DROP", "OBJECT Dropped");
        put("PICKUP", "OBJECT Picked Up");
        put("PLAY_ONE_MINUTE", "Minutes Played");
        put("TOTAL_WORLD_TIME", "Total World Time");
        put("WALK_ONE_CM", "Centimeters Walked");
        put("WALK_ON_WATER_ONE_CM", "Centimeters Walked on Water");
        put("FALL_ONE_CM", "Centimeters Fallen");
        put("SNEAK_TIME", "Time Sneaking");
        put("CLIMB_ONE_CM", "Centimeters Climbed");
        put("FLY_ONE_CM", "Centimeters Flown");
        put("WALK_UNDER_WATER_ONE_CM", "Centimeters Walked Underwater");
        put("MINECART_ONE_CM", "Centimeters Traveled in Minecart");
        put("BOAT_ONE_CM", "Centimeters Traveled in Boat");
        put("PIG_ONE_CM", "Centimeters Traveled on Pig");
        put("HORSE_ONE_CM", "Centimeters Traveled on Horse");
        put("SPRINT_ONE_CM", "Centimeters Sprinting");
        put("CROUCH_ONE_CM", "Centimeters Crouched");
        put("AVIATE_ONE_CM", "Centimeters Flown (Aviation)");
        put("MINE_BLOCK", "OBJECT Mined");
        put("USE_ITEM", "OBJECT Used");
        put("BREAK_ITEM", "OBJECT Broken");
        put("CRAFT_ITEM", "OBJECT Crafted");
        put("KILL_ENTITY", "OBJECT Killed");
        put("ENTITY_KILLED_BY", "Killed by OBJECT");
        put("TIME_SINCE_DEATH", "Time Since Death");
        put("TALKED_TO_VILLAGER", "Talked to Villager");
        put("TRADED_WITH_VILLAGER", "Traded with Villager");
        put("CAKE_SLICES_EATEN", "Cake Slices Eaten");
        put("CAULDRON_FILLED", "Cauldrons Filled");
        put("CAULDRON_USED", "Cauldrons Used");
        put("ARMOR_CLEANED", "Armor Cleaned");
        put("BANNER_CLEANED", "Banner Cleaned");
        put("BREWINGSTAND_INTERACTION", "Brewing Stands Interacted With");
        put("BEACON_INTERACTION", "Beacons Interacted With");
        put("DROPPER_INSPECTED", "Droppers Inspected");
        put("HOPPER_INSPECTED", "Hoppers Inspected");
        put("DISPENSER_INSPECTED", "Dispensers Inspected");
        put("NOTEBLOCK_PLAYED", "Note Blocks Played");
        put("NOTEBLOCK_TUNED", "Note Blocks Tuned");
        put("FLOWER_POTTED", "Flowers Potted");
        put("TRAPPED_CHEST_TRIGGERED", "Trapped Chests Triggered");
        put("ENDERCHEST_OPENED", "Ender Chests Opened");
        put("ITEM_ENCHANTED", "Items Enchanted");
        put("RECORD_PLAYED", "Records Played");
        put("FURNACE_INTERACTION", "Furnaces Interacted With");
        put("CRAFTING_TABLE_INTERACTION", "Crafting Tables Interacted With");
        put("CHEST_OPENED", "Chests Opened");
        put("SLEEP_IN_BED", "Slept in Beds");
        put("SHULKER_BOX_OPENED", "Shulker Boxes Opened");
        put("TIME_SINCE_REST", "Time Since Rest");
        put("SWIM_ONE_CM", "Centimeters Swum");
        put("DAMAGE_DEALT_ABSORBED", "Damage Absorbed");
        put("DAMAGE_DEALT_RESISTED", "Damage Resisted");
        put("DAMAGE_BLOCKED_BY_SHIELD", "Damage Blocked by Shields");
        put("DAMAGE_ABSORBED", "Damage Absorbed");
        put("DAMAGE_RESISTED", "Damage Resisted");
        put("CLEAN_SHULKER_BOX", "Shulker Boxes Cleaned");
        put("OPEN_BARREL", "Barrels Opened");
        put("INTERACT_WITH_BLAST_FURNACE", "Interacted With Blast Furnaces");
        put("INTERACT_WITH_SMOKER", "Interacted With Smokers");
        put("INTERACT_WITH_LECTERN", "Interacted With Lecterns");
        put("INTERACT_WITH_CAMPFIRE", "Interacted With Campfires");
        put("INTERACT_WITH_CARTOGRAPHY_TABLE", "Interacted With Cartography Tables");
        put("INTERACT_WITH_LOOM", "Interacted With Looms");
        put("INTERACT_WITH_STONECUTTER", "Interacted With Stonecutters");
        put("BELL_RING", "Bells Rung");
        put("RAID_TRIGGER", "Raids Triggered");
        put("RAID_WIN", "Raids Won");
        put("INTERACT_WITH_ANVIL", "Interacted With Anvils");
        put("INTERACT_WITH_GRINDSTONE", "Interacted With Grindstones");
        put("TARGET_HIT", "Targets Hit");
        put("INTERACT_WITH_SMITHING_TABLE", "Interacted With Smithing Tables");
        put("STRIDER_ONE_CM", "Centimeters Traveled on Strider");
    }};
    public static void generateStatistics(){
        System.out.println("Generating Statistics");
        for(Statistic stat:Statistic.values()){
            switch (stat.getType()){
                case ITEM:
                    for(Material mat:Material.values()){
                        if(!mat.isItem()) continue;
                        String name = mat.name().replace("_", " ").toLowerCase();
                        name = name.substring(0,1).toUpperCase() + name.substring(1);
                        statistics.add(new StatisticPair(stat, mat,statisticNames.get(stat.toString()).replace("OBJECT",name)));
                    }
                    break;
                case BLOCK:
                    for(Material mat:Material.values()){
                        if(!mat.isBlock()) continue;
                        String name = mat.name().replace("_", " ").toLowerCase();
                        name = name.substring(0,1).toUpperCase() + name.substring(1);
                        statistics.add(new StatisticPair(stat, mat,statisticNames.get(stat.toString()).replace("OBJECT",name)));
                    }
                    break;
                case ENTITY:
                    for(EntityType type:EntityType.values()){
                        String name = type.name().replace("_", " ").toLowerCase();
                        name = name.substring(0,1).toUpperCase() + name.substring(1);
                        statistics.add(new StatisticPair(stat, type,statisticNames.get(stat.toString()).replace("OBJECT",name)));
                    }
                case UNTYPED:
                    statistics.add(new StatisticPair(stat, null,statisticNames.get(stat.toString())));
                    break;
            }
        }
        System.out.println("Generated "+statistics.size()+" statistics");
    }
}
