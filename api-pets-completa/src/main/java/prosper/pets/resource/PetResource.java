package prosper.pets.resource;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import prosper.pets.domain.Pet;
import prosper.pets.service.PetService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pets")
@ApiResponses({
    @ApiResponse(responseCode = "401", description = "Falha de autenticação - Autenticação obrigatória", content = @Content(schema = @Schema(implementation = Void.class))),
    @ApiResponse(responseCode = "403", description = "Falha de autorização - Perfil não autorizado para esta operação", content = @Content(schema = @Schema(implementation = Void.class)))
})
public class PetResource {

    @Autowired
    private PetService petService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({"ROLE_usuario", "ROLE_admin"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Consulta retornou dados"),
        @ApiResponse(responseCode = "204", description = "Consulta não retornou nenhum dado", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<List<Pet>> get(Pet petPesquisa) {
        return ResponseEntity.ok(petService.getLista(petPesquisa));
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({"ROLE_usuario", "ROLE_admin"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Consulta retornou dados"),
        @ApiResponse(responseCode = "404", description = "Consulta não retornou nenhum dado", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<Pet> get(@PathVariable Long id) {
        return ResponseEntity.ok(petService.recuperar(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_admin")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<Pet> post(@RequestBody @Valid Pet novoPet,
                                    Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.criar(novoPet, authentication));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_admin")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "id inválido", content = @Content(schema = @Schema(implementation = Void.class))),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<Pet> put(@PathVariable Long id,
                                   @RequestBody @Valid Pet pet,
                                   Authentication authentication) {
        petService.atualizar(id, pet, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(petService.recuperar(id));
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_admin")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Registro excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "id inválido", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                      Authentication authentication) {
        petService.excluir(id, authentication);
        return ResponseEntity.noContent().build();
    }

}
