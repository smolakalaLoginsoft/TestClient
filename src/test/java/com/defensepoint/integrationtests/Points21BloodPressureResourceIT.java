package com.defensepoint.integrationtests;

import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration tests for the BloodPressureResource REST controller of 21-points.
 */
public class Points21BloodPressureResourceIT {

    protected final String RESOURCE_PATH="/api";
    protected final String JSON = "application/json";
    protected final String XML = "application/xml";
    protected final ZonedDateTime bloodPressureTimestamp = ZonedDateTime.now();
    protected final Integer bloodPressureSystolic = 90;
    protected final Integer bloodPressureDiastolic = 120;
    protected final String username = "admin";
    protected final String password = "admin";

    protected String authorizationHeader;
    protected Map<String, Object> bloodPressure;
    protected Map<String, Object> loginParams;
    protected Long bloodPressureId;
    protected Long pointsId;

    @Before
    public void setUp() {
        RestAssured.baseURI="http://localhost";
        RestAssured.port=8080;

        loginParams = new HashMap<>();
        loginParams.put("username", username);
        loginParams.put("password", password);

        getAuthorizationHeader();
        createBloodPressure();
        createPoints();
    }

    private void getAuthorizationHeader() {

        // Get the Authorization Header
        Response response = given()
                .contentType(JSON)
                .accept(JSON)
                .body(loginParams)
                .when()
                .post(RESOURCE_PATH + "/authenticate")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .response();

        authorizationHeader = response.header("Authorization");
    }

    public void createBloodPressure() {
        Map<String, Object> bloodPressureJsonPayload = new HashMap<>();
        bloodPressureJsonPayload.put("timestamp", "2020-12-07T05:31:00.000Z");
        bloodPressureJsonPayload.put("systolic", bloodPressureSystolic);
        bloodPressureJsonPayload.put("diastolic", bloodPressureDiastolic);
        bloodPressureJsonPayload.put("user", null);

        Response response = given()
                .contentType(JSON)
                .accept(JSON)
                .header("Authorization", authorizationHeader)
                .body(bloodPressureJsonPayload)
                .when()
                .post(RESOURCE_PATH + "/blood-pressures")
                .then()
                .statusCode(201)
                .contentType(JSON)
                .extract()
                .response();

        this.bloodPressureId =  response.jsonPath().getLong("id");
        ZonedDateTime timestamp = ZonedDateTime.parse(response.jsonPath().getString("timestamp"));
        Integer systolic = response.jsonPath().getInt("systolic");
        Integer diastolic = response.jsonPath().getInt("diastolic");

        assertNotNull(timestamp);
        assertNotNull(systolic);
        assertNotNull(diastolic);

        assertEquals(systolic, bloodPressureSystolic);
        assertEquals(diastolic, bloodPressureDiastolic);
    }

    public void createPoints() {
        Map<String, Object> pointsJsonPayload = new HashMap<>();
        pointsJsonPayload.put("date", "2020-12-08");
        pointsJsonPayload.put("exercise", 1);
        pointsJsonPayload.put("meals", 1);
        pointsJsonPayload.put("alcohol", 1);
        pointsJsonPayload.put("notes", "hey");
        pointsJsonPayload.put("user", null);

        Response response = given()
                .contentType(JSON)
                .accept(JSON)
                .header("Authorization", authorizationHeader)
                .body(pointsJsonPayload)
                .when()
                .post(RESOURCE_PATH + "/points")
                .then()
                .statusCode(201)
                .contentType(JSON)
                .extract()
                .response();

        this.pointsId =  response.jsonPath().getLong("id");
        String notes = response.jsonPath().getString("notes");

        assertNotNull(notes);

        assertEquals(notes, "hey");
    }

    @Test
    public void updateBloodPressureWithXMLPayloadParseWithDbf() {
        String xmlPayload =  "<bloodPressure>" +
                "<bloodPressureId>"+ bloodPressureId +"</bloodPressureId>" +
                "<bloodPressureTimestamp>" + ZonedDateTime.now() + "</bloodPressureTimestamp>" +
                "<bloodPressureSystolic>"+ bloodPressureSystolic +"</bloodPressureSystolic>" +
                "<bloodPressureDiastolic>"+ bloodPressureDiastolic + "</bloodPressureDiastolic>" +
                "<user null=\"true\"/></bloodPressure>";

        Response response = given()
                .contentType(XML)
                .accept(JSON)
                .header("Authorization", authorizationHeader)
                .pathParam("parser", "dbf")
                .body(xmlPayload)
                .when()
                .put(RESOURCE_PATH + "/blood-pressures-xml/{parser}")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .response();

        Long idResponse =  response.jsonPath().getLong("id");
        ZonedDateTime timestamp = ZonedDateTime.parse(response.jsonPath().getString("timestamp"));
        Integer systolic = response.jsonPath().getInt("systolic");
        Integer diastolic = response.jsonPath().getInt("diastolic");

        assertNotNull(idResponse);
        assertNotNull(timestamp);
        assertNotNull(systolic);
        assertNotNull(diastolic);

        assertEquals(idResponse, bloodPressureId);
        assertEquals(systolic, bloodPressureSystolic);
        assertEquals(diastolic, bloodPressureDiastolic);
    }

    @Test
    public void updateBloodPressureWithXMLPayloadParseWithXif() {
        String xmlPayload =  "<bloodPressure>" +
                "<bloodPressureId>"+ bloodPressureId +"</bloodPressureId>" +
                "<bloodPressureTimestamp>" + ZonedDateTime.now() + "</bloodPressureTimestamp>" +
                "<bloodPressureSystolic>"+ bloodPressureSystolic +"</bloodPressureSystolic>" +
                "<bloodPressureDiastolic>"+ bloodPressureDiastolic + "</bloodPressureDiastolic>" +
                "<user null=\"true\"/></bloodPressure>";

        Response response = given()
                .contentType(XML)
                .accept(JSON)
                .header("Authorization", authorizationHeader)
                .pathParam("parser", "xif")
                .body(xmlPayload)
                .when()
                .put(RESOURCE_PATH + "/blood-pressures-xml/{parser}")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .response();

        Long idResponse =  response.jsonPath().getLong("id");
        ZonedDateTime timestamp = ZonedDateTime.parse(response.jsonPath().getString("timestamp"));
        Integer systolic = response.jsonPath().getInt("systolic");
        Integer diastolic = response.jsonPath().getInt("diastolic");

        assertNotNull(idResponse);
        assertNotNull(timestamp);
        assertNotNull(systolic);
        assertNotNull(diastolic);

        assertEquals(idResponse, bloodPressureId);
        assertEquals(systolic, bloodPressureSystolic);
        assertEquals(diastolic, bloodPressureDiastolic);
    }

    @Test
    public void updateBloodPressureWithXMLPayloadParseWithXifUnsafePayload() {
        String xmlPayload =
                "<?xml version=\"1.0\"?>\n" +
                "<!DOCTYPE lolz [\n" +
                "        <!ENTITY lol \"lol\">\n" +
                "        <!ELEMENT lolz (#PCDATA)>\n" +
                "        <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
                "        <!ENTITY lol2 \"&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;\">\n" +
                "        <!ENTITY lol3 \"&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;\">\n" +
                "        <!ENTITY lol4 \"&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;\">\n" +
                "        <!ENTITY lol5 \"&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;\">\n" +
                "        <!ENTITY lol6 \"&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;\">\n" +
                "        <!ENTITY lol7 \"&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;\">\n" +
                "        <!ENTITY lol8 \"&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;\">\n" +
                "        <!ENTITY lol9 \"&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;\">\n" +
                "        ]>\n" +
                "<lolz>&lol9;</lolz>" +
                "<bloodPressure>" +
                "<bloodPressureId>"+ bloodPressureId +"</bloodPressureId>" +
                "<bloodPressureTimestamp>" + ZonedDateTime.now() + "</bloodPressureTimestamp>" +
                "<bloodPressureSystolic>"+ bloodPressureSystolic +"</bloodPressureSystolic>" +
                "<bloodPressureDiastolic>"+ bloodPressureDiastolic + "</bloodPressureDiastolic>" +
                "<user null=\"true\"/></bloodPressure>";

        Response response = given()
                .contentType(XML)
                .accept(JSON)
                .header("Authorization", authorizationHeader)
                .pathParam("parser", "xif")
                .body(xmlPayload)
                .when()
                .put(RESOURCE_PATH + "/blood-pressures-xml/{parser}")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .response();

        Long idResponse =  response.jsonPath().getLong("id");
        ZonedDateTime timestamp = ZonedDateTime.parse(response.jsonPath().getString("timestamp"));
        Integer systolic = response.jsonPath().getInt("systolic");
        Integer diastolic = response.jsonPath().getInt("diastolic");

        assertNotNull(idResponse);
        assertNotNull(timestamp);
        assertNotNull(systolic);
        assertNotNull(diastolic);

        assertEquals(idResponse, bloodPressureId);
        assertEquals(systolic, bloodPressureSystolic);
        assertEquals(diastolic, bloodPressureDiastolic);
    }

    @Test
    public void updateBloodPressure() {

        Map<String, Object> bloodPressureJsonPayload = new HashMap<>();
        bloodPressureJsonPayload.put("id", this.bloodPressureId);
        bloodPressureJsonPayload.put("timestamp", "2020-12-07T05:31:00.000Z");
        bloodPressureJsonPayload.put("systolic", 89);
        bloodPressureJsonPayload.put("diastolic", 119);
        bloodPressureJsonPayload.put("user", null);

        Response response = given()
                .contentType(JSON)
                .accept(JSON)
                .header("Authorization", authorizationHeader)
                .body(bloodPressureJsonPayload)
                .when()
                .put(RESOURCE_PATH + "/blood-pressures")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .response();

        Long idResponse =  response.jsonPath().getLong("id");
        ZonedDateTime timestamp = ZonedDateTime.parse(response.jsonPath().getString("timestamp"));
        Integer systolic = response.jsonPath().getInt("systolic");
        Integer diastolic = response.jsonPath().getInt("diastolic");

        assertNotNull(idResponse);
        assertNotNull(timestamp);
        assertNotNull(systolic);
        assertNotNull(diastolic);

        assertEquals(idResponse, bloodPressureId);
        assertEquals(systolic, 89);
        assertEquals(diastolic, 119);
    }

    @Test
    public void updatePoints() {

        Map<String, Object> pointsJsonPayload = new HashMap<>();
        pointsJsonPayload.put("id", this.pointsId);
        pointsJsonPayload.put("date", "2020-12-08");
        pointsJsonPayload.put("exercise", 1);
        pointsJsonPayload.put("meals", 1);
        pointsJsonPayload.put("alcohol", 1);
        pointsJsonPayload.put("notes", "updated");
        pointsJsonPayload.put("user", null);

        Response response = given()
                .contentType(JSON)
                .accept(JSON)
                .header("Authorization", authorizationHeader)
                .body(pointsJsonPayload)
                .when()
                .put(RESOURCE_PATH + "/points")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .response();

        String notes = response.jsonPath().getString("notes");

        assertNotNull(notes);

        assertEquals(notes, "updated");
    }
}
