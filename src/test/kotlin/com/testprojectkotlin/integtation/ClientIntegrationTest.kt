package com.testprojectkotlin.integtation

import com.fasterxml.jackson.databind.ObjectMapper
import com.testprojectkotlin.controller.dto.ClientDto
import com.testprojectkotlin.repository.ClientRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ClientIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper
    @Autowired
    private lateinit var clientRepository: ClientRepository

    @Test
    @Order(1)
    fun getAllClients_thereAreNoClients_returnEmptyList() {
        mockMvc.perform(get("/api/v1/client")
            .param("pageNumber", "0")
            .param("pageSize", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0]").doesNotExist())
    }

    @Test
    @Order(2)
    fun addClient_correctData_returnClientAndStatusOk() {
        val clientRequestDto = ClientDto(
            firstName = "Mark",
            lastName = "Smith",
            email = "smith-test@gmail.com",
            job = "andersen",
            position = "developer",
        )

        mockMvc.perform(post("/api/v1/client")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(clientRequestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(clientRequestDto.firstName))
            .andExpect(jsonPath("$.lastName").value(clientRequestDto.lastName))
            .andExpect(jsonPath("$.email").value(clientRequestDto.email))
            .andExpect(jsonPath("$.job").value(clientRequestDto.job))
            .andExpect(jsonPath("$.position").value(clientRequestDto.position))
            .andExpect(jsonPath("$.gender").value("male"))
    }

    @Test
    @Order(3)
    fun addClient_notUniqueEmail_returnBadRequest() {
        val clientRequestDto = ClientDto(
            firstName = "Mark",
            lastName = "Smith",
            email = "smith-test@gmail.com",
            job = "andersen",
            position = "developer",
        )

        mockMvc.perform(post("/api/v1/client")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(clientRequestDto)))
            .andExpect(status().isNotAcceptable)
            .andExpect(jsonPath("$.status").value(406))
            .andExpect(jsonPath("$.message").value("New client's email is not unique"))
    }

    @Test
    @Order(4)
    fun addClient_genderCantBeDetected_returnBadRequest() {
        val clientRequestDto = ClientDto(
            firstName = "wrongtestname",
            lastName = "Smith",
            email = "smith2test@gmail.com",
            job = "andersen",
            position = "developer",
        )

        mockMvc.perform(post("/api/v1/client")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(clientRequestDto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Gender not detected"))
    }

    @Test
    @Order(5)
    fun getClientById_correctData_returnClientAndStatusOk() {
        val id = getClientId()

        val clientRequestDto = ClientDto(
            firstName = "Mark",
            lastName = "Smith",
            email = "smith-test@gmail.com",
            job = "andersen",
            position = "developer",
        )

        mockMvc.perform(get("/api/v1/client/$id")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(clientRequestDto.firstName))
            .andExpect(jsonPath("$.lastName").value(clientRequestDto.lastName))
            .andExpect(jsonPath("$.email").value(clientRequestDto.email))
            .andExpect(jsonPath("$.job").value(clientRequestDto.job))
            .andExpect(jsonPath("$.position").value(clientRequestDto.position))
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.gender").value("male"))
    }

    @Test
    @Order(6)
    fun getClientById_clientNotFound_returnNotFound() {
        val id = UUID.randomUUID()

        mockMvc.perform(get("/api/v1/client/$id")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("Client with the id $id was not found."))
    }

    @Test
    @Order(7)
    fun getAllClients_correctData_returnClientsAndStatusOk() {
        val clientRequestDto = ClientDto(
            firstName = "Mark",
            lastName = "Smith",
            email = "smith-test@gmail.com",
            job = "andersen",
            position = "developer",
        )

        mockMvc.perform(get("/api/v1/client")
            .param("pageNumber", "0")
            .param("pageSize", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].firstName").value(clientRequestDto.firstName))
            .andExpect(jsonPath("$.content[0].lastName").value(clientRequestDto.lastName))
            .andExpect(jsonPath("$.content[0].email").value(clientRequestDto.email))
            .andExpect(jsonPath("$.content[0].job").value(clientRequestDto.job))
            .andExpect(jsonPath("$.content[0].position").value(clientRequestDto.position))
            .andExpect(jsonPath("$.content[0].gender").value("male"))
    }

    @Test
    @Order(8)
    fun deleteClient_correctData_returnClientAndStatusOk() {
        val id = getClientId()

        mockMvc.perform(delete("/api/v1/client/$id"))
            .andExpect(status().isOk())
            .andExpect(content().string("Client with id $id was deleted successfully"))
    }

    @Test
    @Order(9)
    fun deleteClient_clientNotFound_returnNotFound() {
        val id: UUID = UUID.randomUUID()

        mockMvc.perform(delete("/api/v1/client/$id"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("Client with the id $id was not found."))
    }

    private fun getClientId(): UUID? {
        val client = clientRepository.findByEmail("smith-test@gmail.com")
        if (client.isPresent) {
            return client.get().id
        }

        return null
    }
}