package com.gbm.rabbitmqcamel.common;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class BaseTests {

    static RabbitMQContainer rabbit = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.7.25-management-alpine"));

    @LocalServerPort
    public Integer port;

    @BeforeAll
    static void beforeAll() {
        rabbit.start();
    }

    @AfterAll
    static void afterAll() {
        rabbit.stop();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("app.rabbitmq.host", () -> rabbit.getAmqpUrl());
        registry.add("app.rabbitmq.username", () -> rabbit.getAdminUsername());
        registry.add("app.rabbitmq.password", () -> rabbit.getAdminPassword());
    }

}
