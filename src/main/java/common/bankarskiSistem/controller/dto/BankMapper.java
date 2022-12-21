package common.bankarskiSistem.controller.dto;

import common.bankarskiSistem.model.Bank;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BankMapper {
    BankMapper INSTANCE = Mappers.getMapper(BankMapper.class);

    @Named(value = "convertToDTO")
    BankDto convertToDTO(Bank bank);


    @InheritInverseConfiguration(name = "convertToDTO")
    Bank convertToEntity(BankDto bankDto);

    @IterableMapping(qualifiedByName = "convertToDTOShow")
    List<BankDto> convertToDTOShow(List<Bank> banks);

    @Named(value = "convertToDTOShow")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="idBank", target="idBank")
    @Mapping(source="name", target="name")
    @Mapping(source="address", target="address")
    BankDto convertToDTOShow(Bank bank);

    @Named(value = "convertToDTOShowER")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="idBank", target="idBank")
    @Mapping(source="name", target="name")
    @Mapping(source="address", target="address")
    @Mapping(source="exchangeRates", target="exchangeRates")
    BankDto convertToDTOShowER(Bank bank);
}
