plugins {
    `java-library`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    compileOnly("org.springframework.boot:spring-boot-starter-web")
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}