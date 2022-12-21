package common.bankarskiSistem.controller;


import common.bankarskiSistem.BankarskiSistem;
import common.bankarskiSistem.controller.dto.BankAccountDTO;
import common.bankarskiSistem.controller.dto.BankAccountMapper;
import common.bankarskiSistem.controller.dto.UserDTO;
import common.bankarskiSistem.controller.dto.UserMapper;
import common.bankarskiSistem.exceptions.EntityAlreadyExistsException;
import common.bankarskiSistem.exceptions.EntityNotFoundException;
import common.bankarskiSistem.model.BankAccount;
import common.bankarskiSistem.model.Currency;
import common.bankarskiSistem.model.User;
import common.bankarskiSistem.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping(value = "/users", method = RequestMethod.GET)
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(BankarskiSistem.class);
    private final UserService userService;
    private final UserMapper mapUser = UserMapper.INSTANCE;
    private final BankAccountMapper mapBankAccount = BankAccountMapper.INSTANCE;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDto) {
        User savedUser;
        try {
            User user = mapUser.userDTOtoUser(userDto);
            savedUser = userService.saveUser(user);

        } catch (EntityAlreadyExistsException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
        return ok(mapUser.userToUserDTOShow(savedUser));
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUserById(@RequestParam String id) {
        User user;
        try {
            user = userService.getUserByPersonalID(id);
            return ok(mapUser.userToUserDTOShow(user));
        } catch (NullPointerException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @GetMapping(value = "/getBankAccountBalance")
    public ResponseEntity<String> getAccountBalance(@RequestParam String personalId,
                                      @RequestParam Integer idAccount,
                                      @RequestParam(required = false) Optional<Currency> currency) {
        try{
            double balance = userService.getBalance(personalId, idAccount, currency);
            String currencyName = currency.isPresent() ? currency.get().toString() : "";
            return ok(balance + " " + currencyName);
        } catch (EntityNotFoundException |
                 NullPointerException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @GetMapping(value = "/getAllBalance")
    public ResponseEntity<String> getAllBankAccountBalance(@RequestParam String personalId,
                                      @RequestParam(required = false) Optional<Currency> currency) {
        try{
            double balance = userService.getAllBalance(personalId, currency);
            String currencyName = currency.isPresent() ? currency.get().toString() : "EUR";
            return ok(balance + " " + currencyName);
        } catch (NullPointerException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @PutMapping("/payIn")
    public ResponseEntity<Double> payInUserBankAccount(@RequestParam String personalId,
                                                       @RequestParam Integer idAccount,
                                                       @RequestParam Double payment) {
        try{
            double balance = userService.payIn(personalId, idAccount, payment);
            return ok(balance);
        } catch (NullPointerException |
                 ArithmeticException |
                 EntityNotFoundException |
                 IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @PutMapping("/payOut")
    public ResponseEntity<Double> payOutUserBankAccount(@RequestParam String personalId,
                                                       @RequestParam Integer idAccount,
                                                       @RequestParam Double payment) {
        try{
            double balance = userService.payOut(personalId, idAccount, payment);
            return ok(balance);
        } catch (NullPointerException |
                 ArithmeticException |
                 EntityNotFoundException |
                 IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @PutMapping("/transfer")
    public ResponseEntity<String> transferFromUserAccountToAnotherUserAccount(@RequestParam String personalId,
                                                        @RequestParam Integer idAccountFrom,
                                                        @RequestParam Integer idAccountTo,
                                                        @RequestParam Double payment) {
        try{
            double transferMoney = userService.transfer(personalId, idAccountFrom, idAccountTo, payment);
            return ok("Successful transfer of " + transferMoney);
        } catch (NullPointerException |
                 ArithmeticException |
                 EntityNotFoundException |
                 IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDto) {
        User savedUser;
        try {
            User user = mapUser.userDTOtoUser(userDto);
            savedUser = userService.updateUser(user);
        } catch (EntityNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
        return ok(mapUser.userToUserDTOShow(savedUser));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<UserDTO> deleteUserById(@RequestParam String id) {
        User user;
        try {
            user = userService.deleteUserById(id);
            return ok(mapUser.userToUserDTOShow(user));
        } catch (EntityNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @PostMapping("/addBankAccount")
    public ResponseEntity<BankAccountDTO> addBankAccount(@RequestBody BankAccountDTO bankAccountDTO) {
        BankAccount savedBankAccount;
        try {
            BankAccount bankAccount = mapBankAccount.DTOToEntity(bankAccountDTO);
            savedBankAccount = userService.createBankAccount(bankAccount);
        } catch (EntityAlreadyExistsException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
        return ok(mapBankAccount.entityToDTOShow(savedBankAccount));
    }

    @DeleteMapping("/deleteBankAccount")
    public ResponseEntity<BankAccountDTO> deleteBankAccount(
            @RequestParam String personalId,
            @RequestParam Integer bankAccountId
    ) {
        BankAccount deletedBankAccount = null;
        try {
            deletedBankAccount = userService.deleteBankAccountById(personalId, bankAccountId);

        } catch (NullPointerException exception) {

            return badRequest().build();
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ok(mapBankAccount.entityToDTOShow(deletedBankAccount));
    }

    @DeleteMapping("/deleteAllBankAccounts")
    public ResponseEntity<List<BankAccountDTO>> deleteAllBankAccounts(@RequestParam String personalId) {
        User user = null;
        List<BankAccount> bankAccounts = new ArrayList<>();
        try {
            user = userService.getUserByPersonalID(personalId);
            bankAccounts = user.getBankAccounts();

            if(!bankAccounts.isEmpty()){
                for (BankAccount bankAccount: bankAccounts){
                    userService.deleteBankAccountById(personalId, bankAccount.getIdAccount());
                }
            }

        } catch (NullPointerException exception) {
            return badRequest().build();
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }

        return ok(mapBankAccount.entityListToDTOShow(bankAccounts));
    }

    @GetMapping("/getAllBankAccounts")
    public ResponseEntity<List<BankAccountDTO>> getAllBankAccounts(@RequestParam String personalId) {
        User user = null;
        try{
            user = userService.getUserByPersonalID(personalId);
            List<BankAccount> bankAccounts = user.getBankAccounts();
            return ok(mapBankAccount.entityListToDTOShow(bankAccounts));
        } catch (NullPointerException exception) {
            return notFound().build();
        }
    }

    @GetMapping("/getBankAccount")
    public ResponseEntity<BankAccountDTO> getBankAccount(
            @RequestParam String personalId,
            @RequestParam Integer accountId
    ) {
        User user = null;
        try{
            user = userService.getUserByPersonalID(personalId);
            log.info(user.toString());

            List<BankAccount> bankAccounts = user.getBankAccounts();
            for(BankAccount account : bankAccounts){
                if (account.getIdAccount().equals(accountId)) {
                    return ok(mapBankAccount.entityToDTOShow(account));
                }
            }
        } catch (NullPointerException exception) {
            return notFound().build();
        }
        return null;
    }

}
