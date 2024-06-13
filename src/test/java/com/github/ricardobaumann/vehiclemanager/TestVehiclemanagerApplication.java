package com.github.ricardobaumann.vehiclemanager;

import org.springframework.boot.SpringApplication;

public class TestVehiclemanagerApplication {

	public static void main(String[] args) {
		SpringApplication.from(VehiclemanagerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
