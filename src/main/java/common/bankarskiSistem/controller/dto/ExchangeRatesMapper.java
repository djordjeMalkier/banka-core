package common.bankarskiSistem.controller.dto;

import common.bankarskiSistem.model.ExchangeRates;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExchangeRatesMapper {
    ExchangeRatesMapper INSTANCE = Mappers.getMapper(ExchangeRatesMapper.class);

    @Named(value = "convertToDTO")
    ExchangeRatesDTO convertToDTO(ExchangeRates exchangeRates);

    @InheritInverseConfiguration(name = "convertToDTO")
    ExchangeRates convertToEntity(ExchangeRatesDTO exchangeRatesDTO);

}
