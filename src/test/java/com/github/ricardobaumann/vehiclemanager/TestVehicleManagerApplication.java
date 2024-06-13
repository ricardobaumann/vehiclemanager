package com.github.ricardobaumann.vehiclemanager;

import org.springframework.boot.SpringApplication;

public class TestVehicleManagerApplication {

	public static void main(String[] args) {
		SpringApplication.from(VehicleManagerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
