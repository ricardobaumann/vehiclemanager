package com.github.ricardobaumann.vehiclemanager.mappers;

import com.github.ricardobaumann.vehiclemanager.dtos.input.CreateVehicleCommand;
import com.github.ricardobaumann.vehiclemanager.dtos.output.VehicleListResult;
import com.github.ricardobaumann.vehiclemanager.dtos.output.VehicleResult;
import com.github.ricardobaumann.vehiclemanager.entities.Vehicle;
import com.github.ricardobaumann.vehiclemanager.services.ModelService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ModelService.class})
public interface VehicleMapper {
    VehicleResult map(Vehicle vehicle);

    VehicleListResult mapToList(Vehicle vehicle);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "model", source = "modelId", qualifiedByName = "getModelOrFail")
    @Mapping(target = "createdAt", expression = "java(java.time.ZonedDateTime.now())")
    Vehicle map(CreateVehicleCommand createVehicleCommand);
}
