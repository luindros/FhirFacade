package com.luindros;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.hl7.fhir.r4.model.Observation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import jakarta.servlet.ServletException;

public class FhirServlet extends RestfulServer {
    @Override
    protected void initialize() {
        setFhirContext(FhirContext.forR4());

        String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "postgres";

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            registerProvider(new PatientResourceProvider(connection));
            registerProvider(new ObservationProvider(connection));

            registerInterceptor(new ResponseHighlighterInterceptor());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}