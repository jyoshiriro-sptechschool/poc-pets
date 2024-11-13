package prosper.pets.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Pet não encontrado")
public class PetNaoEncontradoException extends RuntimeException {

}
