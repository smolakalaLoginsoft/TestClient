package com.defensepoint.integrationtests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration tests for the Users of KeyClock.
 */
public class KeyCloakUserIT {

    private final String JSON = "application/json";
    private String access_token;
    private String userId;

    @Before
    public void setUp() {
        RestAssured.baseURI="http://localhost";
        RestAssured.port=8080;

        getAuthorizationToken();
        getUserId();
    }

    private void getAuthorizationToken() {

        Response response = given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("username", "admin")
                .formParam("password", "admin")
                .formParam("client_id", "admin-cli")
                .formParam("grant_type", "password")
                .when()
                .post("/auth/realms/master/protocol/openid-connect/token")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .response();

        access_token = response.jsonPath().getString("access_token");
    }

    public void getUserId() {
        Response response = given()
                .header("Authorization", "Bearer " + access_token)
                .when()
                .get("/auth/admin/realms/master/users")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .response();

        userId = response.jsonPath().getString("id");

        assertNotNull(userId);
    }

    @Test
    public void getUserById() {
        Response response = given()
                .header("Authorization", "Bearer " + access_token)
                .pathParam("id", userId)
                .when()
                .get("/auth/admin/realms/master/users/{id}")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .response();

        userId = response.jsonPath().getString("id");

        assertNotNull(userId);
    }

    @Test
    public void updateUser() {
        Map<String, Object> userJsonPayload = new HashMap<>();
        userJsonPayload.put("firstName", "we");
        userJsonPayload.put("lastName", "we");

        given()
                .header("Authorization", "Bearer " + access_token)
                .header("Content-Type", "application/json")
                .pathParam("id", userId)
                .body(userJsonPayload)
                .when()
                .put("/auth/admin/realms/master/users/{id}")
                .then()
                .statusCode(204);
    }
}
