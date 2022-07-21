package nl.rabobank.account;

import lombok.Value;
import org.springframework.data.annotation.Id;

/**
 * User Account Model.
 */
@Value
public class Account {

    @Id
    String accountNumber;
    String accountHolderName;
    Double balance;
    AccountType accountType;

}
