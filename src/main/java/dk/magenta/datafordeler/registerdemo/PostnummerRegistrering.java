package dk.magenta.datafordeler.registerdemo;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by lars on 16-02-17.
 */
@Entity
@Table(name="PostnummerRegistrering")
public class PostnummerRegistrering {

    @ManyToOne
    @JoinColumn(name="entity")
    private Postnummer entity;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="registerFrom")
    private Date registerFrom;

    @Column(name="nummer")
    private int nummer;

    @Column(name="bynavn")
    private String bynavn;


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
    }

    public String getBynavn() {
        return bynavn;
    }

    public void setBynavn(String bynavn) {
        this.bynavn = bynavn;
    }

    public byte[] getChecksumInput() {
        String data = "" + this.nummer + " " + this.bynavn;
        return data.getBytes();
    }
}
