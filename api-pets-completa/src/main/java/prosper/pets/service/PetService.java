package prosper.pets.service;

import feign.FeignException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import prosper.pets.config.apiclient.RacasApi;
import prosper.pets.domain.Pet;
import prosper.pets.domain.racas.RacaPet;
import prosper.pets.domain.racas.TipoRaca;
import prosper.pets.exception.ChamadaApiException;
import prosper.pets.exception.PetNaoEncontradoException;
import prosper.pets.respository.PetRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    private RacasApi racasApi;

    private RegistroLogService registroLogService;

    private PetRepository petRepository;

    public PetService(RacasApi racasApi, RegistroLogService registroLogService, PetRepository petRepository) {
        this.racasApi = racasApi;
        this.registroLogService = registroLogService;
        this.petRepository = petRepository;
    }

    public List<Pet> getLista(Pet petPesquisa) {
        return getListaOuStatus204(petRepository.findAll(Example.of(petPesquisa)));
    }

    public List<Pet> getLista(String nome, String nomeDono) {
        return getListaOuStatus204(petRepository.findByNomeAndNomeDonoContains(nome, nomeDono));
    }

    public Pet criar(Pet novoPet, Authentication authentication) {
        setRaca(novoPet);
        Pet pet = petRepository.save(novoPet);
        registrarLog(authentication, "Pet criado: %d", pet.getId());
        return pet;
    }

    public void atualizar(Long idPet, Pet pet, Authentication authentication) {
        validarId(idPet);
        setRaca(pet);
        Pet anterior = petRepository.findById(idPet).get();
        BeanUtils.copyProperties(pet, anterior, "id");
        petRepository.save(anterior);
        registrarLog(authentication, "Pet %d atualizado", pet.getId());
    }

    public Pet recuperar(Long idPet) {
        validarId(idPet);
        return petRepository.findById(idPet).get();
    }

    public void excluir(Long idPet, Authentication authentication) {
        validarId(idPet);
        petRepository.deleteById(idPet);
        registrarLog(authentication, "Pet %d excluido", idPet);
    }

    protected void setRaca(Pet novoPet) {
        RacaPet raca = getRaca(novoPet.getTipo(), novoPet.getRaca());
        novoPet.setIdRaca(raca.getId());
    }

    public void atualizarPeso(Long idPet, Double novoPeso, Authentication authentication) {
        validarId(idPet);
        if (novoPeso == null || novoPeso <= 0.0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "novoPeso é obrigatório e deve ser >= 0.2");
        }
        petRepository.atualizarPeso(idPet, novoPeso);
        registrarLog(authentication, "Pet %d teve o peso atualizado para %.3f", idPet, novoPeso);
    }

    protected List<Pet> getListaOuStatus204(List<Pet> pets) {
        if (pets.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return pets;
    }

    protected void registrarLog(Authentication authentication, String descricao, Object... parametros) {
        try {
            registroLogService.registrarLog(authentication.getName(), String.format(descricao, parametros));
        } catch (FeignException ex) {
            throw new ChamadaApiException("Logs", ex);
        }
    }

    protected void validarId(Long idPet) {
        if (!petRepository.existsById(idPet)) {
            throw new PetNaoEncontradoException();
        }
    }

    protected RacaPet getRaca(TipoRaca tipo, String raca) {
        List<RacaPet> racas;

        try {
            racas = racasApi.get(tipo.getUri(), raca);
        } catch (FeignException ex) {
            throw new ChamadaApiException("Raças", ex);
        }

        if (racas.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Raça '%s' não encontrada na API de raças", raca)
            );
        }

        if (racas.size() > 1) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                String.format(
                    "Mais de um raça encontrada para '%s': %s",
                    raca, racas.stream().map(RacaPet::getRaca).collect(Collectors.joining(", "))
                )
            );
        }

        return racas.get(0);
    }

}
