package com.luindros;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//import java.sql.Connection;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
//import org.hl7.fhir.r4.model.Observation.ObservationComponentComponent;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * FHIR Resource Provider for Observations (Blood Pressure and Heart Rate)
 * This class handles FHIR operations for Observation resources, specifically
 * managing
 * blood pressure and heart rate measurements stored in a SQL database.
 * Each observation type has a unique suffix (-bp for blood pressure, -hr for
 * heart rate)
 */
public class ObservationProvider implements IResourceProvider {
    private Connection connection;

    /**
     * Constructor that initializes the database connection
     * 
     * @param conn SQL database connection for storing/retrieving observations
     */
    public ObservationProvider(Connection conn) {
        this.connection = conn;
    }

    /**
     * Required by IResourceProvider. Indicates this provider handles Observation
     * resources
     * 
     * @return Class object for Observation resource type
     */
    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Observation.class;
    }

    /**
     * Converts blood pressure database records into FHIR Observation resources
     * Creates a compound observation with both systolic and diastolic components
     * 
     * @param result SQL ResultSet containing blood pressure data
     * @return FHIR Observation resource with blood pressure components
     * @throws SQLException If there's an error reading from the ResultSet
     */
    private Observation convertBloodPressureToObservation(ResultSet result) throws SQLException {
        Observation observation = new Observation();
        observation.setId(result.getString("id") + "-bp");
        observation.setStatus(Observation.ObservationStatus.FINAL);
        observation.getCode().setText("Blood pressure panel with all children optional");
        observation.getCode().addCoding().setCode("85354-9")
                .setDisplay("Blood pressure panel with all children optional");

        Observation.ObservationComponentComponent systolicComponent = new Observation.ObservationComponentComponent()
                .setCode(new org.hl7.fhir.r4.model.CodeableConcept()
                        .addCoding(new org.hl7.fhir.r4.model.Coding()
                                .setCode("8480-6")
                                .setDisplay("Systolic blood pressure")
                                .setSystem("http://loinc.org")))
                .setValue(new org.hl7.fhir.r4.model.Quantity()
                        .setValue(result.getDouble("systolic"))
                        .setUnit("mmHg"));
        Observation.ObservationComponentComponent diastolicComponent = new Observation.ObservationComponentComponent()
                .setCode(new org.hl7.fhir.r4.model.CodeableConcept()
                        .addCoding(new org.hl7.fhir.r4.model.Coding()
                                .setCode("8462-4")
                                .setDisplay("Diastolic blood pressure")
                                .setSystem("http://loinc.org")))
                .setValue(new org.hl7.fhir.r4.model.Quantity()
                        .setValue(result.getDouble("diastolic"))
                        .setUnit("mmHg"));
        observation.addComponent(systolicComponent);
        observation.addComponent(diastolicComponent);

        observation.getSubject().setReference("Patient/" + result.getString("patient_id"));

        return observation;
    }

    /**
     * Converts heart rate database records into FHIR Observation resources
     * 
     * @param result SQL ResultSet containing heart rate data
     * @return FHIR Observation resource with heart rate measurement
     * @throws SQLException If there's an error reading from the ResultSet
     */
    private Observation convertHeartRateToObservation(ResultSet result) throws SQLException {
        Observation observation = new Observation();
        observation.setId(result.getString("id") + "-hr");
        observation.setStatus(Observation.ObservationStatus.FINAL);
        observation.getCode().setText("Heart Rate")
                .addCoding().setCode("8867-4").setDisplay("Heart rate").setSystem("http://loinc.org");
        ;
        observation.setValue(
                new org.hl7.fhir.r4.model.Quantity()
                        .setValue(result.getInt("rate"))
                        .setUnit("/min"));
        observation.getSubject().setReference("Patient/" + result.getString("patient_id"));
        return observation;
    }

    /**
     * Handles FHIR search operation to retrieve all observations
     * Combines both blood pressure and heart rate observations
     * 
     * @return List of all Observation resources from both tables
     * @throws SQLException If there's an error accessing the database
     */
    @Search
    public List<Observation> getAllObservations() throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet bpResults = statement.executeQuery("SELECT * from blood_pressure;");
        // get result set
        List<Observation> observations = new ArrayList<Observation>();
        while (bpResults.next()) {
            Observation bpResource = convertBloodPressureToObservation(bpResults);
            // observations.add(observation);
            observations.add(bpResource);
        }
        bpResults.close();
        ResultSet heartRateResults = statement.executeQuery("SELECT * from heart_rate;");
        while (heartRateResults.next()) {
            Observation heartRateResource = convertHeartRateToObservation(heartRateResults);
            observations.add(heartRateResource);
        }
        return observations;
    }

    /**
     * Handles FHIR read operation for specific observations
     * Uses ID suffix to determine observation type (-bp or -hr)
     * 
     * @param id The ID of the observation to retrieve (format: number-type)
     * @return The requested Observation resource
     * @throws SQLException              If there's an error accessing the database
     * @throws ResourceNotFoundException If no observation found with given ID
     */
    @Read
    public Observation getObservation(@IdParam IdType id) throws SQLException {
        String[] parts = id.getIdPart().split("-");
        String prefix = parts[0];
        String suffix = parts[1];

        if (suffix.equals("bp")) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * from blood_pressure WHERE id = " + prefix + ";");
            if (result.next()) {
                return convertBloodPressureToObservation(result);
            }
        } else if (suffix.equals("hr")) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * from heart_rate WHERE id = " + prefix + ";");
            if (result.next()) {
                return convertHeartRateToObservation(result);
            }
        }
        throw new ResourceNotFoundException(id);
    }
}
