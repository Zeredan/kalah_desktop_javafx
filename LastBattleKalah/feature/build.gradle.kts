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

    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
    implementation(project(":usecases"))
}

tasks.test {
    useJUnitPlatform()
}