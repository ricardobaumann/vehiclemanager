package com.github.ricardobaumann.vehiclemanager.mappers;

import com.github.ricardobaumann.vehiclemanager.dtos.input.CreateModelCommand;
import com.github.ricardobaumann.vehiclemanager.dtos.output.ModelResult;
import com.github.ricardobaumann.vehiclemanager.entities.Model;
import com.github.ricardobaumann.vehiclemanager.services.BrandService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {BrandMapper.class, BrandService.class})
public interface ModelMapper {
    ModelResult map(Model model);

    @Mapping(target = "brand", qualifiedByName = "getByIdOrFail", source = "brandId")
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    Model map(CreateModelCommand createModelCommand);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "brand", source = "brandId", qualifiedByName = "getByIdOrFail")
    Model partialUpdate(@MappingTarget Model model, CreateModelCommand createModelCommand);
}
