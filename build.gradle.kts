plugins {
    val kotlinVersion = "2.2.21"

    kotlin("jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.compose") version kotlinVersion
    id("org.jetbrains.compose") version "1.8.2"
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    // Production
    implementation("com.github.wendykierp:JTransforms:3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.1")
    implementation("org.greenrobot:eventbus-java:3.3.1")
    implementation("com.fazecast:jSerialComm:2.11.0")
    implementation("tools.jackson.module:jackson-module-kotlin:3.0.+")
    implementation(compose.desktop.currentOs)

    // Testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.0")
}

compose.desktop {
    application {
        mainClass = "MainKt"
        args += listOf()
    }
}

tasks.test { useJUnitPlatform() }