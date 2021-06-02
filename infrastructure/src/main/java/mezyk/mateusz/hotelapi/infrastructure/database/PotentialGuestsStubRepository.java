package mezyk.mateusz.hotelapi.infrastructure.database;

import mezyk.mateusz.hotelapi.domain.model.PotentialGuest;
import mezyk.mateusz.hotelapi.domain.ports.output.PotentialGuestsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PotentialGuestsStubRepository implements PotentialGuestsRepository {

    private static final List<PotentialGuest> POTENTIAL_GUESTS = List.of(
            new PotentialGuest(BigDecimal.valueOf(23)),
            new PotentialGuest(BigDecimal.valueOf(45)),
            new PotentialGuest(BigDecimal.valueOf(155)),
            new PotentialGuest(BigDecimal.valueOf(374)),
            new PotentialGuest(BigDecimal.valueOf(22L)),
            new PotentialGuest(BigDecimal.valueOf(99.99)),
            new PotentialGuest(BigDecimal.valueOf(100)),
            new PotentialGuest(BigDecimal.valueOf(101)),
            new PotentialGuest(BigDecimal.valueOf(115)),
            new PotentialGuest(BigDecimal.valueOf(209))
    );

    @Override
    public List<PotentialGuest> findAllPotentialGuests() {
        return POTENTIAL_GUESTS;
    }
}
