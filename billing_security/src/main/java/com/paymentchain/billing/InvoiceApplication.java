package com.paymentchain.billing;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

//exclude = {} Permite excluir clases de la configuración básica que nos ofrece spring boot.
//SecurityAutoConfiguration.class: En este caso se excluirá la clase que proporciona Spring Security, la cual  tiene la configuración de seguridad por defecto.
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class InvoiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoiceApplication.class, args);

	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().components(new Components()).info(new Info().title("Billing API").version("1.0.0"));
	}

}
