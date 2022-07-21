package nl.rabobank.account;

import lombok.*;
import nl.rabobank.authorizations.Authorization;

/**
 * User account access model.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountAccess {

    String grantorAccountNumber;
    String granteeAccountNumber;
    Authorization authorization;

}
