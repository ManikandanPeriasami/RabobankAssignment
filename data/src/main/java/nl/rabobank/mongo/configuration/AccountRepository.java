package nl.rabobank.mongo.configuration;

import nl.rabobank.account.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * User account repository.
 */
public interface AccountRepository extends MongoRepository<Account, String> {
}
