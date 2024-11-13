package prosper.pets.service;

import prosper.pets.domain.logs.RegistroLog;

public interface RegistroLogService {

    RegistroLog registrarLog(String usuario, String descricao);
}
