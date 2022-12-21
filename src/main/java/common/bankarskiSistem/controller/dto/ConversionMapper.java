package common.bankarskiSistem.controller.dto;

import common.bankarskiSistem.model.Conversion;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ConversionMapper {
    ConversionMapper INSTANCE = Mappers.getMapper(ConversionMapper.class);

    @Named(value = "convertToDTO")
    ConversionDTO convertToDTO(Conversion conversion);

    @InheritInverseConfiguration(name = "convertToDTO")
    Conversion convertToEntity(ConversionDTO ConversionDTO);

    @Named(value = "convertToDTOShow")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="idConversion", target="idConversion")
    @Mapping(source="currencyFrom", target="currencyFrom")
    @Mapping(source="currencyTo", target="currencyTo")
    @Mapping(source="value", target="value")
    ConversionDTO convertToDTOShow(Conversion conversion);

}
