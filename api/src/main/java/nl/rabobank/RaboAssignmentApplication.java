package nl.rabobank;

import nl.rabobank.mongo.configuration.MongoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Account API Application Startup
 */
@Import(MongoConfiguration.class)
@SpringBootApplication
public class RaboAssignmentApplication {

    /**
     * Main method of the application.
     * @param args argument details.
     */
    public static void main(final String[] args) {
        SpringApplication.run(RaboAssignmentApplication.class, args);
    }
}
