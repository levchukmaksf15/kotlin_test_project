package com.testprojectkotlin.service.implementation

import com.testprojectkotlin.controller.dto.ClientDto
import com.testprojectkotlin.exception.ClientNotFoundException
import com.testprojectkotlin.exception.GenderNotDetectedException
import com.testprojectkotlin.exception.NotUniqueEmailException
import com.testprojectkotlin.model.entity.Client
import com.testprojectkotlin.repository.ClientRepository
import com.testprojectkotlin.service.ClientService
import com.testprojectkotlin.service.FeignClient
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ClientServiceImpl(val clientRepository: ClientRepository,
                        val feignClient: FeignClient) : ClientService {

    override fun addClient(clientDto: ClientDto): ClientDto {
        if (clientRepository.existsByEmail(clientDto.email)) {
            throw NotUniqueEmailException("New client's email is not unique")
        }

        val genderResponse = feignClient.getGender(clientDto.firstName)
        if (genderResponse.gender == null || genderResponse.probability < 0.8) {
            throw GenderNotDetectedException("Gender not detected")
        }

        val client = clientDto.toClientEntity(genderResponse.gender)

        return clientRepository.save(client).toClientDto()
    }

    override fun updateClient(id: String, clientDto: ClientDto): ClientDto {
        val savedClient = clientRepository.findById(id)
        val savedClientByEmail = clientRepository.findByEmail(clientDto.email)

        if (savedClient.isEmpty) {
            throw ClientNotFoundException("Client with the id $id was not found.")
        }

        if (savedClientByEmail.isPresent && savedClientByEmail.get().id != id) {
            throw NotUniqueEmailException("New client's email is not unique")
        }

        val client = savedClient.get()
        clientDto.updateClientEntity(client)

        return clientRepository.save(client).toClientDto()
    }

    override fun deleteClient(id: String) {
        if (!clientRepository.existsById(id)) {
            throw ClientNotFoundException("Client with the id $id was not found.")
        }

        clientRepository.deleteById(id)
    }

    override fun getAllClients(pageNumber: Int, pageSize: Int): Page<ClientDto> {
        val clientPage = clientRepository.findAll(PageRequest.of(pageNumber, pageSize))

        return clientPage.map { it.toClientDto() }
    }

    override fun getClient(id: String): ClientDto {
        val client = clientRepository.findById(id)

        if (client.isEmpty) {
            throw ClientNotFoundException("Client with the id $id was not found.")
        }

        return client.get().toClientDto()
    }

    private fun ClientDto.toClientEntity(gender: String) = Client(
        firstName =  firstName,
        lastName = lastName,
        email = email,
        job = job,
        position = position,
        gender = gender
    )

    private fun ClientDto.updateClientEntity(client: Client) {
        client.email = email
        client.job = job
        client.firstName = firstName
        client.lastName = lastName
        client.position = position
    }

    private fun Client.toClientDto() = ClientDto(
        firstName =  firstName,
        lastName = lastName,
        email = email,
        job = job,
        position = position,
        gender = gender,
        id = id
    )
}