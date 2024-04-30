package com.testprojectkotlin.service

import com.testprojectkotlin.controller.dto.ClientDto
import org.springframework.data.domain.Page

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
    fun updateClient(id: String, clientDto: ClientDto): ClientDto

    /**
     * Method for delete client by id.
     *
     * @param id client id
     */
    fun deleteClient(id: String)

    /**
     * Method for getting list of clients
     * @param pageNumber page number
     * @param pageSize size og page
     * @return page of clients
     */
    fun getAllClients(pageNumber: Int, pageSize: Int): Page<ClientDto>

    /**
     * Method for getting client by id
     *
     * @return client information
     */
    fun getClient(id: String): ClientDto
}