plugins {
    val kotlinVersion = "2.2.21"

    kotlin("jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.compose") version kotlinVersion
    id("org.jetbrains.compose") version "1.8.2"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation("com.github.wendykierp:JTransforms:3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.1")
    implementation("org.greenrobot:eventbus-java:3.3.1")
    implementation(compose.desktop.currentOs)

    // Unit Testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation(platform("org.junit:junit-bom:6.0.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}

tasks.test {
    useJUnitPlatform()
}
