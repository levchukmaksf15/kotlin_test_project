package com.testprojectkotlin.service

import com.testprojectkotlin.controller.dto.ClientDto
import com.testprojectkotlin.controller.dto.FeignResponseDto
import com.testprojectkotlin.exception.ClientNotFoundException
import com.testprojectkotlin.exception.GenderNotDetectedException
import com.testprojectkotlin.exception.NotUniqueEmailException
import com.testprojectkotlin.model.entity.Client
import com.testprojectkotlin.repository.ClientRepository
import com.testprojectkotlin.service.implementation.ClientServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

@ExtendWith(MockitoExtension::class)
class ClientServiceTest {

    @InjectMocks
    lateinit var clientServiceImpl: ClientServiceImpl
    @Mock
    lateinit var clientRepository: ClientRepository
    @Mock
    lateinit var feignClient: FeignClient

    @Test
    fun addClient_correctData_returnNewClient() {
        val clientDto = ClientDto(
            firstName = "Mark",
            lastName = "Smith",
            email = "smith@gmail.com",
            job = "andersen",
            position = "developer",
        )

        val feignResponse = FeignResponseDto(
            count = 1,
            name = "Mark",
            gender = "male",
            probability = 0.9F
        )

        val clientToSave = Client(
            firstName = "Mark",
            lastName = "Smith",
            email = "smith@gmail.com",
            job = "andersen",
            position = "developer",
            gender = "male"
        )

        val savedClient = Client(
            firstName = "Mark",
            lastName = "Smith",
            email = "smith@gmail.com",
            job = "andersen",
            position = "developer",
            gender = "male",
            id = UUID.randomUUID().toString()
        )

        `when`(clientRepository.existsByEmail("smith@gmail.com")).thenReturn(false)
        `when`(clientRepository.save(clientToSave)).thenReturn(savedClient)
        `when`(feignClient.getGender("Mark")).thenReturn(feignResponse)

        val client = clientServiceImpl.addClient(clientDto)

        assertThat(client.gender).isEqualTo("male")
        assertThat(client.firstName).isEqualTo("Mark")
        assertThat(client.lastName).isEqualTo("Smith")
        assertThat(client.email).isEqualTo("smith@gmail.com")
        assertThat(client.job).isEqualTo("andersen")
        assertThat(client.position).isEqualTo("developer")
    }

    @Test
    fun addClient_userWithEmailExists_throwException() {
        val clientDto = ClientDto(
            firstName = "Mark",
            lastName = "Smith",
            email = "smith@gmail.com",
            job = "andersen",
            position = "developer",
        )

        `when`(clientRepository.existsByEmail("smith@gmail.com")).thenReturn(true)

        assertThrows<NotUniqueEmailException> ("New client's email is not unique.") {
            clientServiceImpl.addClient(clientDto)
        }
    }

    @Test
    fun addClient_genderCantBeDetected_throwException() {
        val clientDto = ClientDto(
            firstName = "testname",
            lastName = "Smith",
            email = "smith@gmail.com",
            job = "andersen",
            position = "developer",
        )

        val feignResponse = FeignResponseDto(
            count = 1,
            name = "testname",
            gender = "male",
            probability = 0.1F
        )

        `when`(clientRepository.existsByEmail("smith@gmail.com")).thenReturn(false)
        `when`(feignClient.getGender("testname")).thenReturn(feignResponse)

        assertThrows<GenderNotDetectedException> ("Gender not detected") {
            clientServiceImpl.addClient(clientDto)
        }
    }

    @Test
    fun deleteClient_correctData_deleteClient() {
        val id = UUID.randomUUID().toString()

        `when`(clientRepository.existsById(id)).thenReturn(true)

        clientServiceImpl.deleteClient(id)

        verify(clientRepository).deleteById(id)
        verify(clientRepository).existsById(id)
    }

    @Test
    fun deleteClient_clientNotFound_throwException() {
        val id = UUID.randomUUID().toString()

        `when`(clientRepository.existsById(id)).thenReturn(false)

        assertThrows<ClientNotFoundException> ("Client with the id $id was not found.") {
            clientServiceImpl.deleteClient(id)
        }
    }

    @Test
    fun gerClientById_correctData_returnClient() {
        val id = UUID.randomUUID().toString()

        val savedClient = Client(
            firstName = "Mark",
            lastName = "Smith",
            email = "smith@gmail.com",
            job = "andersen",
            position = "developer",
            gender = "male",
            id = id
        )

        `when`(clientRepository.findById(id)).thenReturn(Optional.of(savedClient))

        val client = clientServiceImpl.getClient(id)

        assertThat(client.gender).isEqualTo("male")
        assertThat(client.firstName).isEqualTo("Mark")
        assertThat(client.lastName).isEqualTo("Smith")
        assertThat(client.email).isEqualTo("smith@gmail.com")
        assertThat(client.job).isEqualTo("andersen")
        assertThat(client.position).isEqualTo("developer")
    }

    @Test
    fun getClient_clientNotFound_throwException() {
        val id = UUID.randomUUID().toString()

        `when`(clientRepository.findById(id)).thenReturn(Optional.empty())

        assertThrows<ClientNotFoundException> ("Client with the id $id was not found.") {
            clientServiceImpl.getClient(id)
        }
    }

    @Test
    fun gerAllClients_correctData_returnClients() {
        val savedClient = Client(
            firstName = "Mark",
            lastName = "Smith",
            email = "smith@gmail.com",
            job = "andersen",
            position = "developer",
            gender = "male",
            id = UUID.randomUUID().toString()
        )

        `when`(clientRepository.findAll(PageRequest.of(0, 1))).thenReturn(PageImpl(listOf(savedClient)))

        val clients = clientServiceImpl.getAllClients(0, 1)

        assertThat(clients.content[0].gender).isEqualTo("male")
        assertThat(clients.content[0].firstName).isEqualTo("Mark")
        assertThat(clients.content[0].lastName).isEqualTo("Smith")
        assertThat(clients.content[0].email).isEqualTo("smith@gmail.com")
        assertThat(clients.content[0].job).isEqualTo("andersen")
        assertThat(clients.content[0].position).isEqualTo("developer")
    }

    @Test
    fun gerAllClients_thereAteNoClients_returnEmptyList() {
        `when`(clientRepository.findAll(PageRequest.of(0, 1))).thenReturn(Page.empty())

        val clients = clientServiceImpl.getAllClients(0, 1)

        assertThat(clients.content).isEqualTo(listOf<Client>())
    }
}