package prosper.pets.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;
import prosper.pets.domain.Pet;
import prosper.pets.domain.racas.TipoRaca;
import prosper.pets.exception.PetNaoEncontradoException;
import prosper.pets.service.PetService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PetResource.class) // permite o uso de @Autowired e @MockBean na classe de testes, para injetar beans reais ou mocks, respectivamente. Com a propriedade "controllers", indicamos qual(is) controller(s) será(ão) testadas
@AutoConfigureMockMvc(addFilters = false) // permite o uso de objetos o tipo MockMvc na classe de testes. O "addFilters = false" é para inibir qualquer uso da spring security nas chamadas
class PetResourceTest {

    private static final String URI_BASE = "/pets";

    @MockBean
    PetService service; // Object Mock da classe PetService. Seus métodos se comportarão conforme programado aqui

    @Autowired
    MockMvc mockMvc; // objeto que inicializa um contexto de aplicação REST para pemritir a chamada às requisições da API do projeto

    @Autowired
    ObjectMapper mapper; // objeto para converters objetos Java em JSON e vice-versa


    @Test
    @DisplayName("post de Pet deve retornar status 400 em caso de erro de validação")
    void postErroValidacao() throws Exception {
        Pet requisicao = new Pet();

        mockMvc.perform(
                    post(URI_BASE).contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(requisicao)))
                    .andExpect(status().isBadRequest());
        /*
        No código acima, pedimos para o mockMvc:
        - realizar um post, usando como URI o "/pets" e enviando "content-type":"application/json" na requisição
        - usar o objeto "requisicao", convertido numa string JSON como corpo de requisição
        - verificar se o status da resposta é o status 400 (Bad Request), já que o JSON é certamente inválido (não preenchemos nenhum campo)
         */
    }

    @Test
    @DisplayName("post de Pet deve retornar status 201 se tudo der certo")
    void postSemErro() throws Exception {
        Pet requisicao = new Pet();
        requisicao.setCpfDono("56254606046");
        requisicao.setFilhotes(0);
        requisicao.setNascimento(LocalDate.now().minusYears(2));
        requisicao.setNome("Fofura");
        requisicao.setEmailDono("eee@ggg.com");
        requisicao.setNomeDono("Zé Ruela");
        requisicao.setTelefoneDono("11 22222-3333");
        requisicao.setPeso(1.99);
        requisicao.setRaca("bulldog");
        requisicao.setTipo(TipoRaca.CACHORRO);

        mockMvc.perform(
                    post(URI_BASE).contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(requisicao)))
                    .andExpect(status().isCreated());

        /*
        No código acima, pedimos para o mockMvc:
        - realizar um post, usando como URI o "/pets" e enviando "content-type":"application/json" na requisição
        - usar o objeto "requisicao" (com todos os campos com valores válidos), convertido numa string JSON como corpo de requisição
        - verificar se o status da resposta é o status 201 (Created), já que o JSON é válido
         */
    }

    @Test
    @DisplayName("get deve retornar 204 e sem corpo caso não existam dados")
    void getSemDados() throws Exception {
        when(service.getLista(any(Pet.class))).then(invocation -> {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        });
        /*
        No código acima, aqui programamos o mock service, para que
        sempre que seu método "getLista()" for invocado, com qualquer Pet,
        vai lançar uma exceção do tipo ResponseStatusException(HttpStatus.NO_CONTENT)

        O código poderia ter sido:
        doThrow(new ResponseStatusException(HttpStatus.NO_CONTENT)).when(service).getLista(any(Pet.class));
        Com o exato mesmo resultado
         */

        MvcResult resposta = mockMvc.perform(
                                    get(URI_BASE))
                                    .andExpect(status().isNoContent())
                                    .andReturn();
        /*
        No código acima, pedimos para o mockMvc:
        - realizar um get, usando como URI o "/pets"
        - verificar se o status da resposta é o status 204 (No content), já que o mock da service vai "fazer de conta" que não existem pets na API
        - criamos um objeto do tipo MvcResult chamado "resposta" com o qual podemos fazer verificações mais complexas em cima da resposta
         */

        assertEquals(0, resposta.getResponse().getContentLength());
        // aqui nós apenas estamos verificando se o tamanho do corpo da resposta é 0, ou seja, estamos verificando que realmente não chegou nada no corpo da resposta



        MvcResult resposta1Parametro = mockMvc.perform(
                                                get(URI_BASE).queryParam("nome","batman"))
                                                .andExpect(status().isNoContent())
                                                .andReturn();
        /*
        No código acima, pedimos para o mockMvc:
        - realizar um get, usando como URI o "/pets?nome=batman"
        - verificar se o status da resposta é o status 204 (No content), já que o mock da service vai "fazer de conta" que não existem pets na API
        - criamos um objeto do tipo MvcResult chamado "resposta" com o qual podemos fazer verificações mais complexas em cima da resposta
         */

        assertEquals(0, resposta1Parametro.getResponse().getContentLength());
        // aqui nós apenas estamos verificando se o tamanho do corpo da resposta é 0, ou seja, estamos verificando que realmente não chegou nada no corpo da resposta


        MvcResult resposta2Parametros = mockMvc.perform(
                                                get(URI_BASE)
                                                    .queryParam("nome","batman")
                                                    .queryParam("tipo","GATO")
                                                )
                                                .andExpect(status().isNoContent())
                                                .andReturn();
        /*
        No código acima, pedimos para o mockMvc:
        - realizar um get, usando como URI o "/pets?nome=batman&tipo=GATO"
        - verificar se o status da resposta é o status 204 (No content), já que o mock da service vai "fazer de conta" que não existem pets na API
        - criamos um objeto do tipo MvcResult chamado "resposta" com o qual podemos fazer verificações mais complexas em cima da resposta
         */

        assertEquals(0, resposta2Parametros.getResponse().getContentLength());
        // aqui nós apenas estamos verificando se o tamanho do corpo da resposta é 0, ou seja, estamos verificando que realmente não chegou nada no corpo da resposta
    }

    @Test
    @DisplayName("get deve retornar 200 e com corpo caso existam dados")
    void getComDados() throws Exception {
        Pet pet1 = new Pet();
        pet1.setNome("p1");
        pet1.setPeso(11.1);
        Pet pet2 = new Pet();
        pet1.setNome("p22");
        pet1.setPeso(22.22);
        List<Pet> lista = List.of(pet1, pet2);
        // criamos uma lista com 2 Pets simples

        when(service.getLista(any(Pet.class))).thenReturn(lista);
        // programamos o mock da service para que o método getLista() sempre retorne a "lista"

        MvcResult resposta = mockMvc.perform(
                                    get(URI_BASE))
                                    .andExpect(status().isOk())
                                    .andReturn();
        /*
        No código acima, pedimos para o mockMvc:
        - realizar um get, usando como URI o "/pets"
        - verificar se o status da resposta é o status 200 (Ok), já que o endpoint deve retornar uma lista de 2 pets
        - criamos um objeto do tipo MvcResult chamado "resposta" com o qual podemos fazer verificações mais complexas em cima da resposta
         */

        List<Map<String, Object>> corpoResposta = mapper.readValue(
                resposta.getResponse().getContentAsString(),
                List.class
        );
        // convertemos o corpo da resposta numa List de Map (porque o endpoint deveria retorna um vetor de 2 JSONs)

        assertEquals(2, corpoResposta.size());
        /*
        verificamos se o tamanho da lista obtida a partir do corpo da resposta possui 2 itens,
        ou seja, se a API não corrompeu o resultado que o "banco" (no caso o mock service)
         */

        assertEquals(lista.get(0).getNome(), corpoResposta.get(0).get("nome"));
        /*
        verificamos se o campo "nome" do corpo do 1º item da lista da resposta corpoResposta.get(0).get("nome")
        é igual ao nome do 1º item da lista que enviamos na requisição lista.get(0).getNome()
         */

        assertEquals(lista.get(0).getPeso(), corpoResposta.get(0).get("peso"));
        /*
        verificamos se o campo "peso" do corpo do 1º item da lista da resposta corpoResposta.get(0).get("peso")
        é igual ao peso do 1º item da lista que enviamos na requisição lista.get(0).getPeso()
         */

        assertEquals(lista.get(1).getNome(), corpoResposta.get(1).get("nome"));
        /*
        verificamos se o campo "nome" do corpo do 2º item da lista da resposta corpoResposta.get(1).get("nome")
        é igual ao nome do 2º item da lista que enviamos na requisição lista.get(1).getNome()
         */

        assertEquals(lista.get(1).getPeso(), corpoResposta.get(1).get("peso"));
        /*
        verificamos se o campo "peso" do corpo do 2º item da lista da resposta corpoResposta.get(1).get("peso")
        é igual ao peso do 2º item da lista que enviamos na requisição lista.get(1).getPeso()
         */
    }

    @Test
    @DisplayName("delete deve retornar 404 caso id inexistente")
    void deleteNaoEncontrado() throws Exception {

        doThrow(PetNaoEncontradoException.class)
                .when(service).excluir(any(), any());
        /*
        No código acima, aqui programamos o mock service, para que
        sempre que seu método "excluir()" for invocado, com quaisquer parâmetros,
        vai lançar uma exceção do tipo PetNaoEncontradoException (que está programada para ter como status de resposta o 404)
         */

        mockMvc.perform(
                delete(URI_BASE+"/-1"))
                .andExpect(status().isNotFound());
        /*
        No código acima, pedimos para o mockMvc:
        - realizar um pet, usando como URI o "/pets/-1"
        - verificar se o status da resposta é o status 404 (Ok),
          já que nosso endpoint não deveria achar pet de código -1 ao tentar excluir um, conforme o que programamos no mock service
         */
    }
}