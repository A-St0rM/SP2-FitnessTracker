package app.controllers;

import app.DAO.ExerciseDAO;
import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.dtos.UserDTO;
import app.entities.User;
import app.enums.Role;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    public static EntityManagerFactory emf;
    public static ExerciseDAO exerciseDAO;
    public static ApplicationConfig app;

    @BeforeAll
    static void startUp(){
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        app = ApplicationConfig.startServer(7076);
        RestAssured.baseURI = "http://localhost:7076/api/v1";
        exerciseDAO = new ExerciseDAO(emf);
    }

    @Test
    void create(){
        User user = new User("dromme@burger.com", "pw");

        //Create
        String email =
                given()
                        .contentType("application/json")
                        .body(user)
                        .when()
                        .post("/auth/register")
                        .then()
                        .statusCode(201)
                        .body("id", notNullValue())
                        .body("email", equalTo("dromme@burger.com"))
                        .extract()
                        .path("email");


        //getByEmail
        given()
                .contentType("application/json")
                .body(user)
                .when()
                .get("/user/" + email)
                .then()
                .statusCode(200)
                .body("email", equalTo("dromme@burger.com"))
                .body("roles", hasItem("USER"));




    }

    @Test
    void login(){
        User u = new User("kungfu@may.dk", "mypassword");

        given()
                .contentType("application/json")
                .body(u)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201);

        // Lav login-request med samme email/password
        given()
                .contentType("application/json")
                .body(u)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("email", equalTo("kungfu@may.dk"))
                .body("token", notNullValue());
    }

    @Test
    void updateUser() {
        User u = new User("asger@mail.dk", "pw");


        // create
        given()
                .contentType("application/json")
                .body(u)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201);

        UserDTO updated = UserDTO.builder()
                .email("rnd@me.com")
                .password("newpw")
                .roles(Set.of(Role.ADMIN))
                .build();

        //update
        given()
                .contentType("application/json")
                .body(updated)
                .when()
                .put("/user/asger@mail.dk")
                .then()
                .statusCode(200)
                .body("email", equalTo("rnd@me.com"))
                .body("password", notNullValue())
                .body("roles[0]", equalTo("ADMIN"));
    }

    @Test
    void deleteUser() {
        User u = new User("rdsaekrf@eeeeemail.com", "pw");

        //Create
        int id =
                given()
                        .contentType("application/json")
                        .body(u)
                        .when()
                        .post("/auth/register")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");
        //Delete
        given()
                .when()
                .delete("/user/" + id)
                .then()
                .statusCode(200)
                .body(equalTo("User deleted"));

        //Tjek
        given()
                .when()
                .get("/user/rdsaekrf@eeeeemail.com")
                .then()
                .statusCode(404);

    }

    @Test
    void getAllUsers() {
        User u1 = new User("u1@u1.dk", "1234", Role.USER);
        User u2 = new User("u2@u2.dk", "1234", Role.ADMIN);

        given()
                .contentType("application/json")
                .body(u1)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201);

        given()
                .contentType("application/json")
                .body(u2)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/user")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(2));
    }
}


