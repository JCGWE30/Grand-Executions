package org.lepigslayer.grandExecutions;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.*;

public class EulogyManager {
    private static final HashMap<UUID,EulogyManager> managers = new HashMap<>();

    private UUID owner;

    public EulogyManager(OfflinePlayer owner) {
        this.owner=owner.getUniqueId();
    }

    public static EulogyManager addEulogy(OfflinePlayer player) {
        return managers.computeIfAbsent(player.getUniqueId(),u->new EulogyManager(player));
    }

    private ItemStack getEulogy(EntityDamageEvent.DamageCause cause, String killer){
        OfflinePlayer op = Bukkit.getOfflinePlayer(owner);

        String message = String.format(DeathMessages.deathMessages.get(cause.toString())[new Random().nextInt(3)],op.getName());

        if(DeathMessages.attackerMessages.contains(cause)) {
            message = message.replace("KILLER",killer);
        }

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK,1);
        BookMeta meta = ((BookMeta) book.getItemMeta());


        meta.setAuthor("§4Death");
        meta.setTitle("§aThe life of "+op.getName());
        List<String> pages = new ArrayList<>();

        StringBuilder startingPage = new StringBuilder();
        startingPage.append("§6").append(String.format(DeathMessages.summaryMessages[new Random().nextInt(DeathMessages.summaryMessages.length)], op.getName()));
        startingPage.append("\n\n");
        startingPage.append("§c").append(String.format(message, op.getName()));
        pages.add(startingPage.toString());

        StringBuilder statisticsPage = new StringBuilder();
        StringBuilder statisticsPage1 = new StringBuilder();
        StringBuilder statisticsPage2 = new StringBuilder();
        statisticsPage.append("§3Here are some unique statistics for "+op.getName());
        statisticsPage.append("\n\n");
        List<StatisticPair> stats = getRandomStatistics(op,10);

        for(int i = 0;i<3;i++) {
            StatisticPair stat = stats.get(i);
            statisticsPage.append("§1").append(stat.getName()).append(": ").append(stat.getValue(op));
            statisticsPage.append("\n\n");
        }
        for(int i = 3;i<7;i++) {
            StatisticPair stat = stats.get(i);
            statisticsPage1.append("§1").append(stat.getName()).append(": ").append(stat.getValue(op));
            statisticsPage1.append("\n\n");
        }
        for(int i = 7;i<10;i++) {
            StatisticPair stat = stats.get(i);
            statisticsPage2.append("§1").append(stat.getName()).append(": ").append(stat.getValue(op));
            statisticsPage2.append("\n\n");
        }
        pages.add(statisticsPage.toString());
        pages.add(statisticsPage1.toString());
        pages.add(statisticsPage2.toString());

        meta.setPages(pages);
        book.setItemMeta(meta);

        return book;
    }

    public static ItemStack getEulogy(EntityDamageEvent.DamageCause cause,OfflinePlayer player,String killer){
        return addEulogy(player).getEulogy(cause,killer);
    }

    public static List<StatisticPair> getRandomStatistics(OfflinePlayer op, int count){
        List<StatisticPair> possible = new ArrayList<>(StatisticGenerator.statistics.stream()
                .filter(s -> !Objects.equals(s.getValue(op), "0"))
                .toList());

        Random random = new Random();
        Collections.shuffle(possible,random);

        List<StatisticPair> actual = new ArrayList<>();
        for(int i = 0;i<count;i++){
            actual.add(possible.remove(random.nextInt(possible.size())));
        }
        return actual;
    }
}
