package prosper.pets.exception;

import feign.FeignException;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

public class ChamadaApiException extends ResponseStatusException
{
    public ChamadaApiException(String nomeApi, FeignException causa) {
        super(
            causa.status() == -1 ? SERVICE_UNAVAILABLE : INTERNAL_SERVER_ERROR,
            String.format("Erro na chamada à API de %s: %s %s",
                    nomeApi, causa.getMessage(), causa.getCause() != null ? causa.getCause() : "")
        );
    }

}
