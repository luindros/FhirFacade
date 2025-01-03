package com.luindros;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.sql.Statement;

public class PatientResourceProvider implements IResourceProvider {
    private Connection connection;

    /**
     * Constructor
     */
    public PatientResourceProvider(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Patient.class;
    }

    private Patient convertResultSetToPatient(ResultSet result) throws SQLException {
        int id = result.getInt("id");
        String first_name = result.getString("first_name");
        String last_name = result.getString("last_name");
        Date dob = result.getDate("date_of_birth");
        Patient patient = new Patient();
        patient.setId(Integer.toString(id));
        patient.addName().setFamily(last_name).addGiven(first_name).setText(first_name + " " + last_name);
        patient.setBirthDate(dob);
        return patient;
    }

    @Read()
    public Patient read(@IdParam IdType theId) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM patients WHERE id = " + theId.getIdPart() + ";");
        if (result.next()) {
            Patient patient = convertResultSetToPatient(result);
            return patient;
        } else {
            throw new ResourceNotFoundException(theId);
        }

    }

    @Search
    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM patients;");
        while (result.next()) {
            Patient patient = convertResultSetToPatient(result);
            patients.add(patient);
        }
        return patients;
    }

}