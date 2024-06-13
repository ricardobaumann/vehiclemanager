package com.github.ricardobaumann.vehiclemanager.repos;

import com.github.ricardobaumann.vehiclemanager.entities.Brand;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BrandRepo extends CrudRepository<Brand, UUID> {
}
