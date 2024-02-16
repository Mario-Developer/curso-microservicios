package com.paymentchain.customer.business.transactions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;


@Service
public class MicroserviceComunication {
	
	
	private final WebClient.Builder webClientBuilder;
	
	public MicroserviceComunication(WebClient.Builder webClientBuilder) {
		this.webClientBuilder = webClientBuilder;
	}
	
    private TcpClient tcpClient = TcpClient
    		//Se crea una instancia de TcpClient configurada con un tiempo de espera de conexión de 5 segundos.
            .create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            
            //Se crean manejadores (ReadTimeoutHandler y WriteTimeoutHandler) para manejar los tiempos de espera de lectura y escritura respectivamente.
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });
    
    //Este método obtiene una lista de transacciones a través de un número de cuenta proporcionado.
    private <T> List<T> getTransactions(String accountNumber) {
    	
    	WebClient client = this.clientBuilder();
    	
    	List<T> transactions = this.request(client, accountNumber);
    	
        return transactions;
    }
    
    //Este método construye un cliente para realizar solicitudes HTTP.
    private WebClient clientBuilder() {
    	
    	//Se configura el cliente con un conector Reactor HTTP y se le asigna la configuración del TcpClient.
    	WebClient client = webClientBuilder.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
    			
    			//Se establece la URL base del servicio web.
                .baseUrl("http://businessdomain-transactions/transaction")
                
                //Se establecen los encabezados predeterminados. 
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                
                //Se establecen las variables de URI predeterminadas.
                .defaultUriVariables(Collections.singletonMap("url", "http://businessdomain-transactions/transaction"))
                
                //Se construye la instancia de WebClient.
                .build();
    	
		return client;
	}
    
    private <T> List<T> request(WebClient client, String accountNumber){
    	List<T> transactions = new ArrayList<>();
    	
    	//Se realiza una solicitud HTTP GET a la ruta /transactions con el parámetro de consulta accountNumber.
        List<Object> block = client.method(HttpMethod.GET)
        		.uri(uriBuilder -> uriBuilder
	                .path("/transactions")
	                .queryParam("accountNumber", accountNumber)
	                .build())
        		
        		//Se utiliza el método retrieve() para iniciar la recuperación de la respuesta.
                .retrieve()
                
                //Esta función se utiliza para transformar el cuerpo de la respuesta de una solicitud HTTP en un tipo específico de objeto llamado Flux.
                .bodyToFlux(Object.class).collectList()
                
                /* block() es una operación de bloqueo que espera hasta que la operación reactiva se complete y devuelve el resultado o lanza una 
                 * excepción si hay algún error. */
                .block();
        
        		/* La cadena de métodos realiza una solicitud HTTP GET, convierte el cuerpo de la respuesta en un Flux<Object>, recopila los elementos 
        		 * del Flux en una lista, y finalmente, block() se utiliza para esperar la finalización y obtener la lista resultante de transacciones.*/
        		 
        
        transactions = (List<T>) block;
        
        return transactions;
    }
}
