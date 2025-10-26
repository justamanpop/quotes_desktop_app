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
    implementation(libs.androidx.sqlite.bundled)
    implementation(libs.androidx.sqlite)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(24)
}