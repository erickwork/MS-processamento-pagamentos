package br.com.erick.processamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProcessamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessamentoApplication.class, args);
	}


}
