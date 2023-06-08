package com.example.plugins

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*

@Serializable
data class User(val name: String, val email:String, val mobile:String, val age: Int, val bloodGroup: String, val address: String, val loginUserId: Int)
class UserService(private val database: Database) {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val email = varchar("email", length = 50)
        val mobile = varchar("mobile", length = 15)
        val age = integer("age")
        val bloodGroup = varchar("blood_group", 30)
        val address = varchar("address", 150)
        val loginUserId = integer("login_user_id")

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)

            val userList = arrayListOf<User>(
                User("John Dev", "john@test.com","9898989111",18, "A positive", "Pune", 1),
                User("Paula Sathey", "Paula@test.com","9898989222",23, "A positive", "Pune",2),
                User("Christian Walker", "Christian@test.com","9898989333",28, "AB positive", "Mumbai",3),
                User("James Walker", "James@test.com","9898989444",39, "AB negative", "Nagpur",4),
                User("Virat Kohli", "Virat@test.com","9898989555",35, "A negative", "Delhi",5),
                User("Sachin Tendulkar", "Sachin@test.com","9898989666",45, "B positive", "Mumbai",6),
                User("MS Dhoni", "MS@test.com","9898989777",48, "B negative", "Mumbai",7),
            )
            Users.batchInsert(userList) { user: User ->
                this[Users.name] = user.name
                this[Users.email] = user.email
                this[Users.mobile] = user.mobile
                this[Users.age] = user.age
                this[Users.bloodGroup] = user.bloodGroup
                this[Users.address] = user.address
                this[Users.loginUserId] = user.loginUserId
            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }


    suspend fun create(user: User): Int = dbQuery {
        Users.insert {
            it[name] = user.name
            it[email] = user.email
            it[mobile] = user.mobile
            it[age] = user.age
            it[bloodGroup] = user.bloodGroup
            it[address] = user.address
            it[loginUserId] = user.loginUserId
        }[Users.id]
    }

    suspend fun readAll() : List<User> {
        return dbQuery {
            Users.selectAll().map {it.mapToUser()}
        }
    }

    suspend fun read(id: Int): User? {
        return dbQuery {
            Users.select { Users.id eq id }
                .map{it.mapToUser()}
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, user: User) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[name] = user.name
                it[email] = user.email
                it[mobile] = user.mobile
                it[age] = user.age
                it[bloodGroup] = user.bloodGroup
                it[address] = user.address
                it[loginUserId] = user.loginUserId
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Users.deleteWhere { Users.id.eq(id) }
        }
    }

    suspend fun searchUsersByBloodGroupAndCity(bloodGroup: String?, city: String?): List<User> {
        return dbQuery {
            Users.selectAll()
                .apply {
                    bloodGroup?.let { s -> andWhere { Users.bloodGroup eq s } }
                    city?.let { s -> andWhere { Users.address.lowerCase() like "%${s.lowercase()}%" } }
                }.map { it.mapToUser() }
        }
    }

    private fun ResultRow.mapToUser():User {
        return User(this[Users.name], this[Users.email], this[Users.mobile], this[Users.age],
            this[Users.bloodGroup], this[Users.address], this[Users.loginUserId])
    }

}