package dk.magenta.datafordeler.registerdemo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Iterator;

/**
 * Created by lars on 16-02-17.
 */
@Entity
public class Postnummer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*public Iterable<PostnummerRegistrering> getRegistreringer() {
        System.out.println("postnummerRegistreringRepository: "+postnummerRegistreringRepository);
        return postnummerRegistreringRepository.findByEntityOrderByRegisterFrom(this);
    }

    @JsonIgnore
    public PostnummerRegistrering getLatest() {
        Iterator<PostnummerRegistrering> iterator = this.getRegistreringer().iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }*/
}
