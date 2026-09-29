plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    // --- Web layer (inbound REST adapter) ---
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // --- Observability ---
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // --- API documentation ---
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    // --- Resilience (will matter once the publisher adapter talks to Kafka) ---
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // --- Mapping (domain <-> DTO at the adapter boundary) ---
    implementation("org.mapstruct:mapstruct:1.6.2")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.2")

    // --- Lombok — infrastructure/DTO layer only, never in domain/application ---
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // --- Tests ---
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
    testImplementation("org.testcontainers:junit-jupiter:1.20.1")
    testCompileOnly("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.3.4")
    }
}
