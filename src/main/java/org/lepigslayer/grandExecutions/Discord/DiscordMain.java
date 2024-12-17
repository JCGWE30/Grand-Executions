package org.lepigslayer.grandExecutions.Discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.lepigslayer.grandExecutions.GrandExecutions;

import java.util.EventListener;

class DiscordMain {
    static DiscordSettings settings;
    static JDA jda;

    static void start(DiscordSettings set) {
        settings=set;
        jda = JDABuilder.createDefault(settings.token)
                .enableCache(CacheFlag.EMOJI)
                .build();
    }

    static void updateSettings(DiscordSettings set){
        settings=set;
    }
}
