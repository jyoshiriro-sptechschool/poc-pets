package prosper.pets.service;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import prosper.pets.config.apiclient.RegistroLogsApi;
import prosper.pets.domain.logs.RegistroLog;
import prosper.pets.exception.ChamadaApiException;

@Profile({"hml","prod"})
@Service
public class RegistroLogServiceImpl implements RegistroLogService {

    @Autowired
    private RegistroLogsApi registroLogsApi;

    public RegistroLog registrarLog(String usuario, String descricao) {
        RegistroLog novoRegistroLog = new RegistroLog(usuario, descricao);
        try {
            return registroLogsApi.post(novoRegistroLog);
        } catch (FeignException ex) {
            throw new ChamadaApiException("Logs", ex);
        }
    }
}
