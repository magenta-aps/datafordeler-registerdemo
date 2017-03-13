package dk.magenta.datafordeler.registerdemo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by lars on 17-02-17.
 */
public interface PostnummerRegistreringRepository extends CrudRepository<PostnummerRegistrering, Long> {
    Iterable<PostnummerRegistrering> findByEntityOrderByRegisterFrom(Postnummer entity);
    PostnummerRegistrering findOneByChecksum(String checksum);
}
