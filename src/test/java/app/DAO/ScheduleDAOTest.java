package app.DAO;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleDAOTest {

    public static EntityManagerFactory emf;
    public static ApplicationConfig app;
    public static ScheduleDAO scheduleDAO;

    @BeforeAll
    static void startUp() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        app = ApplicationConfig.startServer(7076);
        RestAssured.baseURI = "http://localhost:7076/api/v1";
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