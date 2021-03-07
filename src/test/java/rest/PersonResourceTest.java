/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PersonDTO;
import entities.Person;
import entities.RenameMe;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author Tweny
 */
public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Person person1, person2;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        person1 = new Person("First 1", "Last 1", "11111111");
        person2 = new Person("First 2", "Last 2", "22222222");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(person1);
            em.persist(person2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/person/all").then().statusCode(200);
    }

    //This test assumes the database contains two rows
    @Test
    public void testDummyMsg() throws Exception {
        given()
                .contentType("application/json")
                .get("/person/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", equalTo("Hello World"));
    }

    @Test
    public void doThisWhenYouHaveProblems() {
        given().log().all().when().get("/person/all").then().log().body();
    }

    @Test
    public void testAll() {
        given()
                .contentType("application/json")
                .get("/person/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", hasItems("First 1", "First 2"));
    }

    @Test
    public void testById1() {
        given().log().all().when().get("/person/id/{id}", person1.getId()).then().log().body();

        given()
                .contentType("application/json")
                .get("/person/id/{id}", person1.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo("First 1"));
    }

    @Test
    public void testById2() {
        given().log().all().when().get("/person/id/" + person2.getId()).then().log().body();

        given()
                .contentType("application/json")
                .get("/person/id/" + person2.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("lastName", equalTo(person2.getLast_name()));
    }

    //@Test
    public void testByTitle() {
        given().log().all().when().get("/person/bllbla/{title}", "a").then().log().body();

        given()
                .contentType("application/json")
                .get("/person/blabla/{title}", "a").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("title", hasItems(person1.getPhone()));
    }

    @Test
    public void testAllSize1() {
        given()
                .contentType("application/json")
                .get("/person/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("$", hasSize(2));
    }

    @Test
    public void testAllSize2() {
        given()
                .contentType("application/json")
                .get("/person/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("", hasSize(2));
    }

    @Test
    public void testAllSize3() {
        given()
                .contentType("application/json")
                .get("/person/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size()", equalTo(2));
    }

    @Test
    public void testAllSize4() {
        given()
                .contentType("application/json")
                .get("/person/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size()", equalTo(2))
                .body("", hasSize(2))
                .body("$", hasSize(2));
        ;
    }

    //For at denne test kan virke, så skal man lave en equals i i DTO klassen
    @Test
    /* Observe: You must override the equals method for MovieDTO for this to work */
    void testGetAllV2() {
        List<PersonDTO> personDTOs;
        personDTOs = given()
                .contentType("application/json")
                .when()
                .get("/person/all")
                .then()
                .extract().body().jsonPath().getList(".", PersonDTO.class);

        PersonDTO m1DTO = new PersonDTO(person1);
        PersonDTO m2DTO = new PersonDTO(person2);

        assertThat(personDTOs, containsInAnyOrder(m1DTO, m2DTO));
    }

    //******* POST *******
    @Test
    public void testPOSTrequest1() {
        String requestBody = "{\n"
                + "  \"firstName\": \"test1\",\n"
                + "  \"lastName\": \"teest1\",\n"
                + "  \"phone\": \"223344\" \n}";

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/person/post-dbtest")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("test1", response.jsonPath().getString("firstName"));
        Assertions.assertEquals("teest1", response.jsonPath().getString("lastName"));
        Assertions.assertEquals("223344", response.jsonPath().getString("phone"));
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testPOSTrequest2() {
        PersonDTO personDTO = new PersonDTO("Arik", "Twena", "777");
        String requestBody = GSON.toJson(personDTO);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/person/post-dbtest")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("Arik", response.jsonPath().getString("firstName"));
        Assertions.assertEquals("Twena", response.jsonPath().getString("lastName"));
        Assertions.assertEquals("777", response.jsonPath().getString("phone"));
    }

    @Test
    public void testPOSTrequest3() {
        PersonDTO personDTO = new PersonDTO("Bente", "Gaarde", "888");
        String requestBody = GSON.toJson(personDTO);

        given()
                .contentType("application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/person/post-dbtest")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .and()
                .body("firstName", equalTo("Bente"),
                        "lastName", equalTo("Gaarde"),
                        "phone", equalTo("888"));
    }

    //******* PUT *******
    @Test
    public void testPUTrequest1() {
        PersonDTO personDTOEditInfo = new PersonDTO("ChangedName", "ChangedLast", "888");
        Person personToEdit = new Person(person1.getId(), personDTOEditInfo.getFirstName(), personDTOEditInfo.getLastName(), personDTOEditInfo.getPhone());
        PersonDTO personDTO = new PersonDTO(personToEdit);
        String requestBody = GSON.toJson(personDTO);

        given()
                .contentType("application/json")
                .and()
                .body(requestBody)
                .when()
                .put("/person/change/{id}", person1.getId())
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .and()
                .body("firstName", equalTo("ChangedName"),
                        "lastName", equalTo("ChangedLast"),
                        "phone", equalTo("888"),
                        "id", equalTo(person1.getId()));

    }

    //******* DELETE *******
    @Test
    public void testDELETErequest1() {
        given()
                .contentType("application/json")
                .when()
                .delete("/person/delete/{id}", person1.getId())
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .and()
                .body("status", equalTo("removed"));
    }

    //***** ERROR ****
//    @Test
//    public void testError() {
//
//        //given().log().all().when().get("/person/id/" + "abc").then().log().body();
//
//        given()
//                .contentType("application/json")
//                .get("/person/id/" + "abc")
//                .then()
//                .assertThat()
//                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
//                .body("message", equalTo("No person with provided id found"));
//    }

    @Test
    public void testById3Error1() {
        int id = 435345344;
        given()
                .contentType("application/json")
                .get("/person/id/" + id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("message", equalTo(String.format("No person with provided id: (%d) found", id)));
    }
    
    @Test
    public void testById3Error2() {
        int id = 435345344;
        given()
                .contentType("application/json")
                .get("/person/id3/" + id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR_500.getStatusCode())
                .body("message", equalTo("Internal Server Error"));
    }

    @Test
    public void testPOSTrequestError() {
        String requestBody = "{\n"
                + "  \"firstName\": \"test1\",\n"
                + "  \"phone\": \"223344\" \n}";

        given()
                .contentType("application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/person/post-dbtest")
                .then()
                .log()
                .body();

        given()
                .contentType("application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/person/post-dbtest")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .and()
                .body("message", equalTo("First Name and/or Last Name is missing"));
    }

    @Test
    public void testPUTrequestError() {
        String requestBody = "{\n"
                + "  \"firstName\": \"test1\",\n"
                + "  \"phone\": \"223344\" \n}";

        given()
                .contentType("application/json")
                .and()
                .body(requestBody)
                .when()
                .put("/person/change/{id}", person1.getId())
                .then()
                .log()
                .body();

        given()
                .contentType("application/json")
                .and()
                .body(requestBody)
                .when()
                .put("/person/change/{id}", person1.getId())
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .and()
                .body("message", equalTo("First Name and/or Last Name is missing"));
    }

    @Test
    public void testDELETErequestError() {
        given()
                .contentType("application/json")
                .when()
                .delete("/person/delete/{id}", 435345)
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .and()
                .body("message", equalTo("Could not delete, provided id does not exist"));
    }

}
