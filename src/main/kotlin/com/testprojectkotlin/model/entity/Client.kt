package com.testprojectkotlin.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.*
import java.util.*

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client")
@Builder
data class Client (

    @Id
    @GeneratedValue
    var id: UUID? = null,

    var firstName: String,

    var lastName: String,

    var email: String,

    var gender: String? = null,

    var job: String? = null,

    var position: String? = null
)