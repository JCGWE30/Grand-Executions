package org.lepigslayer.grandExecutions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.UUID;

public class HeadUtils {
    private static final String SKIN_SERVER = "https://sessionserver.mojang.com/session/minecraft/profile/%s";

    public static URL fetchTexture(UUID id){
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(SKIN_SERVER,id.toString())))
                .GET()
                .build();

        try{
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

            return fetchTextureUrl(json.getAsJsonArray("properties").get(0)
                    .getAsJsonObject()
                    .get("value").getAsString());
        }catch (IOException | InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    private static URL fetchTextureUrl(String encoded){
        String string = new String(Base64.getDecoder().decode(encoded));
        try {
            return URI.create(JsonParser.parseString(string)
                    .getAsJsonObject()
                    .getAsJsonObject("textures")
                    .getAsJsonObject("SKIN")
                    .get("url").getAsString()).toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
