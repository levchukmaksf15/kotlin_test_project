package com.testprojectkotlin.repository

import com.testprojectkotlin.model.entity.Client
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import java.util.*

@EnableScan
@EnableScanCount
interface ClientRepository : CrudRepository<Client, String> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Optional<Client>
    fun findAll(pageable: Pageable): Page<Client>
}