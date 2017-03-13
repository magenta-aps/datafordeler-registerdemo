package dk.magenta.datafordeler.registerdemo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by lars on 16-02-17.
 */
@Entity
@Table(name="PostnummerRegistrering")
public class PostnummerRegistrering extends Registrering {

    @ManyToOne
    @JoinColumn(name="entity")
    private Postnummer entity;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @Column(name="nummer")
    @JsonProperty
    private int nummer;

    @Column(name="bynavn")
    @JsonProperty
    private String bynavn;

    protected PostnummerRegistrering(int sequenceNumber) {
        super(sequenceNumber);
    }

    public PostnummerRegistrering() {
    }

    public Postnummer getEntity() {
        return entity;
    }

    public void setEntity(Postnummer entity) {
        this.entity = entity;
    }

    public Long getId() {
        return id;
    }

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
        this.updateChecksum();
    }

    public String getBynavn() {
        return bynavn;
    }

    public void setBynavn(String bynavn) {
        this.bynavn = bynavn;
        this.updateChecksum();
    }

    @JsonIgnore
    public byte[] getChecksumInput() {
        String data = this.sequenceNumber + " " + this.nummer + " " + this.bynavn;
        return data.getBytes();
    }
}
