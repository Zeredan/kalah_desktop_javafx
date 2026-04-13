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

    implementation(project(":domain:settings"))
    implementation(project(":data:settings_d"))
    implementation(project(":core:ui"))
}

tasks.test {
    useJUnitPlatform()
}

