package com.techgoons.security.springbootsecurity.config;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
public class BasicConfigurationIntegrationTest {

    private static final String ADMIN_USER="admin";
    private static final String ADMIN_PASSWORD="admin";
    private static final String REGULAR_USER="user";
    private static final String REGULAR_PASSWORD="password";
    private static final String INVALID_USER="garbage";
    private static final String INVALID_PASSWORD="garbage";
    private static final String PUBLIC_ENDPOINT="/";
    private static final String USER_ENDPOINT="/user";
    private static final String ADMIN_ENDPOINT="/admin";

    @Autowired
    private TestRestTemplate restTemplate;

    URL base;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        base = new URL("http://localhost:" + port);
    }

    @Test
    @DisplayName("Public API")
    public void publicAPIwithoutLogin_ThenSuccess()
            throws IllegalStateException {
        restTemplate = new TestRestTemplate();
        ResponseEntity<String> response =
                restTemplate.getForEntity(base.toString(), String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("Hello Public"));
    }

    @Test
    @DisplayName("User API - correct authentication")
    public void whenLoggedUserRequestsUserAPI_ThenSuccess()
            throws IllegalStateException {
        restTemplate = new TestRestTemplate(REGULAR_USER, REGULAR_PASSWORD);
        ResponseEntity<String> response =
                restTemplate.getForEntity(base.toString()+USER_ENDPOINT, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("Hello User"));
    }

    @Test
    @DisplayName("User API - correct authentication admin")
    public void whenLoggedAdminRequestsUserAPI_ThenSuccess()
            throws IllegalStateException {
        restTemplate = new TestRestTemplate(ADMIN_PASSWORD, ADMIN_USER);
        ResponseEntity<String> response =
                restTemplate.getForEntity(base.toString()+USER_ENDPOINT, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("Hello User"));
    }

    @Test
    @DisplayName("User API - incorrect authentication")
    public void whenIncorrectAuthRequestsUserAPI_ThenUnAuthorized()
            throws IllegalStateException {
        restTemplate = new TestRestTemplate(INVALID_USER, INVALID_PASSWORD);
        ResponseEntity<String> response =
                restTemplate.getForEntity(base.toString()+USER_ENDPOINT, String.class);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DisplayName("User API - unauthenticated")
    public void whenUnAuthenticatedRequestsUserAPI_ThenUnAuthorized()
            throws IllegalStateException {
        restTemplate = new TestRestTemplate();
        ResponseEntity<String> response =
                restTemplate.getForEntity(base.toString()+USER_ENDPOINT, String.class);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DisplayName("Admin API - correct authentication")
    public void whenLoggedAdminRequestsAdminAPI_ThenSuccess()
            throws IllegalStateException {
        restTemplate = new TestRestTemplate(ADMIN_USER, ADMIN_PASSWORD);
        ResponseEntity<String> response =
                restTemplate.getForEntity(base.toString()+ADMIN_ENDPOINT, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("Hello Admin"));
    }

    @Test
    @DisplayName("Admin API - incorrect authentication")
    public void whenIncorrectAuthRequestsAdminAPI_ThenUnAuthorized()
            throws IllegalStateException {
        restTemplate = new TestRestTemplate(INVALID_USER, INVALID_PASSWORD);
        ResponseEntity<String> response =
                restTemplate.getForEntity(base.toString()+ADMIN_ENDPOINT, String.class);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DisplayName("Admin API - unauthenticated")
    public void whenUnAuthenticatedRequestsAdminAPI_ThenUnAuthorized()
            throws IllegalStateException {
        restTemplate = new TestRestTemplate();
        ResponseEntity<String> response =
                restTemplate.getForEntity(base.toString()+ADMIN_ENDPOINT, String.class);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DisplayName("Admin API - forbidden")
    public void whenUserRequestsAdminAPI_ThenForbidden()
            throws IllegalStateException {
        restTemplate = new TestRestTemplate();
        restTemplate = new TestRestTemplate(REGULAR_USER, REGULAR_PASSWORD);
        ResponseEntity<String> response =
                restTemplate.getForEntity(base.toString()+ADMIN_ENDPOINT, String.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
