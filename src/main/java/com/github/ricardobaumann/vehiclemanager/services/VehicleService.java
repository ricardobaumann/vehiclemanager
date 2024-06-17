package com.github.ricardobaumann.vehiclemanager.services;

import com.github.ricardobaumann.vehiclemanager.dtos.input.CreateVehicleCommand;
import com.github.ricardobaumann.vehiclemanager.entities.Vehicle;
import com.github.ricardobaumann.vehiclemanager.mappers.VehicleMapper;
import com.github.ricardobaumann.vehiclemanager.repos.VehicleRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class VehicleService {
    private final VehicleMapper vehicleMapper;
    private final VehicleRepo vehicleRepo;

    public Vehicle create(CreateVehicleCommand createVehicleCommand) {
        return vehicleRepo.save(vehicleMapper.map(createVehicleCommand));
    }

    public void update(UUID id, CreateVehicleCommand createVehicleCommand) {
        Vehicle vehicle = vehicleMapper.map(createVehicleCommand);
        vehicle.setId(id);
        vehicleRepo.save(vehicle);
    }

    public Optional<Vehicle> getByID(UUID id) {
        return vehicleRepo.findFullByID(id);
    }

    public Page<Vehicle> list(Pageable pageable) {
        return vehicleRepo.findWithModel(pageable);
    }

    public void delete(UUID id) {
        vehicleRepo.deleteById(id);
    }
}
