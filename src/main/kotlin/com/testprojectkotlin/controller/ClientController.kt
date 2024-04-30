package com.testprojectkotlin.controller

import com.testprojectkotlin.controller.dto.ClientDto
import com.testprojectkotlin.exception.ErrorResponseBody
import com.testprojectkotlin.service.ClientService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ["/api/v1/client"])
class ClientController (val clientService: ClientService){

    @PostMapping
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Client was added successfully", content = [
            (Content(mediaType = "application/json", schema = Schema(implementation = ClientDto::class)))]),
        ApiResponse(responseCode = "400", description = "Gender not detected", content = [
            Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponseBody::class))]),
        ApiResponse(responseCode = "405", description = "New client's email is not unique", content = [
            Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponseBody::class))])]
    )
    fun addClient(@RequestBody clientDto: ClientDto): ResponseEntity<ClientDto> {
        return ResponseEntity.ok(clientService.addClient(clientDto))
    }

    @PatchMapping(path = ["/{id}"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Client was added successfully", content = [
            (Content(mediaType = "application/json", schema = Schema(implementation = ClientDto::class)))]),
        ApiResponse(responseCode = "404", description = "Client was not found by id", content = [
            Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponseBody::class))]),
        ApiResponse(responseCode = "405", description = "New client's email is not unique", content = [
            Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponseBody::class))])]
    )
    fun updateClientById(@PathVariable id: UUID, @RequestBody clientDto: ClientDto): ResponseEntity<ClientDto> {
        return ResponseEntity.ok(clientService.updateClient(id, clientDto))
    }

    @DeleteMapping(path = ["/{id}"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Client was deleted successfully", content = [(Content())]),
        ApiResponse(responseCode = "404", description = "Client was not found by id", content = [
            Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponseBody::class))])]
    )
    fun deleteClient(@PathVariable id: UUID): ResponseEntity<String> {
        clientService.deleteClient(id)
        return ResponseEntity.ok("Client with id $id was deleted successfully")
    }

    @GetMapping(path = ["/{id}"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Client was retrieved successfully", content = [
            (Content(mediaType = "application/json", schema = Schema(implementation = ClientDto::class)))]),
        ApiResponse(responseCode = "404", description = "Client was not found by id", content = [
            Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponseBody::class))])]
    )
    fun getClient(@PathVariable id: UUID): ResponseEntity<ClientDto> {
        return ResponseEntity.ok(clientService.getClient(id))
    }

    @GetMapping
    fun getAllClients(
        @RequestParam("pageNumber") pageNumber: Int,
        @RequestParam("pageSize") pageSize: Int
    ): ResponseEntity<Page<ClientDto>> {
        val page = clientService.getAllClients(pageNumber, pageSize)
        return ResponseEntity.ok(page)
    }

    @GetMapping("/search")
    fun advancedSearchByFirstNameAndLastName(
        @RequestParam("pageNumber") pageNumber: Int,
        @RequestParam("pageSize") pageSize: Int,
        @RequestParam("firstName") firstName: String,
        @RequestParam("lastName") lastName: String
    ): ResponseEntity<Page<ClientDto>> {
        return ResponseEntity.ok(clientService.getClientsByFirstNameAndLastName(
            pageNumber,
            pageSize,
            firstName,
            lastName))
    }
}