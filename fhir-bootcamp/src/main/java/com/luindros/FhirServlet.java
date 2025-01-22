package com.luindros;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import org.hl7.fhir.r4.model.Observation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
//import jakarta.servlet.ServletException;

/**
 * FHIR Server Servlet Configuration
 * This class extends RestfulServer to create a FHIR-compliant server that
 * handles
 * Patient and Observation resources. It manages database connections and
 * registers
 * resource providers and interceptors.
 * 
 * The server is configured to:
 * - Use FHIR R4 (4.0.0) specification
 * - Connect to a PostgreSQL database
 * - Handle Patient and Observation resources
 * - Include response highlighting for better readability
 */
public class FhirServlet extends RestfulServer {

    /**
     * Initializes the FHIR server configuration
     * This method is called automatically when the servlet starts
     * 
     * Performs the following setup:
     * 1. Sets FHIR context to R4
     * 2. Establishes database connection
     * 3. Registers resource providers
     * 4. Configures interceptors for response formatting
     */
    @Override
    protected void initialize() {
        // Set FHIR specification version to R4
        setFhirContext(FhirContext.forR4());

        // Database connection parameters
        String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "postgres";

        try {
            // Load PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

            // Establish database connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Register resource providers with database connection
            registerProvider(new PatientResourceProvider(connection));
            registerProvider(new ObservationProvider(connection));

            // Add response highlighter for formatted output
            registerInterceptor(new ResponseHighlighterInterceptor());
        } catch (ClassNotFoundException e) {
            // Handle JDBC driver loading errors
            e.printStackTrace();
        } catch (SQLException e) {
            // Handle database connection errors
            e.printStackTrace();
        }
    }
}