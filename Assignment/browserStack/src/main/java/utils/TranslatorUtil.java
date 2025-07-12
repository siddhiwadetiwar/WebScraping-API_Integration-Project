package utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

public class TranslatorUtil {

    private static final String[] API_KEYS = {
        "d0cfd9e265mshd268e15baca02acp135e56jsn6826ff8c45e5",
    };
    
    private static int currentKeyIndex = 0;
    
    public static synchronized String getNextApiKey() {
        String key = API_KEYS[currentKeyIndex];
        currentKeyIndex = (currentKeyIndex + 1) % API_KEYS.length;
        return key;
    }
    
    // private static final String API_KEY = "2e4e0c5593msh92fccf7c2b31888p15e395jsna15b153a64dd";
    // private static final String API_KEY2= "7d481c6e99mshc42b3d650343a99p1bf388jsnfb66d772b5ee";
    // private static final String API_KEY3= "3c633ac98fmsh58ce17eeb32173cp132c7bjsne96f7cf98203";
    // private static final String API_KEY4= "2dfc768a72msh039fb22e86b99e9p1c9398jsn8b246ef60552";
    private static final String API_HOST = "google-api31.p.rapidapi.com";
    private static final String API_URL = "https://google-api31.p.rapidapi.com/gtranslate";
    private static final long INITIAL_WAIT_TIME = 5000; // Start with 5 seconds
    
    private static long lastRequestTime = 0;
    
    public static String translate(String text, String sourceLang, String targetLang) {
        // Input validation
        if (text == null || text.trim().isEmpty()) {
            return "[Empty Text]";
        }
        
        if (sourceLang == null || targetLang == null) {
            return "[Invalid Language Parameters]";
        }
        
        // Truncate very long text to avoid API limits
        String textToTranslate = text.length() > 500 ? text.substring(0, 500) + "..." : text;
        
        long waitTime = INITIAL_WAIT_TIME;
        
        // for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(API_URL);
                conn = (HttpURLConnection) url.openConnection();
                
                // Set connection timeouts
                conn.setConnectTimeout(15000); // 15 seconds
                conn.setReadTimeout(20000); // 20 seconds
                
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                // conn.setRequestProperty("X-RapidAPI-Key", API_KEY);
                String apiKey = getNextApiKey();
                conn.setRequestProperty("x-rapidapi-key", apiKey);
                System.out.println("🔁 Using API Key: " + apiKey.substring(0, 5) + "****");
                conn.setRequestProperty("x-Rapidapi-host", API_HOST);
                conn.setDoOutput(true);

                // JSON payload
                JSONObject payload = new JSONObject();
                payload.put("from_lang", sourceLang);
                payload.put("to", targetLang);
                payload.put("text", textToTranslate);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
        
                int status = conn.getResponseCode();
        
                if (status == 200) {
                    try (BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = in.readLine()) != null) {
                            response.append(line);
                        }
        
                        JSONObject json = new JSONObject(response.toString());
                        System.out.println("✅ JSON Response: " + json.toString());
        
                        String translatedText = json.getString("translated_text");
                        lastRequestTime = System.currentTimeMillis();
                        return translatedText;
                    }
                } else if (status == 429) {
                    String retryAfterHeader = conn.getHeaderField("Retry-After");
        
                    if (retryAfterHeader != null && !retryAfterHeader.isEmpty()) {
                        System.out.println("⚠️ Rate limited. Retry-After header suggests waiting " + retryAfterHeader + " seconds.");
                    } else {
                        System.out.println("⚠️ Rate limited. No Retry-After header provided.");
                    }
        
                    return "[Translation Error - Rate Limit]";
                } else {
                    System.out.println("❌ Translation failed: HTTP " + status);
                    return "[Translation Error - HTTP " + status + "]";
                }
            } catch (Exception e) {
                System.out.println("❌ Translation failed: " + e.getMessage());
                return "[Translation Error - " + e.getClass().getSimpleName() + "]";
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
}
