package com.defensepoint.integrationtests;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.restassured.response.Response;
import java.time.ZonedDateTime;
import org.junit.Test;

public class WhiteSourceTests21Points extends  Points21BloodPressureResourceIT{
  @Test
  public void unsafe() {
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
  public void safe() {
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

}
