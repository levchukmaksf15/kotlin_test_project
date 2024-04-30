package com.testprojectkotlin.service

import com.testprojectkotlin.controller.dto.ClientDto
import org.springframework.data.domain.Page
import java.util.*

interface ClientService {

    /**
     * Method for add new client.
     *
     * @param clientDto new client information
     * @return added client
     */
    fun addClient(clientDto: ClientDto): ClientDto

    /**
     * Method for update client information.
     *
     * @param clientDto new client information
     * @param id client id
     * @return updated client
     */
    fun updateClient(id: UUID, clientDto: ClientDto): ClientDto

    /**
     * Method for delete client by id.
     *
     * @param id client id
     */
    fun deleteClient(id: UUID)

    /**
     * Method for getting list of clients
     * @param pageNumber page number
     * @param pageSize size og page
     * @return page of clients
     */
    fun getAllClients(pageNumber: Int, pageSize: Int): Page<ClientDto>

    /**
     * Method for getting list of clients by first name and last name
     * @param pageNumber page number
     * @param pageSize size og page
     * @param firstName client first name of part of it
     * @param lastName client last name of part of it
     * @return list of clients
     */
    fun getClientsByFirstNameAndLastName(pageNumber: Int, pageSize: Int, firstName: String, lastName: String): Page<ClientDto>

    /**
     * Method for getting client by id
     *
     * @return client information
     */
    fun getClient(id: UUID): ClientDto
}