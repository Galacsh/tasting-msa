import org.gradle.internal.os.OperatingSystem
import org.gradle.nativeplatform.platform.internal.Architectures

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
}

group = "com.galacsh"
version = "0.0.1"

springBoot {
    buildInfo {
        properties {
            name = project.name
            group = project.group.toString()
            version = project.version.toString()
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.retry:spring-retry")
    implementation(platform(libs.spring.cloud.dependencies))
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway") {
        // Replace the library with the appropriate one for the architecture if it is macOS
        exclude(group = "io.netty", module = "netty-resolver-dns-native-macos")
        if (OperatingSystem.current().isMacOsX) {
            val currentArch = System.getProperty("os.arch").lowercase()
            val isAppleSilicon = Architectures.AARCH64.isAlias(currentArch)

            implementation("io.netty:netty-resolver-dns-native-macos") {
                artifact {
                    classifier = if (isAppleSilicon) "osx-aarch_64" else "osx-x86_64"
                }
            }
        }
    }
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

// do not generate `-plain.jar`
tasks.jar {
    enabled = false
}