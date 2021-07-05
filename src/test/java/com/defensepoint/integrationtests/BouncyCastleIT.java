package com.defensepoint.integrationtests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BouncyCastleIT {

    private String publicKey;
    private String privateKey;

    @Before
    public void setUp() {
        RestAssured.baseURI="http://localhost";
        RestAssured.port=8080;

        getXMSSKeys();
    }

    public void getXMSSKeys() {
        Response response = given()
                .when()
                .get("/BouncyCastle/GetKeys")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response();

        privateKey = response.jsonPath().getString("privateKey");
        publicKey = response.jsonPath().getString("publicKey");

        assertNotNull(privateKey);
        assertNotNull(publicKey);
    }

    @Test
    public void getSignature() {
        Map<String, Object> keyJsonPayload = new HashMap<>();
        keyJsonPayload.put("publicKey", publicKey);
        keyJsonPayload.put("privateKey", privateKey);

        given()
                .header("Content-Type", "application/json")
                .body(keyJsonPayload)
                .when()
                .post("/BouncyCastle/GetSignature")
                .then()
                .statusCode(200);
    }
}
