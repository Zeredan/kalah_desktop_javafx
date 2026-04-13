plugins {
    id("java")
}

group = "com.last_battle.kalah"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.google.code.gson:gson:2.10.1")

    implementation(project(":core:domain"))
    implementation(project(":data:auth_d"))
}

tasks.test {
    useJUnitPlatform()
}