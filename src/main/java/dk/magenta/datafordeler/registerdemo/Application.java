package dk.magenta.datafordeler.registerdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by lars on 16-02-17.
 */
@ServletComponentScan
@SpringBootApplication
public class Application {

    @Autowired
    SessionManager sessionManager;

    public static void main(final String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }


    /* For testing the servlet in a throwaway Tomcat container */
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        return new TomcatEmbeddedServletContainerFactory(8445);
    }
}
