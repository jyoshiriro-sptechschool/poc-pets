package prosper.pets.resttest.put;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import prosper.pets.resttest.AbstractCachorrosApiTest;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class CachorrosApiPutTest extends AbstractCachorrosApiTest {

    @Test
    @DisplayName("PUT /pets sem autenticação status 401")
    void testPutSemAutenticacao() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        given().contentType(ContentType.JSON).body(corpoValido)
                .put("/1").then()
                .statusCode(401);
    }

    @Test
    @DisplayName("PUT /pets sem autorização status 403")
    void testPutSemAutorizacao() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        getRequisicaoUsuario().contentType(ContentType.JSON).body(corpoValido)
                .put("/1").then()
                .statusCode(403);
    }


    @Test
    @DisplayName("PUT /pets com id inválido status 404")
    void testPutInexistente() {
        getRequisicaoAdmin().body(getMapaDeJson("/pet-valido.json"))
                .put("/-100").then()
                .statusCode(404);
    }

    @Test
    @DisplayName("PUT /pets com id válido status 200 e Pet atualizado no corpo")
    void testPutExistente() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        Map petCriado = getRequisicaoAdmin().body(corpoValido).post().then().extract().as(Map.class);
        Integer idCriado = (Integer) petCriado.get("id");

        corpoValido.put("nome", "nome editado");
        corpoValido.put("filhotes", 77);

        Map petRecemAtualizado = getRequisicaoAdmin().body(corpoValido).put("/" + idCriado).then()
                .statusCode(200)
                .extract().as(Map.class);

        getRequisicaoUsuario().get("/" + idCriado).then()
                .body("id", equalTo(petRecemAtualizado.get("id")))
                .body("nome", equalTo(petRecemAtualizado.get("nome")))
                .body("filhotes", equalTo(petRecemAtualizado.get("filhotes")));

    }

    @Test
    @DisplayName("PUT /pets com nascimento do pet e CPF do dono inválidos status 400")
    void testPutNomeCpfInvalidos() {
        Map corpoInvalido = getMapaDeJson("/pet-nascimento-cpfdono-invalidos.json");

        getRequisicaoAdmin().body(corpoInvalido)
                .post().then()
                .statusCode(400)
                .assertThat()
                .body(containsString(corpoInvalido.get("nascimento").toString()))
                .body(containsString(corpoInvalido.get("cpfDono").toString()))
                .extract().asString();
    }
}
