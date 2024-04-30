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
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

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

    override fun updateClient(id: UUID, clientDto: ClientDto): ClientDto {
        val clientOptional = clientRepository.findById(id)

        if (clientOptional.isEmpty) {
            throw ClientNotFoundException("Client with the id $id was not found.")
        }

        if (clientRepository.existsByEmailAndNotWithId(clientDto.email, id) == 1L) {
            throw NotUniqueEmailException("New client's email is not unique.")
        }

        val client = clientOptional.get()
        clientDto.updateClientEntity(client)

        return clientRepository.save(client).toClientDto()
    }

    override fun deleteClient(id: UUID) {
        val existById = clientRepository.existsById(id)
        if (!existById) {
            throw ClientNotFoundException("Client with the id $id was not found.")
        }

        clientRepository.deleteById(id)
    }

    override fun getAllClients(pageNumber: Int, pageSize: Int): Page<ClientDto> {
        val clientPage = clientRepository.findAll(PageRequest.of(pageNumber, pageSize))

        return clientPage.map { it.toClientDto() }
    }

    override fun getClientsByFirstNameAndLastName(
        pageNumber: Int,
        pageSize: Int,
        firstName: String,
        lastName: String
    ): Page<ClientDto> {
        val clientList = clientRepository.findAllByPartsOfFirstNameAndLastName(
            firstName.lowercase(Locale.getDefault()),
            lastName.lowercase(Locale.getDefault()),
            pageNumber,
            pageSize)

        val clientPage = PageImpl(clientList)

        return clientPage.map { it.toClientDto() }
    }

    override fun getClient(id: UUID): ClientDto {
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