package dk.magenta.datafordeler.registerdemo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by lars on 10-02-17.
 */
@Component
public class SessionManager {

    private SessionFactory sessionFactory;

    private Session session;

    public SessionManager() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            this.sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void beginSession() {
        this.session = this.sessionFactory.openSession();
    }

    public Session getSession() {
        if (this.session == null) {
            this.beginSession();
        }
        return this.session;
    }

    public void endSession() {
        this.session.close();
        this.session = null;
    }
}
