package com.defensepoint.integrationtests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for the ItemController controller of OnlineStore.
 */
public class OnlineStoreItemControllerIT {

    private String jSessionId;
    private final String JSON = "application/json";

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI="http://localhost";
        RestAssured.port=8080;

        getAuthorizationHeader();
    }

    private void getAuthorizationHeader() {

        Response response = given()
                .auth()
                .preemptive()
                .basic("admin@admin.com", "admin")
                .when()
                .get("/login")
                .then()
                .extract()
                .response();

        jSessionId = response.getCookie("JSESSIONID");
    }

    @Test
    public void singleFileUpload() {

        Path path = Paths.get("item.txt");
        String someString = "[{\"name\":\"item\",\"uniqueNumber\":\"hey\",\"price\":10,\"description\":\"hey desc\",\"deleted\":false}]";
        byte[] bytes = someString.getBytes();

        try {
            Files.write(path, bytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        File itemFile = new File("item.txt");

        Response response = given()
                .cookie("JSESSIONID", jSessionId)
                .multiPart("file", itemFile)
                .when()
                .post("/items/upload");

        assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void addUserController() {

        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        sb.append( (char)(r.nextInt(26) + 'a') );
        sb.append( (char)(r.nextInt(26) + 'a') );
        sb.append( (char)(r.nextInt(26) + 'a') );
        sb.append( (char)(r.nextInt(26) + 'a') );
        sb.append( (char)(r.nextInt(26) + 'a') );

        Response response = given()
                .cookie("JSESSIONID", jSessionId)
                .contentType("multipart/form-data")
                .multiPart("surname", "test-surname")
                .multiPart("name", "test-name")
                .multiPart("middleName", "test")
                .multiPart("email", sb.toString() + "@test.com")
                .multiPart("role", "ADMINISTRATOR")
                .when()
                .post("/users/add")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    @Test
    public void singleFileUploadIsSubDirectory() {

        Path path = Paths.get("item.txt");
        String someString = "[{\"name\":\"item\",\"uniqueNumber\":\"hey\",\"price\":10,\"description\":\"hey desc\",\"deleted\":false}]";
        byte[] bytes = someString.getBytes();

        try {
            Files.write(path, bytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        File itemFile = new File("item.txt");

        Response response = given()
                .cookie("JSESSIONID", jSessionId)
                .multiPart("file", itemFile)
                .when()
                .post("/items/uploadIsSubDirectory");

        assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void singleFileUploadSanitizer() {

        Path path = Paths.get("item.txt");
        String someString = "[{\"name\":\"item\",\"uniqueNumber\":\"hey\",\"price\":10,\"description\":\"hey desc\",\"deleted\":false}]";
        byte[] bytes = someString.getBytes();

        try {
            Files.write(path, bytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        File itemFile = new File("item.txt");

        Response response = given()
                .cookie("JSESSIONID", jSessionId)
                .multiPart("file", itemFile)
                .when()
                .post("/items/uploadSanitizer");

        assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void singleFileUploadCheckFileName() {

        Path path = Paths.get("item.txt");
        String someString = "[{\"name\":\"item\",\"uniqueNumber\":\"hey\",\"price\":10,\"description\":\"hey desc\",\"deleted\":false}]";
        byte[] bytes = someString.getBytes();

        try {
            Files.write(path, bytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        File itemFile = new File("item.txt");

        Response response = given()
                .cookie("JSESSIONID", jSessionId)
                .multiPart("file", itemFile)
                .when()
                .post("/items/uploadCheckFileName");

        assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void singleFileUploadCheckPattern() {

        Path path = Paths.get("item.txt");
        String someString = "[{\"name\":\"item\",\"uniqueNumber\":\"hey\",\"price\":10,\"description\":\"hey desc\",\"deleted\":false}]";
        byte[] bytes = someString.getBytes();

        try {
            Files.write(path, bytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        File itemFile = new File("item.txt");

        Response response = given()
                .cookie("JSESSIONID", jSessionId)
                .multiPart("file", itemFile)
                .when()
                .post("/items/uploadCheckPattern");

        assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void singleFileUploadCheckUnsafePattern() {

        Path path = Paths.get("item.txt");
        String someString = "[{\"name\":\"item\",\"uniqueNumber\":\"hey\",\"price\":10,\"description\":\"hey desc\",\"deleted\":false}]";
        byte[] bytes = someString.getBytes();

        try {
            Files.write(path, bytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        File itemFile = new File("item.txt");

        Response response = given()
                .cookie("JSESSIONID", jSessionId)
                .multiPart("file", itemFile)
                .when()
                .post("/items/uploadCheckUnsafePattern");

        assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void addUserControllerUnsafeXSS() {

        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        sb.append( (char)(r.nextInt(26) + 'a') );
        sb.append( (char)(r.nextInt(26) + 'a') );
        sb.append( (char)(r.nextInt(26) + 'a') );
        sb.append( (char)(r.nextInt(26) + 'a') );
        sb.append( (char)(r.nextInt(26) + 'a') );

        Response response = given()
                .cookie("JSESSIONID", jSessionId)
                .contentType("multipart/form-data")
                .multiPart("surname", "test-surname")
                .multiPart("name", "<script>alert(123)</script>")
                .multiPart("middleName", "test")
                .multiPart("email", sb.toString() + "@test.com")
                .multiPart("role", "ADMINISTRATOR")
                .when()
                .post("/users/add")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    @Test
    public void addUserControllerUnsafeSQLi() {

        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        sb.append( (char)(r.nextInt(26) + 'a') );
        sb.append( (char)(r.nextInt(26) + 'a') );
        sb.append( (char)(r.nextInt(26) + 'a') );
        sb.append( (char)(r.nextInt(26) + 'a') );
        sb.append( (char)(r.nextInt(26) + 'a') );

        Response response = given()
                .cookie("JSESSIONID", jSessionId)
                .contentType("multipart/form-data")
                .multiPart("surname", "test-surname")
                .multiPart("name", "test-name")
                .multiPart("middleName", "test")
                .multiPart("email", "test@test.com','$2a$10$I3oa32qgXbNpMFYPKNDbA.eFER4S3M9Sl6QLHd2bm/TLBlH2.bpvW','ADMINISTRATOR',false); INSERT INTO user(id, surname, name, middle_name, email, password, role, is_deleted) VALUES (null,'test','test','test','test2@gmail.com")
                .multiPart("role", "ADMINISTRATOR")
                .when()
                .post("/users/add")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}
