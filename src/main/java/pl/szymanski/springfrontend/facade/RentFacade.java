package pl.szymanski.springfrontend.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.szymanski.springfrontend.dtos.CreateRentDTO;
import pl.szymanski.springfrontend.dtos.RentDTO;
import pl.szymanski.springfrontend.exceptions.ProlongationException;
import pl.szymanski.springfrontend.exceptions.RentException;

public interface RentFacade {

  RentDTO createNewRent(CreateRentDTO createRentDTO) throws RentException;

  Page<RentDTO> getAllPaginated(Pageable pageable);

  Page<RentDTO> getAllPaginatedForUser(Pageable pageable, int userId);

  Page<RentDTO> getAllPaginatedForCurrentUser(Pageable pageable);

  Page<RentDTO> getAllPaginatedForBookEntry(Pageable pageable, int bookEntryId);

  String endRent(int rentId, String physicalState);

  RentDTO performProlongation(int rentId) throws RentException, ProlongationException;

}
