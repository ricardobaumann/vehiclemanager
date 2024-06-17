package com.github.ricardobaumann.vehiclemanager;

import com.github.ricardobaumann.vehiclemanager.entities.Brand;
import com.github.ricardobaumann.vehiclemanager.entities.FuelType;
import com.github.ricardobaumann.vehiclemanager.entities.Model;
import com.github.ricardobaumann.vehiclemanager.entities.Vehicle;
import com.github.ricardobaumann.vehiclemanager.repos.BrandRepo;
import com.github.ricardobaumann.vehiclemanager.repos.ModelRepo;
import com.github.ricardobaumann.vehiclemanager.repos.VehicleRepo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Component
public class TestObjects {
    private final BrandRepo brandRepo;
    private final ModelRepo modelRepo;
    private final VehicleRepo vehicleRepo;

    public TestObjects(BrandRepo brandRepo,
                       ModelRepo modelRepo,
                       VehicleRepo vehicleRepo) {
        this.brandRepo = brandRepo;
        this.modelRepo = modelRepo;
        this.vehicleRepo = vehicleRepo;
    }

    public Model createTestModel() {
        Brand brand = brandRepo.save(new Brand(UUID.randomUUID(), "brandfoo"));
        return modelRepo.save(new Model(UUID.randomUUID(), brand, "testing", new BigDecimal("100.99")));
    }

    public void cleanUp() {
        vehicleRepo.deleteAll();
        modelRepo.deleteAll();
        brandRepo.deleteAll();
    }

    public Vehicle createTestVehicle() {
        return vehicleRepo.save(
                new Vehicle(UUID.randomUUID(), createTestModel(), ZonedDateTime.now(),
                        2000, FuelType.GASOLINE, 2, "red")
        );
    }
}
