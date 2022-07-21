package nl.rabobank.mongo.configuration;

import nl.rabobank.account.Account;
import nl.rabobank.account.AccountAccess;
import nl.rabobank.account.AccountType;
import nl.rabobank.authorizations.Authorization;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Initial data of user account.
 */
@Configuration
public class DataSetupConfiguration {

    private final AccountAccessRepository accountAccessRepository;
    private final AccountRepository accountRepository;

    public DataSetupConfiguration(AccountAccessRepository accountAccessRepository, AccountRepository accountRepository) {
        this.accountAccessRepository = accountAccessRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Setup of user account details.
     */
    @PostConstruct
    public void dataSetup() {
        Account account1 = new Account("101", "Mani", 100D, AccountType.PAYMENT);
        Account account2 = new Account("102", "Bob", 200D, AccountType.SAVING);
        Account account3 = new Account("103", "Sam", 300D, AccountType.SAVING);
        accountRepository.save(account1);
        accountRepository.save(account2);
        accountRepository.save(account3);

        AccountAccess accountAccess = new AccountAccess("101", "103", Authorization.READ);
        accountAccessRepository.save(accountAccess);
    }

}
