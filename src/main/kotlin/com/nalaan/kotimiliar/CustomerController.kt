package com.nalaan.kotimiliar

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/kotimiliar")
class CustomerController @Autowired constructor(val repository: CustomerRepository) {

	@RequestMapping("/")
	fun findAll() = repository.findAll()

	@RequestMapping("/{lastName}")
	fun findByLastName(@PathVariable lastName:String)
			= repository.findByLastName(lastName)
}