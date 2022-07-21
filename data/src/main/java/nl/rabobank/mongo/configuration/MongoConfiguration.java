package nl.rabobank.mongo.configuration;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;

/**
 * Mongo DB Configuration.
 */
@Profile("!local")
@Configuration
@EnableMongoRepositories
@EnableConfigurationProperties(MongoProperties.class)
@Import(EmbeddedMongoAutoConfiguration.class)
@RequiredArgsConstructor
public class MongoConfiguration extends AbstractMongoClientConfiguration
{
    private final MongoProperties mongoProperties;

    /**
     * Return Database Name.
     *
     * @return database name.
     */
    @Override
    protected String getDatabaseName()
    {
        return mongoProperties.getMongoClientDatabase();
    }

    /**
     * MongoDb Client Bean.
     *
     * @return mongoclient bean.
     */
    @Override
    @Bean(destroyMethod = "close")
    public MongoClient mongoClient()
    {
        return MongoClients.create(mongoProperties.determineUri());
    }

}
