package dk.magenta.datafordeler.registerdemo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;

/**
 * Created by lars on 16-03-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Application.class)
public class PostnummerTest {

    private static final String INTERFACE_CREATE_PATH = "/postnummer/create";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void methodNotSupported() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        String body = "";
        HttpEntity<String> httpEntity = new HttpEntity<String>(body, headers);
        HttpMethod[] methods = {HttpMethod.PUT, HttpMethod.DELETE};
        for (HttpMethod method : methods) {
            ResponseEntity<String> resp = this.restTemplate.exchange(INTERFACE_CREATE_PATH, method, httpEntity, String.class);
            Assert.assertEquals("Method "+method.name(),405, resp.getStatusCode().value());
        }
    }

    /**
     * Tests that the GAPI entry point accepts a test input
     * @throws IOException
     */
    @Test
    public void post() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("nummer", "8000");
        formData.add("bynavn", "Århus C");
        formData.add("skipEvent", "1");
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(formData, headers);
        ResponseEntity<String> resp = this.restTemplate.exchange(INTERFACE_CREATE_PATH, HttpMethod.POST, httpEntity, String.class);
        Assert.assertEquals(302, resp.getStatusCode().value());
    }

}
