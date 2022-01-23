package localhost

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import localhost.plugins.*
import localhost.routes.customerRoutes

fun main() {
    embeddedServer(
        Netty,
        watchPaths = listOf("Ndambuki_Consolidate"),
        port = 8080,
        host = "0.0.0.0"
    ) {
        configureRouting()
        configureSerialization()
        configureTemplating()
        configureMonitoring()
        configureHTTP()
        configureSecurity()
        customerRoutes()
    }.start(wait = true)
}
