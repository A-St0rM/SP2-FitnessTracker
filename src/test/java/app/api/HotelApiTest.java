package app.api;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

class HotelApiTest {

    private static Javalin app;
    private static EntityManagerFactory emf;
    private static ApplicationConfig appConfig;

    private int h1Id;
    private int h2Id;

    @BeforeAll
    static void setUpAll() {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();

        appConfig = ApplicationConfig.startServer(7777);

        RestAssured.baseURI = "http://localhost:7777/api/v1";
    }

    @AfterAll
    static void tearDownAll() {
        HibernateConfig.setTest(false);
        ApplicationConfig.stopServer(app);
    }

    @BeforeEach
    void seedDb() {
        // Fyld DB med 2 hoteller og få de faktiske IDs tilbage
        Map<String, Integer> ids = HotelPopulator.seedTwoHotels(emf);
        h1Id = ids.get("h1");
        h2Id = ids.get("h2");
    }

    @Test
    void testGetAllHotels() {
        given()
                .when().get("/hotel")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
    }

    @Test
    void testGetSpecificHotel() {
        given()
                .when().get("/hotel/" + h1Id)
                .then()
                .statusCode(200)
                .body("hotelName", equalTo("TestHotel1"))
                .body("hotelAddress", equalTo("Street 1"));
    }

    @Test
    void testGetRoomsForHotel() {
        given()
                .when().get("/hotel/" + h1Id + "/rooms")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))
                .body("[0].number", equalTo("101"))
                .body("[1].number", equalTo("102"));
    }

    @Test
    void testCreateHotel() {
        String json = """
          {
            "hotelName": "CreatedHotel",
            "hotelAddress": "New Street",
            "rooms": []
          }
          """;

        given()
                .contentType("application/json")
                .body(json)
                .when().post("/hotel")
                .then()
                .statusCode(201)
                .body("hotelName", equalTo("CreatedHotel"))
                .body("hotelAddress", equalTo("New Street"));
    }

    @Test
    void testUpdateHotel() {
        String json = """
          {
            "hotelName": "UpdatedHotel",
            "hotelAddress": "Updated Street",
            "rooms": []
          }
          """;

        given()
                .contentType("application/json")
                .body(json)
                .when().put("/hotel/" + h1Id)
                .then()
                .statusCode(200)
                .body("hotelName", equalTo("UpdatedHotel"))
                .body("hotelAddress", equalTo("Updated Street"));
    }

    @Test
    void testDeleteHotel() {
        // Slet det andet seeded hotel
        given()
                .when().delete("/hotel/" + h2Id)
                .then()
                .statusCode(200)
                .body(equalTo("Hotel deleted"));

        // Bekræft at det er væk
        given()
                .when().get("/hotel/" + h2Id)
                .then()
                .statusCode(404);
    }
}