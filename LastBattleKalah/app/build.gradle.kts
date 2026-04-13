plugins {
    id("java")
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "com.last_battle.kalah"
version = "1.0-SNAPSHOT"

application {
    mainClass = "com.test.kalah.Launcher"
}

javafx {
    version = "17"
    modules = listOf("javafx.controls", "javafx.graphics")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")


    implementation(project(":feature"))
    implementation(project(":feature:lobbies_feat"))
    implementation(project(":feature:lobby"))
    implementation(project(":feature:lobby_creation"))
    implementation(project(":feature:login"))
    implementation(project(":feature:main"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:register"))
    implementation(project(":feature:settings_feat"))
    implementation(project(":feature:top_players"))

    implementation(project(":usecases"))

    implementation(project(":domain:settings"))
    implementation(project(":domain:auth"))
    implementation(project(":domain:users"))
    implementation(project(":domain:lobbies"))

    implementation(project(":domain_impl:settings_impl"))
    implementation(project(":domain_impl:auth_impl"))
    implementation(project(":domain_impl:users_impl"))
    implementation(project(":domain_impl:lobbies_impl"))

    implementation(project(":data:settings_d"))
    implementation(project(":data:auth_d"))
    implementation(project(":data:users_d"))
    implementation(project(":data:lobbies_d"))

    implementation(project(":data_impl:settings_d_impl"))
    implementation(project(":data_impl:auth_d_impl"))
    implementation(project(":data_impl:users_d_impl"))
    implementation(project(":data_impl:lobbies_d_impl"))

    implementation(project(":core:ui"))


}

tasks.test {
    useJUnitPlatform()
}