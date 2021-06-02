package mezyk.mateusz.hotelapi.domain.ports.input;

import mezyk.mateusz.hotelapi.domain.model.PotentialGuest;
import mezyk.mateusz.hotelapi.domain.model.RoomsCalculationRequest;
import mezyk.mateusz.hotelapi.domain.model.RoomsCalculationResponse;
import mezyk.mateusz.hotelapi.domain.ports.output.PotentialGuestsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BasicRoomOccupancyCalculatorTest {

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

    @Mock
    private static PotentialGuestsRepository potentialGuestsRepository;

    private final BigDecimal minPremiumRoomPrice = BigDecimal.valueOf(100);

    private static BasicRoomOccupancyCalculator calculator;

    @BeforeEach
    public void setUp() {
        Mockito.when(potentialGuestsRepository.findAllPotentialGuests()).thenReturn(POTENTIAL_GUESTS);
        calculator = new BasicRoomOccupancyCalculator(minPremiumRoomPrice, potentialGuestsRepository);
    }

    @Test
    public void shouldReturnThreePremiumAndThreeEconomyRooms() {
        RoomsCalculationRequest request = new RoomsCalculationRequest(3, 3);

        RoomsCalculationResponse response = calculator.calculate(request);

        assertEquals(3, response.premiumRoomsOccupancy());
        assertEquals(BigDecimal.valueOf(738), response.premiumRoomsTotalPrice());
        assertEquals(3, response.economyRoomsOccupancy());
        assertEquals(BigDecimal.valueOf(167.99), response.economyRoomsTotalPrice());
    }

    @Test
    public void shouldReturnSevenPremiumAndFiveEconomyRooms() {
        RoomsCalculationRequest request = new RoomsCalculationRequest(7, 5);

        RoomsCalculationResponse response = calculator.calculate(request);

        assertEquals(6, response.premiumRoomsOccupancy());
        assertEquals(BigDecimal.valueOf(1054), response.premiumRoomsTotalPrice());
        assertEquals(4, response.economyRoomsOccupancy());
        assertEquals(BigDecimal.valueOf(189.99), response.economyRoomsTotalPrice());
    }

    @Test
    public void shouldReturnTwoPremiumAndSevenEconomyRooms() {
        RoomsCalculationRequest request = new RoomsCalculationRequest(2, 7);

        RoomsCalculationResponse response = calculator.calculate(request);

        assertEquals(2, response.premiumRoomsOccupancy());
        assertEquals(BigDecimal.valueOf(583), response.premiumRoomsTotalPrice());
        assertEquals(4, response.economyRoomsOccupancy());
        assertEquals(BigDecimal.valueOf(189.99), response.economyRoomsTotalPrice());
    }

    @Test
    public void shouldReturnSevenPremiumAndOneEconomyRooms() {
        RoomsCalculationRequest request = new RoomsCalculationRequest(7, 1);

        RoomsCalculationResponse response = calculator.calculate(request);

        assertEquals(7, response.premiumRoomsOccupancy());
        assertEquals(BigDecimal.valueOf(1153.99), response.premiumRoomsTotalPrice());
        assertEquals(1, response.economyRoomsOccupancy());
        assertEquals(BigDecimal.valueOf(45), response.economyRoomsTotalPrice());
    }


}
