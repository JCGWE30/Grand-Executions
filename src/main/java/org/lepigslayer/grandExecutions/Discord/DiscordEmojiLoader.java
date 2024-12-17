package org.lepigslayer.grandExecutions.Discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import org.bukkit.OfflinePlayer;
import org.lepigslayer.grandExecutions.Utils.HeadUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.lepigslayer.grandExecutions.Discord.DiscordMain.*;
import static org.lepigslayer.grandExecutions.Discord.DiscordMain.settings;

class DiscordEmojiLoader {
    private static List<RichCustomEmoji> emojis;

    public static void load(DiscordSettings settings) {
        emojis = jda.getGuildById(settings.guildId).getEmojis();
        List<String> emojiNames = emojis.stream().map(Emoji::getName).toList();

        for(OfflinePlayer op: settings.players){
            loadEmoji(op,emojiNames);
        }
    }

    public static RichCustomEmoji getEmoji(OfflinePlayer player) {
        List<RichCustomEmoji> emojis = jda.getGuildById(settings.guildId).getEmojisByName(player.getName(),false);
        if(emojis.isEmpty()){
            System.out.println("CANT GET EMOJI FOR "+player.getName());
            return jda.getGuildById(settings.guildId).getEmojisByName("LePigSlayer",false).getFirst();
        }
        return jda.getGuildById(settings.guildId).getEmojisByName(player.getName(),false).getFirst();
    }

    private static void loadEmoji(OfflinePlayer op,List<String> emojiNames) {
        if(emojiNames.contains(op.getName())) return;

        URL textureUrl = HeadUtils.fetchTexture(op.getUniqueId());

        Icon icon = Icon.from(loadImageBytes(textureUrl));

        jda.getGuildById(settings.guildId).createEmoji(op.getName(),icon).queue();
        System.out.println("Loading emoji "+op.getName()+" from "+textureUrl);
    }

    private static byte[] loadImageBytes(URL url){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try(InputStream in = connection.getInputStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream()){
                byte[] buffer = new byte[8192];
                int len;

                while((len = in.read(buffer)) != -1){
                    out.write(buffer, 0, len);
                }

                return cropBytes(out.toByteArray());
            }
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    private static byte[] cropBytes(byte[] bytes){
        try(ByteArrayInputStream in = new ByteArrayInputStream(bytes)){
            BufferedImage image = ImageIO.read(in);

            BufferedImage croppedImage = image.getSubimage(8,8,8,8);

            BufferedImage scaledImage = new BufferedImage(128,128,BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = scaledImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            g.drawImage(croppedImage,0,0,128,128,null);
            g.dispose();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(scaledImage, "png", out);
            return out.toByteArray();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
