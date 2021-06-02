package mezyk.mateusz.hotelapi.domain.ports.output;

import mezyk.mateusz.hotelapi.domain.model.PotentialGuest;

import java.util.List;

public interface PotentialGuestsRepository {

    List<PotentialGuest> findAllPotentialGuests();

}
