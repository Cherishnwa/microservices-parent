plugins {
    java
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ca.gbc"
version = "0.0.1-SNAPSHOT"
description = "product-service"

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
    // IMPORTANT: TestContainers BOM must be FIRST in the dependencies block
    testImplementation(platform("org.testcontainers:testcontainers-bom:1.21.3"))
    // Existing dependencies...
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // ... rest of your dependencies
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // TestContainers Modules
    testImplementation("org.testcontainers:mongodb")
    testImplementation("org.testcontainers:junit-jupiter")
    // RestAssured for API testing
    testImplementation("io.rest-assured:rest-assured")
    // Additional testing utilities
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
