package com.mysite.core.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysite.core.config.CopilotIntegrationConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

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
@Designate(ocd = CopilotIntegrationConfig.class)
public class AEMCopilotIntegrationServlet extends SlingSafeMethodsServlet {

    private String apiKey;
    private String apiUrl;

    @Activate
    @Modified
    protected void activate(CopilotIntegrationConfig config) {
        this.apiKey = config.apiKey();
        this.apiUrl = config.apiUrl();
    }
      @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{
          String userInput = request.getParameter("prompt");
          String locale = request.getParameter("locale");

       CloseableHttpClient httpClient = HttpClients.createDefault();
       HttpPost postRequest = new HttpPost(apiUrl);
       postRequest.setHeader("Authorization", "Bearer " + apiKey);
       postRequest.setHeader("Content-Type", "application/json");
          //Translate 'Hello, how are you?' to French
          String jsonPayload = "{\"message\":\"" +"Translate'" +userInput +"' to"+locale+ "\", \"options\": {\"model\": \"gpt35turbo\"}}";

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
