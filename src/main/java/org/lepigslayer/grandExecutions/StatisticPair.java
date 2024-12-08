package org.lepigslayer.grandExecutions;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class StatisticPair {
    private Statistic statistic;
    private Material material;
    private EntityType entityType;
    private String name;
    private boolean isSub = false;

    public StatisticPair(Statistic statistic, Object key,String name) {
        this.statistic = statistic;
        this.name = name;
        if(key instanceof Material) {
            material = (Material) key;
            isSub = true;
        }
        if(key instanceof EntityType) {
            entityType = (EntityType) key;
            isSub = true;
        }
    }

    public String getValue(OfflinePlayer op){
        try {
            if (!isSub) {
                return op.getStatistic(statistic)+"";
            }
            if (material != null)
                return op.getStatistic(statistic, material)+"";
            return op.getStatistic(statistic, entityType)+"";
        }catch(IllegalArgumentException e){
            return 0+"";
        }
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(statistic);
        if(material!=null)
            str.append(",").append(material);
        if(entityType!=null)
            str.append(",").append(entityType);
        return str.toString();
    }
}
