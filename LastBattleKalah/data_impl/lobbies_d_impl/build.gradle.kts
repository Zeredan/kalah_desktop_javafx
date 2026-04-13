plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "com.last_battle.kalah"
version = "1.0-SNAPSHOT"


javafx {
    version = "17"
    modules = listOf("javafx.base")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.google.code.gson:gson:2.10.1")

    implementation(project(":core:domain"))
    implementation(project(":data:lobbies_d"))
    implementation(project(":usecases"))
}

tasks.test {
    useJUnitPlatform()
}

