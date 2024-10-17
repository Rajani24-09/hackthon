package com.mysite.core.servlets;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.core.beans.TranslationRequest;
import com.mysite.core.beans.TranslationResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.osgi.framework.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@Component(
        immediate = true,
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=ChatGPT Integration",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/chat",
                "sling.servlet.extensions={\"json\"}"
        }
)
public class TranslationServlet extends SlingSafeMethodsServlet {

    private static final Logger LOGGER = Logger.getLogger(TranslationServlet.class.getName());

    private static final String CHATGPT_API_ENDPOINT = "https://api.openai.com/v1/chat/completions";
    private static final String PS_CHAT_API_URL = "https://api.psnext.info/api/chat";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySW5mbyI6eyJpZCI6MjM3OTMsInJvbGVzIjpbImRlZmF1bHQiXSwicGF0aWQiOiIzNjIzYjc3Zi1hZTllLTRiYzEtOWY0NC04YzMyNmI0YTZlNTcifSwiaWF0IjoxNzI5MTAyNTU4LCJleHAiOjE3MzE2OTQ1NTh9.k3gln60yE5gAQ9eqUvuf2h1RdQHt271huwrSUAzPZAk";


    private static final HttpClient client = HttpClients.createDefault();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String prompt = request.getParameter("prompt");
        String message = generateMessage(prompt);
        response.getWriter().write(message);
    }

    private static String generateMessage(String prompt) throws IOException {

        // Generate the chat message using ChatGPT API
        String requestBody = MAPPER.writeValueAsString(new TranslationRequest(prompt,"gpt-3.5-turbo","user"));
        HttpPost request = new HttpPost(PS_CHAT_API_URL);
        request.addHeader("Authorization", "Bearer "+API_KEY);
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(requestBody));
        System.out.println("requestBody: "+requestBody);
        HttpResponse response = client.execute(request);
        TranslationResponse translationResponse = MAPPER.readValue(EntityUtils.toString(response.getEntity()), TranslationResponse.class);
        String message = translationResponse.getChoices().get(0).getMessage().getContent();

        return message;

    }


    public static void main(String[] args) {
        try {
            System.out.println(generateMessage("What is Adobe AEM"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
