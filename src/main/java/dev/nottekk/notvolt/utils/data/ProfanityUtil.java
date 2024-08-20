package dev.nottekk.notvolt.utils.data;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ProfanityUtil {

    private static final String API_URL = "https://www.purgomalum.com/service/containsprofanity?text=";

    public static boolean containsCurseWords(String text) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
            HttpGet request = new HttpGet(API_URL + encodedText);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                return Boolean.parseBoolean(jsonResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
