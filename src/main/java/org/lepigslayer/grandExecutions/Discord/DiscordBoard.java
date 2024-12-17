package org.lepigslayer.grandExecutions.Discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static org.lepigslayer.grandExecutions.Discord.DiscordMain.jda;
import static org.lepigslayer.grandExecutions.Discord.DiscordMain.settings;

public class DiscordBoard {
    private static Message message;

    public static void sendMessage(OfflinePlayer p){
        TextChannel channel = jda.getGuildById(settings.guildId).getTextChannelById(settings.postChannel);

        channel.sendMessage("# "+DiscordEmojiLoader.getEmoji(p).getFormatted()+" "+p.getName()+" has met their fate! @everyone").queue();
    }

    public static void postBoard() {
        Guild guild = jda.getGuildById(settings.guildId);
        TextChannel channel = guild.getTextChannelById(settings.boardChannel);

        for(Message message : channel.getIterableHistory()) {
            message.delete().queue();
        }

        channel.sendMessage(getFullStatus()).queue((m)->message=m);
    }

    public static void editBoard() {
        if(message==null){
            postBoard();
            return;
        }

        message.editMessage(getFullStatus()).queue();
    }

    private static String getFullStatus() {

        StringBuilder builder = new StringBuilder();

        List<OfflinePlayer> livingPlayers = settings.players.stream().filter(settings::isAlive).toList();
        List<OfflinePlayer> deadPlayers = settings.players.stream().filter(settings::isDead).toList();

        builder.append("# Living Players\n");
        for(OfflinePlayer p:livingPlayers){
            builder.append("## ")
                    .append(DiscordEmojiLoader.getEmoji(p).getFormatted())
                    .append(" `")
                    .append(p.getName())
                    .append("`")
                    .append("\n");
        }
        if(!deadPlayers.isEmpty()){
            builder.append("\n# Dead Players\n");
            for(OfflinePlayer p:deadPlayers){
                builder.append("## ")
                        .append(DiscordEmojiLoader.getEmoji(p).getFormatted())
                        .append(" `")
                        .append(p.getName())
                        .append("` ")
                        .append(getStatusText(p))
                        .append("\n");
            }
        }

        if(builder.toString().trim().isEmpty()) return "# NUFFIN!!!";

        return builder.toString().trim();
    }

    private static String getStatusText(OfflinePlayer offlinePlayer){
        if(settings.deathText.containsKey(offlinePlayer.getUniqueId())) return settings.deathText.get(offlinePlayer.getUniqueId());
        return " ALIVE";
    }
}
