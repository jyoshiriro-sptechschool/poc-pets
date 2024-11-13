package prosper.pets.resttest.post;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import prosper.pets.resttest.AbstractCachorrosApiTest;

import java.time.LocalDateTime;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CachorrosApiPostV2Test extends AbstractCachorrosApiTest {

    @Test
    @DisplayName("POST /pets sem autenticação status 401")
    void testPostSemAutenticacao() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        given().contentType(ContentType.JSON).body(corpoValido)
                .post().then()
                .statusCode(401);
    }

    @Test
    @DisplayName("POST /pets sem autorização status 403")
    void testPostSemAutorizacao() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        getRequisicaoUsuario().body(corpoValido)
                .post().then()
                .statusCode(403);
    }

    @Test
    @DisplayName("POST /pets sem erros status 201 e c/ novos campos no corpo")
    void testPostSemErros() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        getRequisicaoAdmin().body(corpoValido)
                .post().then()
                .statusCode(201)
                .assertThat()
                .body("$", hasKey("id"))
                .body("$", hasKey("idade"))
                .body("$", hasKey("idRaca"))
                .body("$", hasKey("criacao"))
                .body("$", hasKey("atualizacao"));
    }


    @Test
    @DisplayName("POST /pets sem erros realmente criou Pet")
    void testPostRealmenteCriou() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        corpoValido.put("nascimento", LocalDateTime.now().minusDays(3).minusMonths(2).minusYears(1).toString());

        Map corpoResposta = getRequisicaoAdmin().body(corpoValido).post().then().extract().as(Map.class);

        getRequisicaoAdmin().get("/"+corpoResposta.get("id")).then()
                .body("id", equalTo(corpoResposta.get("id")))
                .body("nome", equalTo(corpoResposta.get("nome")))
                .body("nomeDono", equalTo(corpoResposta.get("nomeDono")))
                .body("telefoneDono", equalTo(corpoResposta.get("telefoneDono")))
                .body("idade", equalTo("1 ano, 2 meses e 3 dias"));
    }


    @Test
    @DisplayName("POST /pets com nome do pet e CPF do dono inválidos status 400")
    void testPostNomeCpfInvalidos() {
        Map corpoInvalido = getMapaDeJson("/pet-nascimento-cpfdono-invalidos.json");

        getRequisicaoAdmin().body(corpoInvalido)
                .post().then()
                .statusCode(400)
                .assertThat()
                .body(containsString(corpoInvalido.get("nascimento").toString()))
                .body(containsString(corpoInvalido.get("cpfDono").toString()));
    }
}
