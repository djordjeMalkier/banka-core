package common.bankarskiSistem.controller;

import common.bankarskiSistem.controller.dto.*;
import common.bankarskiSistem.exceptions.NameOfTheBankAlreadyExistException;
import common.bankarskiSistem.model.Bank;
import common.bankarskiSistem.model.ExchangeRates;
import common.bankarskiSistem.service.BankService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

import static org.springframework.http.ResponseEntity.ok;

@RequestMapping(value="/bank")
@RestController
@SecurityRequirement(name = "bearerAuth")
public class BankController {
    @Autowired
    private final BankService bankService;
    private final BankMapper mapper = BankMapper.INSTANCE;
    private final UserMapper mapperUser = UserMapper.INSTANCE;
    private final ExchangeRatesMapper mapperER = ExchangeRatesMapper.INSTANCE;

    @Autowired
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/add")
    public ResponseEntity<BankDto> saveBank(@RequestBody BankDto bankDto) {
        Bank savedBank;
        try {
            savedBank =  bankService.createBank(mapper.convertToEntity(bankDto));

        } catch (NullPointerException | NameOfTheBankAlreadyExistException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }

        return ok(mapper.convertToDTO(savedBank));
    }

    @PostMapping("/addExchangeRates")
    public ResponseEntity<BankDto> addExchangeRates(@RequestParam Integer idBank, @RequestBody ExchangeRatesDTO exchangeRatesDTO){
        Bank bank;
        try {
            bank = bankService.findById(idBank);
            bankService.addExchangeRates(mapperER.convertToEntity(exchangeRatesDTO).getIdExchangeRates(),bank);

        } catch (NullPointerException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);

        }

        return ok(mapper.convertToDTOShowER(bank));
    }

   @DeleteMapping("/delete")
    public String deleteBank(@RequestParam Integer idBank) {
        Bank bank;
        try {
            bank = bankService.findById(idBank);
            bankService.deleteBank(bank);
        } catch (NullPointerException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
        return "The bank is deleted successfully.";
    }

    @GetMapping("/get")
    public ResponseEntity<BankDto> getBankById(@RequestParam Integer idBank) {
        Bank bank;
        try {
            bank = bankService.findById(idBank);
        } catch (NullPointerException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
        return ok(mapper.convertToDTOShow(bank));
    }

    @PutMapping("/update")
    public ResponseEntity<BankDto> updateBank(@RequestBody BankDto updatedBank){
        Bank bank;
        try{
            bank = mapper.convertToEntity(updatedBank);
            return ok(mapper.convertToDTOShow(bankService.updateBank(bank)));

        } catch (NullPointerException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        } catch (NameOfTheBankAlreadyExistException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }
    @GetMapping("/getAllUsers")
    public ResponseEntity<Set<UserDTO>> getAllUsers(@RequestParam Integer idBank) {
        Set<UserDTO> users;
        try{
            users = mapperUser.userToUserDTOShow(bankService.getAllUsers(bankService.findById(idBank)));
            return ok(users);

            } catch (NullPointerException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @GetMapping("/getExchangeRates")
    public ResponseEntity<ExchangeRatesDTO> getExchangeRates(@RequestParam Integer idBank) {
        Bank bank;
        ExchangeRates exchangeRates;
        try{
            bank=bankService.findById(idBank);
            exchangeRates=bank.getExchangeRates();

        } catch (NullPointerException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
        return ok(mapperER.convertToDTO(exchangeRates));
    }

}
