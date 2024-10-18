package com.mysite.core.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

@Component(service = Servlet.class,
        property = {
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/copilotIntegration",
                "sling.servlet.extensions={\"json\"}"
        })
public class AEMCopilotIntegrationServlet extends SlingSafeMethodsServlet {
//   private static final String COPILOT_API_URL = "https://api.copilot.microsoft.com/endpoint";
//    private static final String PS_CHAT_API_URL = "https://api.psnext.info/api/chat";
 //   private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySW5mbyI6eyJpZCI6MjM3OTMsInJvbGVzIjpbImRlZmF1bHQiXSwicGF0aWQiOiIzNjIzYjc3Zi1hZTllLTRiYzEtOWY0NC04YzMyNmI0YTZlNTcifSwiaWF0IjoxNzI5MTAyNTU4LCJleHAiOjE3MzE2OTQ1NTh9.k3gln60yE5gAQ9eqUvuf2h1RdQHt271huwrSUAzPZAk";

      @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{
          String userInput = request.getParameter("prompt");
          String locale = request.getParameter("locale");

       CloseableHttpClient httpClient = HttpClients.createDefault();
       HttpPost postRequest = new HttpPost("https://api.psnext.info/api/chat");
       postRequest.setHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySW5mbyI6eyJpZCI6MjM3OTMsInJvbGVzIjpbImRlZmF1bHQiXSwicGF0aWQiOiIzNjIzYjc3Zi1hZTllLTRiYzEtOWY0NC04YzMyNmI0YTZlNTcifSwiaWF0IjoxNzI5MTAyNTU4LCJleHAiOjE3MzE2OTQ1NTh9.k3gln60yE5gAQ9eqUvuf2h1RdQHt271huwrSUAzPZAk");
       postRequest.setHeader("Content-Type", "application/json");
          //Translate 'Hello, how are you?' to French
          String chatGptMsg = "Translate the following text into the locale "+locale+". Just respond with the corrected locale and nothing else. The data to convert is as follows:" + userInput;
          String jsonPayload = "{"
                  + "\"message\": \"" + chatGptMsg + "\", "
                  + "\"options\": {\"model\": \"gpt35turbo\"}"
                  + "}";

         postRequest.setEntity(new StringEntity(jsonPayload));

       // Execute the request and get the response
       String apiResponse = EntityUtils.toString(httpClient.execute(postRequest).getEntity());

       // Parse the response as JSON
       JsonObject jsonResponse = JsonParser.parseString(apiResponse).getAsJsonObject();
       JsonArray messages = jsonResponse.getAsJsonObject("data").getAsJsonArray("messages");

       // Extract the assistant's message
       String assistantResponse = null;
       for (JsonElement message : messages) {
           JsonObject messageObj = message.getAsJsonObject();
           String role = messageObj.get("role").getAsString();
           if ("assistant".equals(role)) {
               assistantResponse = messageObj.get("content").getAsString();
               break;  // We found the assistant's message, no need to continue
           }
       }

       // Write the assistant's message to the response
       response.setContentType("application/json");

       if (assistantResponse != null) {
           assistantResponse = assistantResponse.replace("\"", "\\\"");
           response.getWriter().write( assistantResponse.replace("\\\"","") );
           response.setStatus(HttpServletResponse.SC_OK);
       } else {
           response.getWriter().write("{\"error\": \"No assistant response found\"}");
           response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
       }
   }
}
