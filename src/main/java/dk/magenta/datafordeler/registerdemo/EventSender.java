package dk.magenta.datafordeler.registerdemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by lars on 20-02-17.
 */
@Component
public class EventSender {

    public static final String messageVersion = "1.0";
    private ObjectMapper objectMapper;

    public EventSender(){
        this.objectMapper = new ObjectMapper();
    }


    public void sendPostnummerRegistreringAddEvent(PostnummerRegistrering registrering) {
        this.sendPostnummerRegistreringAddEvent(registrering, false);
    }
    public void sendPostnummerRegistreringAddEvent(PostnummerRegistrering registrering, boolean referenceOnly) {
        if (referenceOnly) {
            this.sendEvent("Postnummer", null, registrering.getReference());
        } else {
            this.sendEvent("Postnummer", registrering, null);
        }
    }

    private void sendEvent(String skema, Object data, Object reference) {
        try {
            Event event = new Event();
            event.setBeskedVersion(messageVersion);
            event.setDataskema(skema);
            if (data != null) {
                event.setObjektData(objectMapper.writeValueAsString(data));
            }
            if (reference != null) {
                event.setObjektReference(objectMapper.writeValueAsString(reference));
            }
            this.sendEvent(event);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void sendEvent(Event event) {
        System.out.println(event);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://localhost:8444/odata/gapi/Events");
        try {
            System.out.println("POST");
            System.out.println("http://localhost:8444/odata/gapi/Events");
            System.out.println(this.objectMapper.writeValueAsString(event));
            StringEntity eventEntity = new StringEntity(this.objectMapper.writeValueAsString(event), ContentType.APPLICATION_JSON.withCharset(Charset.forName("utf-8")));
            post.setEntity(eventEntity);
            CloseableHttpResponse response = httpclient.execute(post);
            System.out.println(response.getStatusLine());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
