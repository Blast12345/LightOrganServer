plugins {
    kotlin("jvm") version "1.8.0"
    id("org.jetbrains.compose") version "1.3.0"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation("com.github.wendykierp:JTransforms:3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation(compose.desktop.currentOs)

    // Unit Testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("io.mockk:mockk:1.13.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}

tasks.test {
    useJUnitPlatform()
}