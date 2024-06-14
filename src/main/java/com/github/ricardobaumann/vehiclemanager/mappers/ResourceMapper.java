package com.github.ricardobaumann.vehiclemanager.mappers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Component
public class ResourceMapper {

    public ResponseEntity<Void> fromId(UUID id) {
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path(id.toString()).build()
                        .toUri()).build();
    }

}
