package prosper.pets.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import prosper.pets.domain.Pet;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PetServiceTest {

	private PetService petService = new PetService(null, null, null);

	@Test
	void getListaOuStatus204_returnsPets_whenPetsListIsNotEmpty() {
		List<Pet> pets = List.of(new Pet());
		List<Pet> result = petService.getListaOuStatus204(pets);
		assertEquals(pets, result);
	}

	@Test
	void getListaOuStatus204_throwsNoContent_whenPetsListIsEmpty() {
		List<Pet> pets = Collections.emptyList();
		ResponseStatusException exception =
								assertThrows(ResponseStatusException.class, () -> petService.getListaOuStatus204(pets));

		assertEquals(200, exception.getRawStatusCode());
	}
}