package prosper.pets.resttest.get;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.blankOrNullString;

public class CachorrosApiGetV2Test {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://api.petsperto.com";
        RestAssured.port = 9000;
        RestAssured.basePath = "/pets";
    }

    RequestSpecification getRequisicao() {
        return given().auth().basic("usuario1", "s1");
    }

    @Test
    @DisplayName("GET /pets sem autenticação status 401")
    void testGetSemAutenticacao() {
        given().get().then()
                .statusCode(401);
    }

    @Test
    @DisplayName("GET /pets com conteúdo status 200 e application/json")
    void testGetComConteudo() {
        getRequisicao().get().then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    @DisplayName("GET /pets sem conteúdo status 204 e sem corpo")
    void testGetSemConteudo() {
        getRequisicao().param("nome", "miojo")
                .get().then()
                .statusCode(204)
                .body(blankOrNullString());
    }

}
