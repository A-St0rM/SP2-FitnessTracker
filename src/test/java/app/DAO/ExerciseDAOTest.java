package app.DAO;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.entities.Exercise;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;


import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseDAOTest {
    public static EntityManagerFactory emf;
    public static ExerciseDAO exerciseDAO;
    public static Javalin app;

    @BeforeAll
    static void startUp(){
        System.setProperty("env", "test");
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        app = ApplicationConfig.startServer(0);
        RestAssured.baseURI = "http://localhost:"+app.port()+"/api/v1";
        exerciseDAO = new ExerciseDAO(emf);
    }



    @BeforeEach
    void cleanDatabase() {
        exerciseDAO.readAll().forEach(e -> exerciseDAO.delete(e.getId()));
    }


    @Test
    void createAndFind() {
        Exercise e = new Exercise();
        e.setName("Pushups");
        e.setMuscleGroup("Chest");
        e.setEquipment("Bodyweight");
        e.setInstructions("Stiff body and let your arms push you up and down.");

        //create
        int id =
        given()
                .contentType("application/json")
                .body(e)
                .when()
                .post("/exercise")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Pushups"))
                        .extract()
                                .path("id");

        //getById
        given()
                .contentType("application/json")
                .body(e)
                .when()
                .get("/exercise/" + id)
                .then()
                .statusCode(200)
                .body("muscleGroup", equalTo("Chest"));



    }

    @Test
    void readAll() {
        Exercise e = new Exercise();
        e.setName("Pushups");
        e.setMuscleGroup("Chest");
        e.setEquipment("Bodyweight");
        e.setInstructions("Stiff body and let your arms push you up and down.");

        Exercise e1 = new Exercise();
        e1.setName("Dance");
        e1.setMuscleGroup("Jump");
        e1.setEquipment("Body");
        e1.setInstructions("Move like crazy");

        given()
                .contentType("application/json")
                .body(e)
                .when()
                .post("/exercise")
                .then()
                .statusCode(201);

        given()
                .contentType("application/json")
                .body(e1)
                .when()
                .post("/exercise")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/exercise")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(2));


    }

    @Test
    void delete() {
        Exercise e = new Exercise();
        e.setName("Pushups");
        e.setMuscleGroup("Chest");
        e.setEquipment("Bodyweight");
        e.setInstructions("Stiff body and let your arms push you up and down.");

        //create
        int id =
        given()
                .contentType("application/json")
                .body(e)
                .when()
                .post("/exercise")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Pushups"))
                        .extract()
                                .path("id");

        //delete
        given()
                .when()
                .delete("/exercise/" + id)
                .then()
                .statusCode(204)
                .body(equalTo("Exercise deleted"));

        //getById
        given()
                .contentType("application/json")
                .body(e)
                .when()
                .get("/exercise/" + id)
                .then()
                .statusCode(404)
                .body(equalTo("Exercise not found"));


    }

    @Test
    void update(){

        Exercise e = new Exercise();
        e.setName("Pushups");
        e.setMuscleGroup("Chest");
        e.setEquipment("Bodyweight");
        e.setInstructions("Stiff body and let your arms push you up and down.");

        //create
        int id =
        given()
                .contentType("application/json")
                .body(e)
                .when()
                .post("/exercise")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Pushups"))
                        .extract()
                                .path("id");

        //getById
        given()
                .contentType("application/json")
                .body(e)
                .when()
                .get("/exercise/" + id)
                .then()
                .statusCode(200)
                .body("muscleGroup", equalTo("Chest"));

        e.setName("Ballade");
        e.setInstructions("Sove");

        //update
        given()
                .contentType("application/json")
                .body(e)
                .when()
                .put("exercise/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Ballade"))
                .body("instructions", equalTo("Sove"));

        //getById
        given()
                .contentType("application/json")
                .body(e)
                .when()
                .get("/exercise/" + id)
                .then()
                .statusCode(200)
                .body("instructions", equalTo("Sove"))
                .body("muscleGroup", equalTo("Chest"));

    }
}