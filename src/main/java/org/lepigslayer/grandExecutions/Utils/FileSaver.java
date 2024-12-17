package org.lepigslayer.grandExecutions.Utils;

import org.lepigslayer.grandExecutions.Death.DeadBody;
import org.lepigslayer.grandExecutions.GrandExecutions;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class FileSaver {
    private final File baseFolder;
    private final File corpsesFolder;

    public FileSaver(GrandExecutions plugin) {
        this.baseFolder = plugin.getDataFolder();
        this.corpsesFolder = new File(plugin.getDataFolder(), "corpses");
        if (!baseFolder.exists()) baseFolder.mkdir();
        if (!corpsesFolder.exists()) corpsesFolder.mkdir();
    }
    public List<DeadBody> loadCorpses() {
        List<DeadBody> corpses = new ArrayList<>();
        for (File f : corpsesFolder.listFiles()) {
            corpses.add(loadCorpse(f));
        }
        return corpses;
    }

    public void deleteCorpse(String name) {
        File f = new File(corpsesFolder, name + ".corpse");
        try {
            Files.delete(f.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveCorpse(DeadBody body, String name) {
        File file = new File(corpsesFolder, name + ".corpse");
        try {
            file.createNewFile();

            FileOutputStream fis = new FileOutputStream(file);
            fis.write(body.serialize());
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DeadBody loadCorpse(File f) {
        if (!f.exists()) throw new RuntimeException("Corpse does not exist");

        try {
            FileInputStream fis = new FileInputStream(f);
            return DeadBody.deseralize(fis.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveList(List<UUID> ids) {
        File list = new File(baseFolder, "deadPlayers.txt");
        create(list);

        StringBuilder builder = new StringBuilder();
        for (UUID id : ids) {
            builder.append(id + "\n");
        }

        try (FileWriter writer = new FileWriter(list)) {
            writer.write(builder.toString().trim());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDiscordSettings(){
        File token = new File(baseFolder,"discord.json");
        if(!token.exists()) throw new RuntimeException("Token file does not exist");
        StringBuilder sb = new StringBuilder();
        try(Scanner scanner = new Scanner(token)){
            while(scanner.hasNext())
                sb.append(scanner.nextLine())
                        .append("\n");
            return sb.toString().trim();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void saveDiscordSettings(String json){
        File token = new File(baseFolder,"discord.json");
        try(FileWriter writer = new FileWriter(token)){
            writer.write(json);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public List<UUID> loadList() {
        File list = new File(baseFolder, "deadPlayers.txt");
        if (!list.exists()) return new ArrayList<>();
        List<UUID> ids = new ArrayList<>();
        try (Scanner scanner = new Scanner(list)) {
            while (scanner.hasNext()) {
                ids.add(UUID.fromString(scanner.next().trim()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ids;
    }

    private void create(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
