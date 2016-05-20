package com.nalaan.kotimiliar

import org.springframework.data.cassandra.repository.Query
import org.springframework.data.repository.CrudRepository

interface CustomerRepository : CrudRepository<Customer, String> {

	@Query("Select * from customer where firstname=?0")
	fun findByFirstName(firstName: String): Customer

	@Query("Select * from customer where lastname=?0")
	fun findByLastName(lastName: String): List<Customer>
}
