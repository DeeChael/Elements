import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
}

group = "net.deechael"
version = properties["elements-version"]!!

allprojects {
    apply {
        plugin("java")
        plugin("org.jetbrains.kotlin.jvm")
    }

    repositories {
        mavenCentral()
        maven {
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
    }

    dependencies {
        compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    tasks.withType<ProcessResources>().configureEach {
        inputs.property("version", rootProject.version)
        filesMatching(listOf("plugin.yml", "paper-plugin.yml")) {
            expand(
                mapOf(
                    Pair("version", rootProject.version)
                )
            )
        }
    }
}