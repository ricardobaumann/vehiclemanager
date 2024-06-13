package com.github.ricardobaumann.vehiclemanager.mappers;

import com.github.ricardobaumann.vehiclemanager.dtos.output.BrandResult;
import com.github.ricardobaumann.vehiclemanager.entities.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandResult map(Brand brand);
}
