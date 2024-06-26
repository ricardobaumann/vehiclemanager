package com.github.ricardobaumann.vehiclemanager.services;

import com.github.ricardobaumann.vehiclemanager.dtos.input.CreateModelCommand;
import com.github.ricardobaumann.vehiclemanager.entities.Model;
import com.github.ricardobaumann.vehiclemanager.exceptions.ResourceNotFoundException;
import com.github.ricardobaumann.vehiclemanager.mappers.VehicleModelMapper;
import com.github.ricardobaumann.vehiclemanager.repos.ModelRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class ModelService {
    private final VehicleModelMapper vehicleModelMapper;
    private final ModelRepo modelRepo;

    public Model create(CreateModelCommand createModelCommand) {
        Model model = vehicleModelMapper.map(createModelCommand);
        return modelRepo.save(model);
    }

    public void update(UUID id, CreateModelCommand createModelCommand) {
        modelRepo.findById(id)
                .map(model -> vehicleModelMapper.partialUpdate(model, createModelCommand))
                .ifPresentOrElse(modelRepo::save, () -> {
                    throw new ResourceNotFoundException();
                });
    }

    @Named("getModelOrFail")
    public Model getModelOrFail(UUID id) {
        return getByID(id).orElseThrow(() -> new EntityNotFoundException("Model not found"));
    }

    public Optional<Model> getByID(UUID id) {
        return modelRepo.findFullById(id);
    }

    public Page<Model> list(Pageable pageable) {
        return modelRepo.findAllFull(pageable);
    }

    public void delete(UUID id) {
        modelRepo.deleteById(id);
    }
}
