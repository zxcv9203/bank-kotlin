package org.example.bankkotlin.user.domain

import jakarta.persistence.*
import org.example.bankkotlin.account.domain.Account
import java.time.LocalDateTime

@Entity
@Table(name = "user")
class User(
    @Id
    @Column(name = "ulid", length = 12, nullable = false)
    val ulid: String,

    @OneToMany(mappedBy = "user")
    val accounts: List<Account> = mutableListOf(),

    @Column(name = "username", nullable = false, unique = true, length = 50)
    val username: String,

    @Column(name = "access_token", length = 255)
    val accessToken: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
)