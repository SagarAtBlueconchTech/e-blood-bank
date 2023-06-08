package com.example.plugins

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction


@Serializable
data class Hospital(val name: String, val address: String)

class HospitalService(private val database: Database) {
    object Hospitals : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val address = varchar("address", length = 250)

        override val primaryKey = PrimaryKey(Hospitals.id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Hospitals)

            val hospitalList = arrayListOf<Hospital>(
                Hospital("Sahyadri Hospital", "Pune"),
                Hospital("Jupiter Hospital", "Baner"),
                Hospital("Ruby Hall Clinic", "Hinjewadi")
            )

            Hospitals.batchInsert(hospitalList){hospital->
                this[Hospitals.name] = hospital.name
                this[Hospitals.address] = hospital.address
            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(hospital: Hospital): Int = dbQuery {
        Hospitals.insert() {
            it[name] = hospital.name
            it[address] = hospital.address
        }[Hospitals.id]
    }

    suspend fun getAll(): List<Hospital> {
        return dbQuery {
            Hospitals.selectAll().map {
                Hospital(it[Hospitals.name], it[Hospitals.address])
            }
        }
    }

}
