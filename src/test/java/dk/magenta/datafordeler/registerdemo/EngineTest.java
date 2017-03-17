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

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by lars on 17-03-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Application.class)
public class EngineTest {

    private class EngineTestCallback implements RequestExpector.Callback {
        @Override
        public void run(String key, RequestExpector expector) {
        }
    }

    private static final String LOCAL_SERVICE = "http://localhost:8445";
    private static final String INTERFACE_BASE_PATH = "/postnummer";
    private static final String INTERFACE_CREATE_PATH = INTERFACE_BASE_PATH + "/create";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    PostnummerRepository postnummerRepository;

    @Autowired
    PostnummerRegistreringRepository postnummerRegistreringRepository;

    private ArrayList<String> returnKeys = new ArrayList<>();
    /*
    This test expects the DataFordeler Engine to be running on port 8444
     */
    @Test
    public void testEvent() {

        // Create a postnumber + registration, making sure that only a reference is passed to the Engine
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("nummer", "8200");
        formData.add("bynavn", "Århus N");
        formData.add("eventLevel", "1");

        // Expect a request on the lookup service
        RequestExpector returnCallExpector = new RequestExpector(null);
        returnCallExpector.addCallback(new EngineTestCallback(){
            public void run(String key, RequestExpector expector) {
                EngineTest.this.returnKeys.add(key);
            }
        });

        // Send a request to our own webservice; we could do it directly to the Engine, but this way we're testing more
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(formData, headers);
        ResponseEntity<String> response = this.restTemplate.exchange(LOCAL_SERVICE+INTERFACE_CREATE_PATH, HttpMethod.POST, httpEntity, String.class);
        Assert.assertEquals(302, response.getStatusCodeValue());

        // We expect a 302 redirect (due to the way our service works). Parse the redirect location and get the last registration on the referred postnummer
        URI redirectAddress = response.getHeaders().getLocation();
        Assert.assertTrue(redirectAddress.toString().startsWith(LOCAL_SERVICE+INTERFACE_BASE_PATH));
        String postnummerId = redirectAddress.toString().substring((LOCAL_SERVICE+INTERFACE_BASE_PATH+"/").length());
        Postnummer postnummer = postnummerRepository.findOne(Long.parseLong(postnummerId));
        Assert.assertNotNull(postnummer);
        PostnummerRegistrering last = null;
        for (PostnummerRegistrering postnummerRegistrering : postnummerRegistreringRepository.findByEntityOrderByRegisterFrom(postnummer)) {
            last = postnummerRegistrering;
        }
        Assert.assertNotNull(last);

        // No need to be subtle here, we're only testing. See if we get called back from the Engine
        String expectedKey = last.getChecksum();
        int i;
        int wait = 100;
        for (i=0; i<wait; i++) {
            if (this.returnKeys.contains(expectedKey)) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Assert.assertFalse(i == wait);

        // We have now ascertained that:
        //      The RegisterDemo creates the relevant objects and sends an Event request to the Engine
        //      The Engine gets the request, sees that it is a reference, and requests us back for the whole object
    }
}
