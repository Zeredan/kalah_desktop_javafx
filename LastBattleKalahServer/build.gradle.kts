plugins {
    java
    application
}

group = "com.last_battle.kalah_server"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Javalin web framework
    implementation("io.javalin:javalin:6.1.3")

    // PostgreSQL driver
    implementation("org.postgresql:postgresql:42.7.1")

    // BCrypt for password hashing
    implementation("org.mindrot:jbcrypt:0.4")

    // Jackson for JSON
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")

    // Logging
    implementation("org.slf4j:slf4j-simple:2.0.9")

    // HikariCP connection pool
    implementation("com.zaxxer:HikariCP:5.0.1")

    implementation("com.google.code.gson:gson:2.11.0")
}

application {
    mainClass.set("com.kalah.server.KalahServer")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.kalah.server.KalahServer"
    }

    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}