package nl.rabobank.mongo.configuration;

import nl.rabobank.account.AccountAccess;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * User account permission repository
 */
public interface AccountAccessRepository extends MongoRepository<AccountAccess, String> {

    List<AccountAccess> findByGranteeAccountNumber(String granteeAccountNumber);
}
