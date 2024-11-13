package prosper.pets.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import prosper.pets.domain.logs.RegistroLog;

@Profile("dev")
@Service
public class RegistroLogServiceDevImpl implements RegistroLogService {

    public RegistroLog registrarLog(String usuario, String descricao) {
       return null;
    }

}
