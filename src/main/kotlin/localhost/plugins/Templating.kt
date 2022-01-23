package localhost.plugins

import com.github.mustachejava.DefaultMustacheFactory
import io.ktor.mustache.Mustache
import io.ktor.mustache.MustacheContent
import io.ktor.html.*
import kotlinx.html.*
import freemarker.cache.*
import io.ktor.freemarker.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureTemplating() {
    install(Mustache) {
        mustacheFactory = DefaultMustacheFactory("templates/mustache")
    }
    
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    routing {
        get("/html-mustache") {
            call.respond(MustacheContent("index.hbs", mapOf("user" to MustacheUser(1, "user1"))))
        }
        get("/html-dsl") {
            call.respondHtml {
                body {
                    h1 { +"HTML" }
                    div {
                        ul {
                            for (n in 1..10) {
                                li { +"$n" }
                            }
                            input(InputType.text) {
                                value="username"
                            }
                            checkBoxInput {
                                checked=true
                            }
                    }
                    }
                }
            }
        }
        get("/html-freemarker") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("data" to IndexData(listOf(1, 2, 3))), ""))
        }
    }
}
data class MustacheUser(val id: Int, val name: String)
data class IndexData(val items: List<Int>)
