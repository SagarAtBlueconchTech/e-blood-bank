package com.example.plugins

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction


@Serializable
data class Hospital(val name: String, val address: String, val email: String, val phone: String, val bloodGroup: String)

class HospitalService(private val database: Database) {
    object Hospitals : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val address = varchar("address", length = 250)
        val email = varchar("email", length = 250)
        val phone = varchar("phone", length = 250)
        val bloodGroup = varchar("blood_group", length = 250)

        override val primaryKey = PrimaryKey(Hospitals.id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Hospitals)

            val hospitalList = arrayListOf<Hospital>(
                Hospital("Sahyadri Hospital", "Pune", "sahyadri@test.com", "9858985898", "[A positive, B positive]"),
                Hospital("Jupiter Hospital", "Baner", "jupiter@test.com", "9858985898", "[A negative, B positive]"),
                Hospital("Ruby Hall Clinic", "Hinjewadi", "ruby@test.com", "9858985898", "[0 negative, B positive]")
            )

            Hospitals.batchInsert(hospitalList){hospital->
                this[Hospitals.name] = hospital.name
                this[Hospitals.address] = hospital.address
                this[Hospitals.email] = hospital.email
                this[Hospitals.phone] = hospital.phone
                this[Hospitals.bloodGroup] = hospital.bloodGroup
            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(hospital: Hospital): Int = dbQuery {
        Hospitals.insert() {
            it[name] = hospital.name
            it[address] = hospital.address
            it[email] = hospital.email
            it[phone] = hospital.phone
            it[bloodGroup] = hospital.bloodGroup
        }[Hospitals.id]
    }

    suspend fun getAll(): List<Hospital> {
        return dbQuery {
            Hospitals.selectAll().map {
                Hospital(
                    it[Hospitals.name],
                    it[Hospitals.address],
                    it[Hospitals.email],
                    it[Hospitals.phone],
                    it[Hospitals.bloodGroup])
            }
        }
    }

    suspend fun searchHospitalsByBloodGroupAndCity(bloodGroup: String?, city: String?): List<Hospital> {
        return dbQuery {
            Hospitals.selectAll()
                .apply {
                    bloodGroup?.let { s -> andWhere { Hospitals.bloodGroup.lowerCase() like "%${s.lowercase()}%" } }
                    city?.let { s -> andWhere { Hospitals.address.lowerCase() like "%${s.lowercase()}%" } }
                }.map { it.mapToHospital() }
        }
    }

    private fun ResultRow.mapToHospital(): Hospital {
        return Hospital(
            this[Hospitals.name], this[Hospitals.address], this[Hospitals.email],
            this[Hospitals.phone], this[Hospitals.bloodGroup]
        )
    }

}
