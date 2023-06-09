import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class LoginUser(val id: Int?=null, val email: String, val password: String, val userType: String)

class LoginService(private val database: Database) {
    object LoginUsers : IntIdTable() {
        val email = varchar("email", length = 50)
        val password = varchar("password", length = 250)
        val userType = varchar("user_type", length = 50)
    }

    init {
        transaction(database) {
            SchemaUtils.create(LoginUsers)

            val loginUserList = arrayListOf<LoginUser>()
            loginUserList.add(LoginUser(1,"user@test.com", "test", "user"))
            loginUserList.add(LoginUser(2,"test2@test.com", "test", "user"))
            loginUserList.add(LoginUser(3,"test3@test.com", "test", "user"))
            loginUserList.add(LoginUser(4,"test4@test.com", "test", "user"))
            loginUserList.add(LoginUser(5,"test5@test.com", "test", "user"))
            loginUserList.add(LoginUser(6,"test6@test.com", "test", "user"))
            loginUserList.add(LoginUser(7,"test7@test.com", "test", "user"))
            loginUserList.add(LoginUser(8,"hospital@test.com", "test", "hospital"))


            LoginUsers.batchInsert(loginUserList) {
                loginUser ->
                this[LoginUsers.email] = loginUser.email
                this[LoginUsers.password] = loginUser.password
                this[LoginUsers.userType] = loginUser.userType
            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(loginUser: LoginUser) : Int = dbQuery {
        LoginUsers.insertAndGetId{
            it[email] = loginUser.email
            it[password] = loginUser.password
            it[userType] = loginUser.userType
        }.value
    }

     suspend fun validateCredentials(email: String?, password: String?, userType: String?) : LoginUser? {
        if (email != null && password != null && userType != null) {
            val rs = dbQuery {
                LoginUsers.select {
                    LoginUsers.email eq email and (LoginUsers.password eq password and (LoginUsers.userType eq userType))
                }.singleOrNull()
            }
            return if (rs != null) {
                LoginUser(
                    rs[LoginUsers.id].value,
                    rs?.get(LoginUsers.email) ?: "",
                    rs?.get(LoginUsers.password) ?: "",
                    rs?.get(LoginUsers.userType) ?: ""
                )
            } else null
        } else {
            return null
        }


    }



}

