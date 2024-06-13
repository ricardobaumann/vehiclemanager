package com.github.ricardobaumann.vehiclemanager.services;

import com.github.ricardobaumann.vehiclemanager.dtos.input.CreateBrandCommand;
import com.github.ricardobaumann.vehiclemanager.entities.Brand;
import com.github.ricardobaumann.vehiclemanager.exceptions.ResourceNotFoundException;
import com.github.ricardobaumann.vehiclemanager.mappers.BrandMapper;
import com.github.ricardobaumann.vehiclemanager.repos.BrandRepo;
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
public class BrandService {
    private final BrandRepo brandRepo;
    private final BrandMapper brandMapper;

    public Brand create(CreateBrandCommand createBrandCommand) {
        Brand brand = new Brand();
        brand.setId(UUID.randomUUID());
        brand.setName(createBrandCommand.name());
        return brandRepo.save(brand);
    }

    public Optional<Brand> getById(UUID id) {
        return brandRepo.findById(id);
    }

    public Page<Brand> list(Pageable pageable) {
        return brandRepo.findAll(pageable);
    }

    public void delete(UUID id) {
        brandRepo.deleteById(id);
    }

    public void update(UUID id, CreateBrandCommand createBrandCommand) {
        brandRepo.findById(id)
                .map(brand -> brandMapper.partialUpdate(brand, createBrandCommand))
                .ifPresentOrElse(brandRepo::save, () -> {
                    throw new ResourceNotFoundException();
                });
    }
}
