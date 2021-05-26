package com.lexxsage.nanopost

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.lexxsage.nanopost.db.initDatabase
import com.lexxsage.nanopost.auth.*
import com.lexxsage.nanopost.routes.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.features.*
import io.ktor.request.ContentTransformationException
import io.ktor.utils.io.*

fun main(args: Array<String>) {
    val serviceAccount = Thread.currentThread().contextClassLoader
        .getResourceAsStream("nano-post-firebase-adminsdk-p696t-20a15cf609.json")
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setStorageBucket("nano-post.appspot.com")
        .build()
    FirebaseApp.initializeApp(options)

    io.ktor.server.cio.EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    initDatabase(testing)
    val auth = installAuth()

    install(ContentNegotiation) {
        json { ignoreUnknownKeys = true }
    }

    install(StatusPages) {
        exception<ContentTransformationException> {
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<NullPointerException> {
            call.respond(HttpStatusCode.BadRequest)
        }

        exception<BackendException> {
            call.respond(it.status, ErrorResponse(it.code))
        }

        exception<Throwable> {
            it.printStack()
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse(message = it.localizedMessage))
        }
    }

    routing {
        register(auth)
        login(auth)
        checkUsername()

        authenticate {
            getUser()
            getUserImages()
            getUserPosts()
            getUserSubscribers()
            getUserSubscribedTo()

            updateProfile()
            updatePassword()

            subscribeToUser()
            unsubscribeFromUser()

            createPost()
            getPost()
            getFeed()
            likePost()
            unlikePost()

            uploadImage()
            getImage()
        }
    }
}
