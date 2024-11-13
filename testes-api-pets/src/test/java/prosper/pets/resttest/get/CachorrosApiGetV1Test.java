package prosper.pets.resttest.get;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.greaterThan;

public class CachorrosApiGetV1Test {

    @Test
    @DisplayName("GET /pets sem autenticação status 401")
    void testGetSemAutenticacao() {
        given().get("/pets").then()
                .statusCode(401);
    }

    @Test
    @DisplayName("GET /pets sem autorização status 403")
    void testGetSemAutorizacao() {
        given().auth().basic("hacker","hacker").get("/pets").then()
                .statusCode(403)
                .contentType(ContentType.JSON);
    }

    @Test
    @DisplayName("GET /pets com conteúdo status 200 e application/json")
    void testGetComConteudo() {
        given().auth().basic("usuario1","s1").get("/pets").then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0));
    }

    @Test
    @DisplayName("GET /pets sem conteúdo status 204 e sem corpo")
    void testGetSemConteudo() {
        given().auth().basic("usuario1", "s1").param("nome", "miojo")
                .get("/pets").then()
                .statusCode(204)
                .body(blankOrNullString());
    }

}
