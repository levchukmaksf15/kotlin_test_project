package com.testprojectkotlin.repository

import com.testprojectkotlin.model.entity.Client
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
//import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

//@EnableScan
//interface ClientRepository : CrudRepository<Client, UUID> {
//    fun existsByEmail(email: String): Boolean
//}

@Repository
interface ClientRepository : CrudRepository<Client, UUID> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Optional<Client>
    @Query(value = """
        select count(1) from client cl
        where cl.id != :id
        and cl.email = :email
    """, nativeQuery = true)
    fun existsByEmailAndNotWithId(@Param("email") email: String, @Param("id") id: UUID): Long
    fun findAll(pageable: Pageable): Page<Client>
    @Query(value = """
        select * from client cl
        where lower(cl.first_name) like %:firstName%
        and lower(cl.last_name) like %:lastName%
        offset :pageNumber * :pageSize 
        limit :pageSize
    """, nativeQuery = true)
    fun findAllByPartsOfFirstNameAndLastName(@Param("firstName") firstName: String,
                                             @Param("lastName") lastName: String,
                                             @Param("pageNumber") pageNumber: Int,
                                             @Param("pageSize") pageSize: Int): List<Client>
}