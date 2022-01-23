package localhost.views

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*
import localhost.database.Db
import localhost.models.Customers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.allCustomers(){

    transaction(Db.db){
//        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Customers)

        Customers.insert {
            it[cFirstName]="Ricky"
            it[cLastName] = "Sensei"
            it[cEmail]="sensei@gmail.com"
        }
        Customers.insert {
            it[cFirstName] = "Joseh"
            it[cLastName] = "Kamau"
            it[cEmail] = "kamau@gmail.com"
        }
        Customers.insert {
            it[cFirstName] = "Reuben"
            it[cLastName] = "Githua"
            it[cEmail] = "githua@gmail.com"
        }
    }
    route("/customers"){

        get {
            val customers = transaction {
                Customers.selectAll().map { Customers.toCustomer(it) }
            }
            if (customers.isNotEmpty()){
                call.respond(customers)
            }else{
                call.respondText("No customers found",status = HttpStatusCode.NotFound)
            }
        }
        get("/{id}"){
            val id = call.parameters["id"]?:return@get call.respondText("Missing or Malformed id",status = HttpStatusCode.BadRequest)
            val customer = transaction {
                Customers.select {
                    Customers.cId eq id.toInt()
                }.map { Customers.toCustomer(it) }
            }
//            val customer = customers.find { it.cId.equals(id) }?:return@get call.respondText("No customer of id: $id found",status = HttpStatusCode.NotFound)
            call.respond(customer)
        }
    }
}

fun Route.addCustomer(){
    get("/addCustomers"){
        call.respondHtml {
            head {
                meta {
                    charset = "UTF-8"
                }
                meta {
                    attributes["http-equiv"] = "X-UA-Compatible"
                    content = "IE=edge"
                }
                meta {
                    name = "viewport"
                    content = "width=device-width, initial-scale=1.0"
                }
                title { +"NDAMBUKI CONSOLIDATE" }
            }
            body {
                header {
                    style="text-align:centre"
                    h1 {
                        +"NDAMBUKI CONSOLIDATE"
                    }
                }
                div {
                    postForm("addCustomer"){
                        textInput (name="first_name"){
                            placeholder="first name"
                        }
                        textInput (name="last_name"){
                            placeholder="last name"
                        }
                        textInput (name="email"){
                            placeholder="email@gmai.com"
                        }
                        postButton (type = ButtonType.submit){ +"Add customer" }
                    }
                }
            }
        }
    }
    post ("/addCustomer"){
//        val customer = call.receive<Customer>()
        val params=call.receiveParameters()
        val cFirstName=params["first_name"]?:return@post call.respondText("first_name not found",status = HttpStatusCode.NotFound)
        val cLastName=params["last_name"]?:return@post call.respondText("last_name not found", status = HttpStatusCode.NotFound)
        val cEmail=params["email"]?:return@post call.respondText("email not found",status = HttpStatusCode.NotFound)
//        val customer=Customer(1,cFirstName,cLastName,cEmail)
//        val customer =

        transaction {
            Customers.insert {
                it[Customers.cFirstName] = cFirstName
                it[Customers.cLastName] = cLastName
                it[Customers.cEmail]=cEmail
            }
        }
//        localhost.database.customer.add(customer)
        call.respondText("Customer added successfully!",status = HttpStatusCode.Accepted)
    }
}
fun Route.deleteCustomer(){
    delete("/delete/{id}"){
        val id = call.parameters["id"]?:return@delete call.respondText("Missing or Malformed id",status = HttpStatusCode.BadRequest)
        transaction {
            Customers.deleteWhere {
                Customers.cId eq id.toInt()
            }
        }
//        ricky has bugs
        call.respondText("Customer of id: $id deleted",status = HttpStatusCode.Accepted)
    }
}
