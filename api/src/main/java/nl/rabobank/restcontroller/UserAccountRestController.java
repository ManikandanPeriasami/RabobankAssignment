package nl.rabobank.restcontroller;

import nl.rabobank.mongo.configuration.AccountRepository;
import nl.rabobank.account.Account;
import nl.rabobank.account.AccountAccess;
import nl.rabobank.mongo.configuration.AccountAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Rest Controller for account api with fetch and provide access to account.
 *
 */
@RestController
public class UserAccountRestController {

    private final AccountRepository accountRepository;
    private final AccountAccessRepository accountAccessRepository;

    @Autowired
    public UserAccountRestController(AccountRepository accountRepository,
                                     AccountAccessRepository accountAccessRepository) {
        this.accountRepository = accountRepository;
        this.accountAccessRepository = accountAccessRepository;
    }

    /**
     * Fetches list of account based on access of user.
     * @param accountNumber account number of user.
     * @return List of Account Details
     */
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<?> getAccountDetails(@PathVariable("accountNumber") String accountNumber) {

        List<AccountAccess> accountAccessList = accountAccessRepository.findByGranteeAccountNumber(accountNumber);
        List<String> accessAccountNumber = accountAccessList.stream()
                .map(AccountAccess::getGrantorAccountNumber)
                .collect(Collectors.toList());
        accessAccountNumber.add(accountNumber);

        Iterable<Account> accountList = accountRepository.findAllById(accessAccountNumber);
        if (!accountList.iterator().hasNext()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(accountList);
    }

    /**
     * Provide permission to the account.
     *
     * @param accountAccess permission details of user.
     * @return response based on valid information.
     */
    @PostMapping("/accountaccess")
    public ResponseEntity<?> provideAccountAccess(@RequestBody AccountAccess accountAccess) {

        String grantorAccountNumber = accountAccess.getGrantorAccountNumber();
        String granteeAccountNumber = accountAccess.getGranteeAccountNumber();

        if (accountAccess.getAuthorization() != null && granteeAccountNumber != null
                && grantorAccountNumber != null
                && !granteeAccountNumber.equalsIgnoreCase(grantorAccountNumber)) {

            Optional<Account> granteeAccount = accountRepository.findById(granteeAccountNumber);
            Optional<Account> grantorAccount = accountRepository.findById(grantorAccountNumber);

            if (granteeAccount.isEmpty() || grantorAccount.isEmpty()) {
                return ResponseEntity.badRequest().build();
            } else {
                accountAccessRepository.save(accountAccess);
                return ResponseEntity.accepted().build();
            }

        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
