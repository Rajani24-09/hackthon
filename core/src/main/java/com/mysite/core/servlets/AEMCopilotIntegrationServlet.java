package com.mysite.core.servlets;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
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
    private static final String COPILOT_API_URL = "https://api.copilot.microsoft.com/endpoint";
    private static final String PS_CHAT_API_URL = "https://api.psnext.info/api/chat";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySW5mbyI6eyJpZCI6MjM3OTMsInJvbGVzIjpbImRlZmF1bHQiXSwicGF0aWQiOiIzNjIzYjc3Zi1hZTllLTRiYzEtOWY0NC04YzMyNmI0YTZlNTcifSwiaWF0IjoxNzI5MTAyNTU4LCJleHAiOjE3MzE2OTQ1NTh9.k3gln60yE5gAQ9eqUvuf2h1RdQHt271huwrSUAzPZAk";

   @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{
       String userInput = request.getParameter("input");

       CloseableHttpClient httpClient = HttpClients.createDefault();
       HttpPost postRequest = new HttpPost("https://api.psnext.info/api/chat");
       postRequest.setHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySW5mbyI6eyJpZCI6MjM3OTMsInJvbGVzIjpbImRlZmF1bHQiXSwicGF0aWQiOiIzNjIzYjc3Zi1hZTllLTRiYzEtOWY0NC04YzMyNmI0YTZlNTcifSwiaWF0IjoxNzI5MTAyNTU4LCJleHAiOjE3MzE2OTQ1NTh9.k3gln60yE5gAQ9eqUvuf2h1RdQHt271huwrSUAzPZAk");
       postRequest.setHeader("Content-Type", "application/json");

       String jsonPayload = "{\"message\":\"" + userInput + "\", \"options\": {\"model\": \"gpt35turbo\"}}";
       postRequest.setEntity(new StringEntity(jsonPayload));

       String apiResponse = EntityUtils.toString(httpClient.execute(postRequest).getEntity());
       response.getWriter().write(apiResponse);
    }
}
