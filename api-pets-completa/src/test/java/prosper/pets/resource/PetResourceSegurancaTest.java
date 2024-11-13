package prosper.pets.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import prosper.pets.domain.Pet;
import prosper.pets.domain.racas.TipoRaca;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class) // anotação necessária para que os testes funcionem corretamente para Spring Security e as regras de perfis definidas
@SpringBootTest // permite o uso de @Autowired e @MockBean na classe de testes, para injetar beans reais ou mocks, respectivamente
@AutoConfigureMockMvc // permite o uso de objetos o tipo MockMvc na classe de testes
class PetResourceSegurancaTest {

    private static final String URI_BASE = "/pets";

    @Autowired
    MockMvc mockMvc; // objeto que inicializa um contexto de aplicação REST para pemritir a chamada às requisições da API do projeto

    @Autowired
    ObjectMapper mapper; // objeto para converters objetos Java em JSON e vice-versa
    

    @Test
    @DisplayName("post de Pet deve retornar status 401 se nenhum usuário")
    void post401() throws Exception {
        Pet requisicao = new Pet();

        mockMvc.perform(
                post(URI_BASE).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requisicao)))
                .andExpect(status().isUnauthorized());
        /*
        No código acima, pedimos para o mockMvc:
        - realizar um post, usando como URI o "/pets" e enviando "content-type":"application/json" na requisição
        - usar o objeto "requisicao", convertido numa string JSON como corpo de requisição
        - verificar se o status da resposta é o status 401 (Unauthorized), já que nenhuma autenticação foi usada
         */
    }

    @Test
    @DisplayName("post de Pet deve retornar status 403 se usuário sem permissão")
    @WithMockUser(roles = "hacker") // aqui indicamos que chegará à API um usuário de perfil (role) "hacker". Independente do mecanismo de autenticação da API (basic, oauth2 etc), essa anotação funcionará
    void post403() throws Exception {
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
                .andExpect(status().isForbidden());

        /*
        No código acima, pedimos para o mockMvc:
        - realizar um post, usando como URI o "/pets" e enviando "content-type":"application/json" na requisição
        - usar o objeto "requisicao", convertido numa string JSON como corpo de requisição
          (usamos um Pet válido aqui para ter certeza que o erro será devido ao perfil do usuário, não devido ao JSON inválido na requisição)
        - verificar se o status da resposta é o status 403 (Forbidden), já que o Endpoint testado não aceita o perfil "hacker"
         */
    }

    @Test
    @DisplayName("post de Pet deve retornar status 201 se usuário autorizado")
    @WithMockUser(roles = "admin")  // aqui indicamos que chegará à API um usuário de perfil (role) "admin". Independente do mecanismo de autenticação da API (basic, oauth2 etc), essa anotação funcionará
    void post201() throws Exception {
        Pet requisicao = new Pet();
        requisicao.setCpfDono("56254606046");
        requisicao.setFilhotes(0);
        requisicao.setNascimento(LocalDate.now().minusYears(2));
        requisicao.setNome("Fofura");
        requisicao.setEmailDono("eee@ggg.com");
        requisicao.setNomeDono("Zé Ruela");
        requisicao.setTelefoneDono("11 22222-3333");
        requisicao.setPeso(1.99);
        requisicao.setRaca("Papillon");
        requisicao.setTipo(TipoRaca.CACHORRO);

        mockMvc.perform(
                post(URI_BASE).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requisicao)))
                .andExpect(status().isCreated());
        /*
        No código acima, pedimos para o mockMvc:
        - realizar um post, usando como URI o "/pets" e enviando "content-type":"application/json" na requisição
        - usar o objeto "requisicao", convertido numa string JSON como corpo de requisição
        - verificar se o status da resposta é o status 201 (Created), já que o Endpoint dará acesso e será criado um novo Pet
        */
    }

    @Test
    @DisplayName("get deve retornar status 401 se nenhum usuário")
    void get401() throws Exception {
        mockMvc.perform(
                 get(URI_BASE))
                .andExpect(status().isUnauthorized());

        /*
        No código acima, pedimos para o mockMvc:
        - realizar um get, usando como URI o "/pets"
        - verificar se o status da resposta é o status 401 (Unauthorized), já que nenhuma autenticação foi usada
         */
    }

    @Test
    @DisplayName("delete deve retornar status 403 se usuário sem permissão")
    @WithMockUser(roles = "hacker") // aqui indicamos que chegará à API um usuário de perfil (role) "hacker". Independente do mecanismo de autenticação da API (basic, oauth2 etc), essa anotação funcionará
    void delete403() throws Exception {
        mockMvc.perform(
                delete(URI_BASE+"/1"))
                .andExpect(status().isForbidden());

        /*
        No código acima, pedimos para o mockMvc:
        - realizar um delete, usando como URI o "/pets/1"
        - verificar se o status da resposta é o status 403 (Forbidden), já que o Endpoint testado não aceita o perfil "hacker"
         */
    }

    @Test
    @DisplayName("get deve retornar status 200 se usuário autorizado")
    @WithMockUser(roles = "usuario") // aqui indicamos que chegará à API um usuário de perfil (role) "usuario". Independente do mecanismo de autenticação da API (basic, oauth2 etc), essa anotação funcionará
    void get200() throws Exception {
        mockMvc.perform(
                get(URI_BASE))
                .andExpect(status().isOk());

        /*
        No código acima, pedimos para o mockMvc:
        - realizar um get, usando como URI o "/pets"
        - verificar se o status da resposta é o status 200 (Ok), já que o Endpoint dará acesso e trará dados
         */
    }

}