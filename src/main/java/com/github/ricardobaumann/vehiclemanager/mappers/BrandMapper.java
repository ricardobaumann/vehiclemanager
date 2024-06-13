package com.github.ricardobaumann.vehiclemanager.mappers;

import com.github.ricardobaumann.vehiclemanager.dtos.input.CreateBrandCommand;
import com.github.ricardobaumann.vehiclemanager.dtos.output.BrandResult;
import com.github.ricardobaumann.vehiclemanager.entities.Brand;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandResult map(Brand brand);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Brand partialUpdate(@MappingTarget Brand brand, CreateBrandCommand createBrandCommand);
}
