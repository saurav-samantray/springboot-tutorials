package com.techgoons.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
public class I18mControllerTest {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

    URL base;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        base = new URL("http://localhost:" + port);
    }

    @Test
    @DisplayName("Default English")
    public void whenNoLocale_en(){
        ResponseEntity<String> response = restTemplate().getForEntity(base.toString(), String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().equals("Welcome to Tech Goons homepage"));

    }

    @Test
    @DisplayName("German")
    public void germanLocale_de() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Language", "de");
        ResponseEntity<String> response = restTemplate().exchange(
                base.toString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().equals("Willkommen auf der Tech Goons-Homepage"));
        }

        @Test
        @DisplayName("Spanish")
        public void spanishLocale_es(){
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept-Language", "es");
            Charset utf8 = Charset.forName("UTF-8");
            MediaType mediaType = new MediaType("text", "html", utf8);
            headers.setContentType(mediaType);
            ResponseEntity<String> response = restTemplate().exchange(
                    base.toString(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class);

            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            ByteBuffer buffer = StandardCharsets.UTF_8.encode("Bienvenido a la página de inicio de Tech Goons");

            String utf8EncodedString = StandardCharsets.UTF_8.decode(buffer).toString();
            System.out.println(utf8EncodedString);
            System.out.println(response.getBody());
            Assertions.assertEquals(utf8EncodedString,response.getBody());

    }
}
