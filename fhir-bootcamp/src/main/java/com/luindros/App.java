package com.luindros; // Package declaration for organizing classes

import org.eclipse.jetty.server.Server; // Import for creating a Jetty server
import org.eclipse.jetty.servlet.ServletContextHandler; // Import for handling servlet context
import org.eclipse.jetty.servlet.ServletHolder; // Import for holding servlets

public class App { // Main application class
    public static void main(String[] args) throws Exception { // Main method to run the application
        // Create a new Jetty server instance on port 8080
        Server server = new Server(8080);

        // Create a new instance of the FhirServlet
        FhirServlet fhirServlet = new FhirServlet();

        // Create a ServletContextHandler to manage the servlet context
        ServletContextHandler handler = new ServletContextHandler();

        // Add the FhirServlet to the handler, mapping it to all requests (/*)
        handler.addServlet(new ServletHolder(fhirServlet), "/*");

        // Set the handler for the server
        server.setHandler(handler);

        // Start the server
        server.start();

        // Wait for the server to join (keep running)
        server.join();
    }
}