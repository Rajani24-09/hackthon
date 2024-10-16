package com.mysite.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Model(adaptables = SlingHttpServletRequest.class)
public class HelloWorldModel {

    @Self
    private SlingHttpServletRequest request;

    @ValueMapValue(name = "textToTranslate")
    private String textToTranslate;

    private String translatedText;

    private static final String API_KEY = "YOUR_SUBSCRIPTION_KEY";
    private static final String REGION = "YOUR_REGION";
    private static final String ENDPOINT = "https://api.cognitive.microsofttranslator.com/translate?api-version=3.0&to=fr";

    @PostConstruct
    protected void init() {
        try {
            translatedText = translateText(textToTranslate);
        } catch (Exception e) {
            translatedText = "Translation failed: " + e.getMessage();
        }
    }

    public String getTranslatedText() {
        return translatedText;
    }

    private String translateText(String text) throws Exception {
        URL url = new URL(ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", API_KEY);
        connection.setRequestProperty("Ocp-Apim-Subscription-Region", REGION);
        connection.setDoOutput(true);

        String jsonInputString = "[{\"Text\":\"" + text + "\"}]";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return parseTranslationResponse(response.toString());
        }
    }

    private String parseTranslationResponse(String response) {
        // Implement JSON parsing logic to extract translated text
        // This is a placeholder for actual JSON parsing
        return response; // Replace with actual parsing logic
    }
}
