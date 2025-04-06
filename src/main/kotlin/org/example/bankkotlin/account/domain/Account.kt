package org.example.bankkotlin.account.domain

import jakarta.persistence.*
import org.example.bankkotlin.user.domain.User
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "account")
class Account(
    @Id
    @Column(name = "ulid", length = 50, nullable = false)
    val ulid: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_ulid", nullable = false)
    val user: User,

    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    var balance: BigDecimal = BigDecimal.ZERO,

    @Column(name = "account_number", length = 100, nullable = false, unique = true)
    val accountNumber: String,

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null,
) {
    fun delete() {
        this.isDeleted = true
        this.updatedAt = LocalDateTime.now()
        this.deletedAt = LocalDateTime.now()
    }
}