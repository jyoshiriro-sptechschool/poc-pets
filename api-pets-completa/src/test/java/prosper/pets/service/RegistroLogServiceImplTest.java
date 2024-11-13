package prosper.pets.service;

import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import prosper.pets.config.apiclient.RegistroLogsApi;
import prosper.pets.domain.logs.RegistroLog;
import prosper.pets.exception.ChamadaApiException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("prod")
@SpringBootTest(classes = {RegistroLogServiceImpl.class})
class RegistroLogServiceImplTest {

    @Autowired
    RegistroLogService service;

    @MockBean
    RegistroLogsApi registroLogsApi;

    @Test
    @DisplayName("registrarLog() deve retornar um RegistroLog se não houver erro")
    void registrarLogOk() {
        String usuario = "usuario-teste";
        String descricao = "descrição teste";
        RegistroLog esperado = new RegistroLog(usuario, descricao);

        when(registroLogsApi.post(any())).thenReturn(esperado);

        RegistroLog resultado = service.registrarLog(usuario, descricao);

        assertEquals(esperado.getUsuario(), resultado.getUsuario());
        assertEquals(esperado.getDescricao(), resultado.getDescricao());
    }

    @Test
    @DisplayName("registrarLog() deve lançar ChamadaApiException em caso de erro na API de logs")
    void registrarLogErro() {
        when(registroLogsApi.post(any())).thenThrow(FeignException.class);

        assertThrows(ChamadaApiException.class, () -> service.registrarLog("usuario", "descricao"));
    }

    @Test
    @DisplayName("registrarLog() deve postar na API de log somente 1x")
    void registrarLogOk1Registro() {
        String usuario = "usuario-teste";
        String descricao = "descrição teste";

        when(registroLogsApi.post(any())).thenReturn(new RegistroLog(usuario, descricao));

        service.registrarLog(usuario, descricao);

        verify(registroLogsApi, times(1)).post(any());
    }

}