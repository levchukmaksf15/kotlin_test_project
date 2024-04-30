package com.testprojectkotlin.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.testprojectkotlin.controller.dto.ClientDto
import com.testprojectkotlin.exception.ClientExceptionHandler
import com.testprojectkotlin.exception.ClientNotFoundException
import com.testprojectkotlin.exception.GenderNotDetectedException
import com.testprojectkotlin.exception.NotUniqueEmailException
import com.testprojectkotlin.service.implementation.ClientServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.*

@ExtendWith(MockitoExtension::class)
class ClientControllerTest {

    @InjectMocks
    private lateinit var clientController: ClientController
    @Mock
    private lateinit var clientServiceImpl: ClientServiceImpl

    private lateinit var clientRequestDto: ClientDto
    private lateinit var clientDtoString: String
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun init() {
        val objectMapper = ObjectMapper().registerModule(JavaTimeModule())

        mockMvc = MockMvcBuilders
            .standaloneSetup(clientController)
            .setControllerAdvice(ClientExceptionHandler())
            .build()

        clientRequestDto = ClientDto(
            firstName = "Mark",
            lastName = "Smith",
            email = "smith@gmail.com",
            job = "andersen",
            position = "developer",
        )

        clientDtoString = objectMapper.writeValueAsString(clientRequestDto)
    }

    @Test
    fun addClient_correctData_returnClientAndStatusOk() {
        val clientResponseDto = ClientDto(
            id = UUID.randomUUID(),
            firstName = "Mark",
            lastName = "Smith",
            email = "smith@gmail.com",
            job = "andersen",
            position = "developer",
            gender = "male"
        )

        `when`(clientServiceImpl.addClient(clientRequestDto)).thenReturn(clientResponseDto)

        mockMvc.perform(post("/api/v1/client")
            .contentType(MediaType.APPLICATION_JSON)
            .content(clientDtoString))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(clientRequestDto.firstName))
            .andExpect(jsonPath("$.lastName").value(clientRequestDto.lastName))
            .andExpect(jsonPath("$.email").value(clientRequestDto.email))
            .andExpect(jsonPath("$.job").value(clientRequestDto.job))
            .andExpect(jsonPath("$.position").value(clientRequestDto.position))
    }

    @Test
    fun addClient_notUniqueEmail_returnBadRequest() {
        `when`(clientServiceImpl.addClient(clientRequestDto))
            .thenThrow(NotUniqueEmailException("New client's email is not unique."))

        mockMvc.perform(post("/api/v1/client")
            .contentType(MediaType.APPLICATION_JSON)
            .content(clientDtoString))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("New client's email is not unique."))
    }

    @Test
    fun addClient_genderCantBeDetected_returnBadRequest() {
        `when`(clientServiceImpl.addClient(clientRequestDto))
            .thenThrow(GenderNotDetectedException("Gender not detected"))

        mockMvc.perform(post("/api/v1/client")
            .contentType(MediaType.APPLICATION_JSON)
            .content(clientDtoString))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Gender not detected"))
    }

    @Test
    fun deleteClient_correctData_returnClientAndStatusOk() {
        val id: UUID = UUID.randomUUID()

        mockMvc.perform(delete("/api/v1/client/$id"))
            .andExpect(status().isOk())
            .andExpect(content().string("Client with id $id was deleted successfully"))

        verify(clientServiceImpl).deleteClient(id)
    }

    @Test
    fun deleteClient_clientNotFound_returnNotFound() {
        val id: UUID = UUID.randomUUID()

        `when`(clientServiceImpl.deleteClient(id))
            .thenThrow(ClientNotFoundException("Client with the id $id was not found."))

        mockMvc.perform(delete("/api/v1/client/$id"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("Client with the id $id was not found."))
    }

    @Test
    fun getClientById_correctData_returnClientAndStatusOk() {
        val id = UUID.randomUUID()

        val clientResponseDto = ClientDto(
            id = id,
            firstName = "Mark",
            lastName = "Smith",
            email = "smith@gmail.com",
            job = "andersen",
            position = "developer",
            gender = "male"
        )

        `when`(clientServiceImpl.getClient(id)).thenReturn(clientResponseDto)

        mockMvc.perform(get("/api/v1/client/$id")
            .contentType(MediaType.APPLICATION_JSON)
            .content(clientDtoString))
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
    fun getClientById_clientNotFound_returnNotFound() {
        val id = UUID.randomUUID()

        `when`(clientServiceImpl.getClient(id)).thenThrow(ClientNotFoundException("Client with the id $id was not found."))

        mockMvc.perform(get("/api/v1/client/$id")
            .contentType(MediaType.APPLICATION_JSON)
            .content(clientDtoString))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("Client with the id $id was not found."))
    }

    @Test
    fun getAllClients_correctData_returnClientsAndStatusOk() {
        val id = UUID.randomUUID()

        val clientResponseDto = ClientDto(
            id = id,
            firstName = "Mark",
            lastName = "Smith",
            email = "smith@gmail.com",
            job = "andersen",
            position = "developer",
            gender = "male"
        )

        `when`(clientServiceImpl.getAllClients()).thenReturn(listOf(clientResponseDto))

        mockMvc.perform(get("/api/v1/client")
            .contentType(MediaType.APPLICATION_JSON)
            .content(clientDtoString))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].firstName").value(clientRequestDto.firstName))
            .andExpect(jsonPath("$[0].lastName").value(clientRequestDto.lastName))
            .andExpect(jsonPath("$[0].email").value(clientRequestDto.email))
            .andExpect(jsonPath("$[0].job").value(clientRequestDto.job))
            .andExpect(jsonPath("$[0].position").value(clientRequestDto.position))
            .andExpect(jsonPath("$[0].id").value(id.toString()))
            .andExpect(jsonPath("$[0].gender").value("male"))
    }

    @Test
    fun getAllClients_thereAreNoClients_returnEmptyList() {
        `when`(clientServiceImpl.getAllClients()).thenReturn(listOf())

        mockMvc.perform(get("/api/v1/client")
            .contentType(MediaType.APPLICATION_JSON)
            .content(clientDtoString))
            .andExpect(status().isOk())
            .andExpect(content().string("[]"))
    }
}