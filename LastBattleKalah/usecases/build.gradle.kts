
plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "com.last_battle.kalah"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "17"
    modules = listOf("javafx.controls", "javafx.graphics")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(project(":core:domain"))
    implementation(project(":core:ui"))
    implementation(project(":domain:settings"))
    implementation(project(":domain:auth"))
    implementation(project(":domain:users"))
    implementation(project(":domain:lobbies"))
    implementation(project(":domain:game"))
}

tasks.test {
    useJUnitPlatform()
}