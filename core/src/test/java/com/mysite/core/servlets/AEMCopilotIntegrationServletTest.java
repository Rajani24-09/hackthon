package com.mysite.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

public class AEMCopilotIntegrationServletTest {

    private AEMCopilotIntegrationServlet servlet;
    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;

    @BeforeEach
    public void setup() {
        servlet = new AEMCopilotIntegrationServlet();
        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);
    }

    @Test
    public void testDoGet() throws Exception {
        // Mock parameters
        when(request.getParameter("prompt")).thenReturn("Hello, how are you?");
        when(request.getParameter("locale")).thenReturn("fr");

        // Mock PrintWriter
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        // Execute the servlet
        servlet.doGet(request, response);

        // Verify interactions and output
        verify(response).setContentType("application/json");
        verify(writer).write(anyString());
    }
}
