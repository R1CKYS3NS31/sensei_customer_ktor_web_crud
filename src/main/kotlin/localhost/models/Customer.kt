package localhost.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@Serializable
data class Customer(val cId:Int,val cFirstName:String,val cLastName:String,val cEmail:String)

//val customer = mutableListOf<Customer>()

object Customers:Table(){
    fun toCustomer(row: ResultRow):Customer= Customer(
        cId = row[cId],
        cFirstName = row[cFirstName],
        cLastName = row[cLastName],
        cEmail = row[cEmail]
    )

    val cId: Column<Int> = integer("cId").autoIncrement()
    val cFirstName:Column<String> = varchar("cFirstName",15)
    val cLastName:Column<String> = varchar("cLastName",15)
    val cEmail:Column<String> = varchar("cEmail",50)
    override val primaryKey=PrimaryKey(cId,name = "pk_$tableName")
}
