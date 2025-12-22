package app.DAO;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleDAOTest {

    public static EntityManagerFactory emf;
    public static Javalin app;
    public static ScheduleDAO scheduleDAO;

    @BeforeAll
    static void startUp() {
        System.setProperty("env", "test");
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        app = ApplicationConfig.startServer(0);
        RestAssured.baseURI = "http://localhost:"+app.port()+"/api/v1";
        scheduleDAO = new ScheduleDAO(emf);
    }

    @BeforeEach
    void cleanDatabase() {
    }

    @Test
    void upsert() {
    }

    @Test
    void findByUser() {
    }
}