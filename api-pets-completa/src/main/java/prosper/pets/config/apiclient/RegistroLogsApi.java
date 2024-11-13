package prosper.pets.config.apiclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import prosper.pets.domain.logs.RegistroLog;
import prosper.pets.domain.racas.RacaPet;

import java.util.List;

@FeignClient(value = "logsApi", url = "https://60b6deef17d1dc0017b886b3.mockapi.io/api/v1/logs/")
public interface RegistroLogsApi {

    @PostMapping
    RegistroLog post(@RequestBody RegistroLog novoRegistroLog);

}
