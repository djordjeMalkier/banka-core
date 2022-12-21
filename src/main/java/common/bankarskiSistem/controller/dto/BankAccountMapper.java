package common.bankarskiSistem.controller.dto;

import common.bankarskiSistem.model.BankAccount;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(mappingInheritanceStrategy = MappingInheritanceStrategy.EXPLICIT)
public interface BankAccountMapper {
    BankAccountMapper INSTANCE = Mappers.getMapper(BankAccountMapper.class);

    @Named(value = "entityToDTO")
    BankAccountDTO entityToDTO(BankAccount bankAccount);

    @InheritInverseConfiguration(name = "entityToDTO")
    BankAccount DTOToEntity(BankAccountDTO bankAccountDTO);

    @Named(value = "entityToDTOShow")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "idAccount", target = "idAccount")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "accountType", target = "accountType")
    BankAccountDTO entityToDTOShow(BankAccount bankAccount);

    @IterableMapping(qualifiedByName = "entityToDTOShow")
    List<BankAccountDTO> entityListToDTOShow(List<BankAccount> accountDTOS);
}
