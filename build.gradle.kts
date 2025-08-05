plugins {
	java
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("org.projectlombok:lombok")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-rsocket")
	implementation("org.springframework.security:spring-security-rsocket")
	implementation("org.springframework.security:spring-security-messaging")
	testImplementation("io.projectreactor:reactor-test")
	annotationProcessor("org.projectlombok:lombok")
	implementation("org.apache.commons:commons-lang3:3.18.0")
	implementation("org.springframework.kafka:spring-kafka")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.mysql:mysql-connector-j")
	runtimeOnly("com.oracle.database.jdbc:ojdbc11")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.amqp:spring-rabbit-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
