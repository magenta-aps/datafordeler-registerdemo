package dk.magenta.datafordeler.registerdemo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by lars on 08-03-17.
 */
@MappedSuperclass
@Embeddable
public abstract class Registrering {

    protected int sequenceNumber;

    protected String checksum;

    public Registrering() {}

    protected Registrering(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        this.updateChecksum();
    }

    @Column(name="registerFrom")
    @JsonProperty
    private Date registerFrom;

    @Column(name="registerTo")
    @JsonProperty
    private Date registerTo;

    public String getChecksum() {
        return checksum;
    }

    public abstract byte[] getChecksumInput();

    public void updateChecksum() {
        byte[] input = this.getChecksumInput();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(input);
            byte[] digest = md.digest();
            this.checksum = DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @JsonIgnore
    public RegistreringReference getReference() {
        return new RegistreringReference(this.getChecksum(), this.sequenceNumber);
    }
}
