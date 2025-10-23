package app.DAO;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.entities.WorkoutProgram;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class ProgramDAOTest {

    public static EntityManagerFactory emf;
    public static ApplicationConfig app;
    public static ProgramDAO programDAO;



    @BeforeAll
    static void startUp() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        app = ApplicationConfig.startServer(7076);
        RestAssured.baseURI = "http://localhost:7076/api/v1";
        programDAO = new ProgramDAO(emf);
    }

    @BeforeEach
        void cleanDatabase() {
            programDAO.readAll().forEach(p -> programDAO.delete(p.getId()));
        }


    @Test
    void create() {

        //Entitet
        WorkoutProgram wp = WorkoutProgram.builder()
                .name("Upper Body")
                .description("Bryst, Ryg, Skulder, Arme")
                .items(null)
                .build();

        //Tester create endpoint

        //Henter id fra extract() så vi kan teste findById
        int id =
        given()
                .contentType("application/json")
                .body(wp)
                .when()
                .post("/program")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("Upper Body"))
                .extract()
                .path("id");

            //Tester findById endpoint
        given()
                .contentType("application/json")
                .body(wp)
                .when()
                .get("/program/" + id)
                .then()
                .statusCode(200)
                .body("description", equalTo("Bryst, Ryg, Skulder, Arme"));

    }

    @Test
    void readAll() {

        //Entitet 1
        WorkoutProgram wp1 = WorkoutProgram.builder()
                .name("Skulder")
                .description("Delts og sådan")
                .items(null)
                .build();

        //Entitet 2
        WorkoutProgram wp2 = WorkoutProgram.builder()
                .name("Ben")
                .description("Lægmuskler og sådan")
                .items(null)
                .build();

        //POST af entity 1
        given()
                .contentType("application/json")
                .body(wp1)
                .when()
                .post("/program")
                .then()
                .statusCode(200);

        //POST af entity 2
        given()
                .contentType("application/json")
                .body(wp2)
                .when()
                .post("/program")
                .then()
                .statusCode(200);

        //Tester om der er 2 eller flere entiteter
        given()
                .when()
                .get("/program")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(2));




    }

    @Test
    void update() {

        //Entitet 1
        WorkoutProgram wp1 = WorkoutProgram.builder()
                .name("Skulder")
                .description("Delts og sådan")
                .items(null)
                .build();

        //POST af Entitet 1
        int id =
        given()
                .contentType("application/json")
                .body(wp1)
                .when()
                .post("/program")
                .then()
                .statusCode(200)
                        .extract()
                                .path("id");

        //Tjekker om name passer sammen
        given()
                .when()
                .get("/program/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Skulder"));

        //Ændre navn
        wp1.setName("Sideskulder");

        //Opdaterer ændringen og ser om name matcher det nye
        given()
                .contentType("application/json")
                .body(wp1)
                .when()
                .put("/program/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Sideskulder"));



    }

    @Test
    void delete() {

        //Entitet 1
        WorkoutProgram wp1 = WorkoutProgram.builder()
                .name("Skulder")
                .description("Delts og sådan")
                .items(null)
                .build();


        //POST af Entitet 1
        int id =
                given()
                        .contentType("application/json")
                        .body(wp1)
                        .when()
                        .post("/program")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("id");

        //DELETE af Entitet 1
        given()
        .when()
                .delete("/program/" + id)
                .then()
                .statusCode(204);




    }
}