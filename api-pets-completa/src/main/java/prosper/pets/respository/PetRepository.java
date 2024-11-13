package prosper.pets.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import prosper.pets.domain.Pet;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByNomeAndNomeDonoContains(String nome, String nomeDono);

    @Modifying
    @Transactional
    @Query("update Pet p set p.peso = ?2, p.atualizacao = CURRENT_TIMESTAMP where p.id = ?1")
    void atualizarPeso(Long id, Double novoPeso);
}
