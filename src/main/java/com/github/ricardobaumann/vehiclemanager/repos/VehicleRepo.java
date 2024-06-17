package com.github.ricardobaumann.vehiclemanager.repos;

import com.github.ricardobaumann.vehiclemanager.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepo extends CrudRepository<Vehicle, UUID>,
        PagingAndSortingRepository<Vehicle, UUID> {
    @Query("""
            select v from Vehicle v
                join fetch v.model m
                    join fetch m.brand
            where v.id = :id
            """)
    Optional<Vehicle> findFullByID(UUID id);

    @Query("""
            select v from Vehicle v
                join fetch v.model
            """)
    Page<Vehicle> findWithModel(Pageable pageable);
}
