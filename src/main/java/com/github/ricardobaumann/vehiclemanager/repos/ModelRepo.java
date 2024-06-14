package com.github.ricardobaumann.vehiclemanager.repos;

import com.github.ricardobaumann.vehiclemanager.entities.Model;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModelRepo extends CrudRepository<Model, UUID>, PagingAndSortingRepository<Model, UUID> {
    @Query("""
            select m from Model m
                join fetch m.brand
            where m.id = :id
            """)
    Optional<Model> findFullById(UUID id);
}
