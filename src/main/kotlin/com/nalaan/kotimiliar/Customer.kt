package com.nalaan.kotimiliar

/**
 * Created by nam.vu on 2016/05/20.
 */

import com.datastax.driver.core.utils.UUIDs
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table
data class Customer (
        @PrimaryKey
        val id: UUID,
        val firstName: String,
        val lastName: String
)