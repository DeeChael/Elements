plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.deechael"
version = rootProject.version

dependencies {
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.7")
    implementation(project(":elements-api"))
}