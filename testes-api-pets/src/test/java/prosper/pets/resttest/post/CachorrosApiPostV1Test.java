package prosper.pets.resttest.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import prosper.pets.resttest.AbstractCachorrosApiTest;

import java.util.Map;

public class CachorrosApiPostV1Test extends AbstractCachorrosApiTest {

    @Test
    @DisplayName("POST /pets com corpo válido status 201")
    void testPostSemAutenticacao() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        getRequisicaoAdmin().body(corpoValido)
                            .post().then()
                            .statusCode(201);
    }

}
