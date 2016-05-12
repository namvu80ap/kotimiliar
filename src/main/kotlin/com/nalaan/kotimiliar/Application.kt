package com.nalaan.kotimiliar

import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@SpringBootApplication
open class Application {

	private val log = LogManager.getLogger(Application::class.java)

//	@Bean
//	open fun objectMapperBuilder(): Jackson2ObjectMapperBuilder
//		= Jackson2ObjectMapperBuilder().modulesToInstall(KotlinModule())

	@Bean
	open fun init(repository: CustomerRepository) = CommandLineRunner {
		log.debug("This is log {} ", "nam log debug");
		log.info("This is log {} ", "nam log info");
		log.error("This is log {} ", "nam log error");
		log.fatal("This is log {} ", "nam log fatal");
		log.warn("This is log {} ", "nam log warning");
		log.trace("This is log {} ", "nam log trace");
		// save a couple of customers
		repository.save(Customer("Jack", "Bauer"))
		repository.save(Customer("Chloe", "O'Brian"))
		repository.save(Customer("Kim", "Bauer"))
		repository.save(Customer("David", "Palmer"))
		repository.save(Customer("Michelle", "Dessler"))

		// fetch all customers
		log.info("Customers found with findAll():")
		log.info("-------------------------------")
		for (customer in repository.findAll()) {
			log.info(customer.toString())
		}
		log.info("")

		// fetch an individual customer by ID
		val customer = repository.findOne(1L)
		log.info("Customer found with findOne(1L):")
		log.info("--------------------------------")
		log.info(customer.toString())
		log.info("")

		// fetch customers by last name
		log.info("Customer found with findByLastName('Bauer'):")
		log.info("--------------------------------------------")
		for (bauer in repository.findByLastName("Bauer")) {
			log.info(bauer.toString())
		}
		log.info("")
	}

}

fun main(args: Array<String>) {
	SpringApplication.run(Application::class.java, *args)
}
