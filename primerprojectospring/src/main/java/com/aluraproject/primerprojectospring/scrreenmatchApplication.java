package com.aluraproject.primerprojectospring;

import com.aluraproject.primerprojectospring.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class scrreenmatchApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(scrreenmatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Principal principal = new Principal();
        principal.muestraElMenu();

    }

}
