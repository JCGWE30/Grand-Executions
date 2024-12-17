package org.lepigslayer.grandExecutions.Discord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.lepigslayer.grandExecutions.GrandExecutions;

import java.util.*;
import java.util.stream.Collectors;

class DiscordSettings {
    public String token;
    public String boardChannel;
    public String postChannel;
    public String guildId;
    public String mostRecentDeath;
    public List<OfflinePlayer> players;
    public Map<UUID, String> deathText = new HashMap<>();

    public DiscordSettings(OfflinePlayer[] players) {
        this.players = Arrays.asList(players);
        System.out.println(this.players.size());

        Gson gson = new Gson();
        String stringJson = GrandExecutions.getSaver().getDiscordSettings();
        JsonObject json = gson.fromJson(stringJson, JsonObject.class);

        token = json.get("token").getAsString();
        boardChannel = json.get("boardChannel").getAsString();
        postChannel = json.get("postChannel").getAsString();
        guildId = json.get("guild").getAsString();

        if(json.has("mostRecentDeath"))
            mostRecentDeath = json.get("mostRecentDeath").getAsString();

        if(!json.has("deathText")) return;

        deathText = json.get("deathText").getAsJsonObject().asMap().entrySet().stream()
                .collect(Collectors.toMap(e -> UUID.fromString(e.getKey()), e -> e.getValue().getAsString(), (v1, v2) -> v1, HashMap<UUID, String>::new));
    }

    public Guild getGuild(){
        return DiscordMain.jda.getGuildById(guildId);
    }

    public String getRecentDeath(){
        if(mostRecentDeath == null) return "No one is dead!";

        OfflinePlayer op = Bukkit.getOfflinePlayer(mostRecentDeath);
        return DiscordEmojiLoader.getEmoji(op).getFormatted()+" "+op.getName();
    }

    public boolean isAlive(OfflinePlayer op){
        return !deathText.containsKey(op.getUniqueId());
    }

    public boolean isDead(OfflinePlayer op){
        return !isAlive(op);
    }

    public String getJson() {
        JsonObject json = new JsonObject();
        json.addProperty("token", token);
        json.addProperty("boardChannel", boardChannel);
        json.addProperty("postChannel", postChannel);
        json.addProperty("guild", guildId);
        if(mostRecentDeath != null)
            json.addProperty("mostRecentDeath", mostRecentDeath);

        JsonObject array = new JsonObject();
        deathText.forEach((u, s) -> array.addProperty(u.toString(), s));

        json.add("deathText", array);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }
}
