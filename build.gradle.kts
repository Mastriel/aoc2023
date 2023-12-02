plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "dev.calathea"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("dev.calathea.aoc2023.MainKt")
}