plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.deechael"
version = rootProject.version

dependencies {
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.7")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.3")
    implementation(project(":elements-api"))
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    archiveClassifier.set("")
}