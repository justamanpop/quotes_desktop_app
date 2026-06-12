plugins {
    kotlin("jvm")
    alias(libs.plugins.ksp)
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(project(":core"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(24)
}