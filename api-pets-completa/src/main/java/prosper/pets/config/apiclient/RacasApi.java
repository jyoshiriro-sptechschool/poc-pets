package prosper.pets.config.apiclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import prosper.pets.domain.racas.RacaPet;

import java.util.List;

@FeignClient(value = "racasApi", url = "https://60b6deef17d1dc0017b886b3.mockapi.io/api/v1/")
public interface RacasApi {

    @GetMapping("{tipo}")
    List<RacaPet> get(@PathVariable String tipo, @RequestParam String raca);

}
