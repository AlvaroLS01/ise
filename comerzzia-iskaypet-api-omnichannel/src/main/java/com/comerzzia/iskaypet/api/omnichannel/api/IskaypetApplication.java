package com.comerzzia.iskaypet.api.omnichannel.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// Esta configuracion elimina el control de errores por defecto de spring mvc
// que provocaba que por ejemplo un 401 (Unauthorized) se devolvia como un 404. 
// Esto se debe a que una aplicacion web tendria que redirigir a la pagina que 
// controlara esa situacion. Esto no procede en una aplicacion que solo maneja REST
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@ImportResource({"classpath*:comerzzia-*context.xml"})
@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableTransactionManagement

public class IskaypetApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(IskaypetApplication.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(IskaypetApplication.class);
    }
		
}
