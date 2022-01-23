package localhost.routes

import io.ktor.application.*
import io.ktor.routing.*
import localhost.views.addCustomer
import localhost.views.allCustomers
import localhost.views.deleteCustomer

fun Application.customerRoutes(){
    routing {
        deleteCustomer()
        allCustomers()
        addCustomer()
    }
}

