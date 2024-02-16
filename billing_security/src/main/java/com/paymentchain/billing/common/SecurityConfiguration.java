/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.billing.common;

import java.time.Duration;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 *
 * @author sotobotero
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	//Esta es una lista en donde se definen las url que no necesitarán autenticación.
	private static final String[] NO_AUTH_LIST = { "/v3/api-docs",
			"/configuration/ui", //
			"/swagger-resources", //
			"/configuration/security", //
			"/webjars/**", //
			"/login", "/h2-console/**" };

	//Anota el método para que Spring lo trate como un bean administrado por el contenedor de Spring.
	@Bean
	
	//Este método crea y devuelve un objeto SecurityFilterChain, que define la configuración de seguridad para la aplicación. 
	//La configuración de seguridad en Spring se realiza mediante el uso de la clase HttpSecurity.
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		/*
		//MÉTODO 1 - Define la configuración de seguridad de manera general.
		
		//Desactiva la protección CSRF (Cross-Site Request Forgery). CSRF es un tipo de ataque que aprovecha la confianza de un 
		//usuario autenticado en una aplicación para ejecutar acciones no autorizadas en su nombre. Al desactivar CSRF, la aplicación 
		//no requiere tokens CSRF para cada solicitud.
		http.csrf().disable()
		
		//Indica que se va a comenzar a configurar las reglas de autorización para las solicitudes HTTP.
		.authorizeHttpRequests()
		
		//Indica que cualquier solicitud debe estar autenticada. En otras palabras, se requiere autenticación para acceder a cualquier recurso de la aplicación.
		.anyRequest().authenticated()
		
		//Indica que la configuración actual ha finalizado y se está volviendo a la configuración principal (http).
		.and()
		
		//Configura la autenticación básica HTTP. Esto significa que los clientes deben proporcionar credenciales (nombre de usuario y contraseña) 
		//para acceder a los recursos protegidos. withDefaults() configura valores predeterminados para la autenticación básica proporcionados en
		//el archivo .properties.
		.httpBasic(withDefaults())
		
		//Configura la autenticación mediante un formulario de inicio de sesión. Esto significa que se proporcionará un formulario de inicio de sesión 
		//personalizado para la autenticación. withDefaults() configura valores predeterminados para la autenticación basada en formulario.
		.formLogin(withDefaults());

		//Devuelve el objeto http configurado como un SecurityFilterChain construido.
		return http.build();
		*/

		
		//MÉTODO 2 - Define la configuración de seguridad con restricciones específicas.
		http.csrf().disable()

		.authorizeHttpRequests((authz) -> authz
				// Permite el acceso sin autenticación a las rutas especificadas en NO_AUTH_LIST. Esto significa que las solicitudes a estas rutas no 
				//requieren autenticación.
				.antMatchers(NO_AUTH_LIST).permitAll()   
				
				//Requiere autenticación para todas las solicitudes HTTP POST a rutas que coincidan con el patrón "/billing/**". Esto significa que para 
				//realizar solicitudes POST a las rutas relacionadas con "billing", los usuarios deben estar autenticados.
				.antMatchers(HttpMethod.POST, "/*billing*/**").authenticated()
				
				//Requiere que los usuarios tengan el rol "ADMIN" para realizar solicitudes HTTP GET a rutas que coincidan con el patrón "/billing/**". 
				//Esto implica que solo los usuarios con el rol "ADMIN" pueden acceder a las rutas de lectura relacionadas con "billing".
				.antMatchers(HttpMethod.GET,"/*billing*/**").hasRole("ADMIN"))   
		
		.httpBasic(withDefaults())
		.formLogin(withDefaults());
		
		return http.build();
		
		
	}

	//Este método configura la política de Same-Origin Policy (SOP) para Cross-Origin Resource Sharing (CORS).
	//La configuración de CORS es importante cuando una aplicación del lado del cliente (por ejemplo, una aplicación web en JavaScript) 
	//desea realizar solicitudes a un servidor que está en un dominio diferente al de la aplicación cliente.
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration cc = new CorsConfiguration();

		//Define las cabeceras HTTP que la solicitud puede incluir
		cc.setAllowedHeaders(Arrays.asList("Origin,Accept", "X-Requested-With", "Content-Type",
				"Access-Control-Request-Method", "Access-Control-Request-Headers", "Authorization"));
		
		//Define las cabeceras HTTP que el navegador puede exponer al cliente después de recibir una respuesta del servidor.
		cc.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));

		//Define los orígenes permitidos para las solicitudes. En este caso, se permite cualquier origen debido al uso de "/*".
		cc.setAllowedOrigins(Arrays.asList("/*"));

		//Especifica los métodos HTTP permitidos en las solicitudes CORS.
		cc.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "PATCH"));

		//Permite cualquier origen mediante el uso de un patrón.
		cc.addAllowedOriginPattern("*");

		//Establece el tiempo máximo que una respuesta preflight puede ser almacenada en caché. Duration.ZERO significa que no se almacenará en caché.
		cc.setMaxAge(Duration.ZERO);
		
		//Permite que las solicitudes incluyan credenciales, como cookies o encabezados de autorización.
		cc.setAllowCredentials(Boolean.TRUE);
		
		//Crea una instancia de UrlBasedCorsConfigurationSource, que es una implementación de CorsConfigurationSource basada en patrones de URL.
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		
		//Registra la configuración CORS (cc) para aplicarla a todas las rutas ("/**").
		source.registerCorsConfiguration("/**", cc);
		return source;
	}

}
