package prosper.pets.resttest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.util.Map;

import static io.restassured.RestAssured.given;

public abstract class AbstractCachorrosApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://api.petsperto.com";
        RestAssured.port = 9000;
        RestAssured.basePath = "/pets";
    }

    protected RequestSpecification getRequisicaoUsuario() {
        return given().auth().basic("usuario1", "s1").contentType(ContentType.JSON);
    }

    protected RequestSpecification getRequisicaoAdmin() {
        return given().auth().basic("admin", "admin").contentType(ContentType.JSON);
    }

    protected Map getMapaDeJson(String arquivoJson) {
        return new JsonPath(getClass().getResource(arquivoJson)).getMap("$");
    }

}
