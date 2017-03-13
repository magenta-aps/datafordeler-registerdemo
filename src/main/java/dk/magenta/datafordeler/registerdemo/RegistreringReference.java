package dk.magenta.datafordeler.registerdemo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by lars on 08-03-17.
 */
public class RegistreringReference {

    @JsonProperty
    public String checksum;

    @JsonProperty("sekvensNummer")
    public int sequenceNumber;

    public RegistreringReference(String checksum, int sequenceNumber) {
        this.checksum = checksum;
        this.sequenceNumber = sequenceNumber;
    }

    @JsonProperty("uri")
    public String getReference() {
        return "/postnummer/"+this.checksum+"/get";
    }
}
