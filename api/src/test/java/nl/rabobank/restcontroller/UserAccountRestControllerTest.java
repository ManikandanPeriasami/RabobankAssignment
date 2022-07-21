package nl.rabobank.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.account.Account;
import nl.rabobank.account.AccountAccess;
import nl.rabobank.account.AccountType;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.mongo.configuration.AccountAccessRepository;
import nl.rabobank.mongo.configuration.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserAccountRestController.class)
class UserAccountRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    AccountAccessRepository accountAccessRepository;

    @DisplayName("Fetch list of accounts based on account access")
    @org.junit.jupiter.api.Test
    void testGetAccountDetails() throws Exception {
        Mockito.when(accountAccessRepository.findByGranteeAccountNumber(anyString()))
                .thenReturn(List.of(new AccountAccess("101", "202", Authorization.READ)));
        Mockito.when(accountRepository.findAllById(anyList()))
                .thenReturn(List.of(new Account("100", "Mani", 120D, AccountType.SAVING)));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/{accountNumber}/", 102))
                .andExpect(status().isOk());
        Mockito.verify(accountAccessRepository, Mockito.times(1))
                .findByGranteeAccountNumber(anyString());
        Mockito.verify(accountRepository, Mockito.times(1))
                .findAllById(anyList());
    }

    @DisplayName("Fetch empty list of accounts for incorrect data")
    @org.junit.jupiter.api.Test
    void testGetAccountDetailsFetchEmptyList() throws Exception {
        Mockito.when(accountAccessRepository.findByGranteeAccountNumber(anyString()))
                .thenReturn(List.of(new AccountAccess("101", "202", Authorization.READ)));
        Mockito.when(accountRepository.findAllById(List.of(anyString())))
                .thenReturn(List.of(new Account("100", "Mani", 120D, AccountType.SAVING)));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/account/{accountNumber}/", 102))
                .andExpect(status().isBadRequest());
        Mockito.verify(accountAccessRepository, Mockito.times(1))
                .findByGranteeAccountNumber(anyString());
        Mockito.verify(accountRepository, Mockito.times(1))
                .findAllById(anyList());
    }

    @DisplayName("Provide permission for user account")
    @org.junit.jupiter.api.Test
    void testProvideAccountAccess() throws Exception {
        Mockito.when(accountRepository.findById(anyString()))
                .thenReturn(Optional.of(new Account("102", "Mani", 120D, AccountType.SAVING)))
                .thenReturn(Optional.of(new Account("103", "Mani2", 130D, AccountType.SAVING)));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/accountaccess" )
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(new AccountAccess("102", "103", Authorization.READ))))
                .andExpect(status().isAccepted());
    }

    @DisplayName("Provide permission for non existing user account")
    @org.junit.jupiter.api.Test
    void testProvideAccountAccessNonExistingUser() throws Exception {
        Mockito.when(accountRepository.findById(anyString()))
                .thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/accountaccess" )
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(new AccountAccess("102", "103", Authorization.READ))))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Provide permission for same user account")
    @org.junit.jupiter.api.Test
    void testProvideAccountAccessForSameUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/accountaccess" )
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(new AccountAccess("102", "102", Authorization.READ))))
                .andExpect(status().isBadRequest());
    }
}