package mezyk.mateusz.hotelapi.domain.ports.input;

import mezyk.mateusz.hotelapi.domain.model.PotentialGuest;
import mezyk.mateusz.hotelapi.domain.model.RoomsCalculationRequest;
import mezyk.mateusz.hotelapi.domain.model.RoomsCalculationResponse;
import mezyk.mateusz.hotelapi.domain.ports.output.PotentialGuestsRepository;
import mezyk.mateusz.hotelapi.domain.model.HotelSchema;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Predicate.*;

public class BasicRoomOccupancyCalculator implements RoomOccupancyCalculator {

    private final BigDecimal minPremiumRoomPrice;

    private final PotentialGuestsRepository potentialGuestsRepository;

    public BasicRoomOccupancyCalculator(BigDecimal minPremiumRoomPrice, PotentialGuestsRepository potentialGuestsRepository) {
        this.minPremiumRoomPrice = minPremiumRoomPrice;
        this.potentialGuestsRepository = potentialGuestsRepository;
    }

    @Override
    public RoomsCalculationResponse calculate(RoomsCalculationRequest request) {

        List<PotentialGuest> allPotentialGuests = potentialGuestsRepository.findAllPotentialGuests();
        List<PotentialGuest> premiumGuests = allPotentialGuests.stream().filter(pg -> pg.price().compareTo(minPremiumRoomPrice) >= 0).toList();
        List<PotentialGuest> economyGuests = allPotentialGuests.stream().filter(pg -> pg.price().compareTo(minPremiumRoomPrice) < 0).collect(Collectors.toList());
        HotelSchema hotelSchema = new HotelSchema(request.premiumRoomsCount(), request.economyRoomsCount());

        allocateMostPayingGuestsIntoPremiumRooms(hotelSchema, premiumGuests);
        allocateMostPayingEconomyGuestsIntoEconomyRooms(hotelSchema, economyGuests);
        tryToMoveMostPayingEconomyGuestsIntoPremiumRoomsPuttingOtherEconomyGuestsInTheirPlace(hotelSchema, economyGuests);

        List<PotentialGuest> accommodatedPremiumRoomGuests = hotelSchema.getPremiumGuests();
        List<PotentialGuest> accommodatedEconomyRoomGuests = hotelSchema.getEconomyGuests();
        return new RoomsCalculationResponse(
                accommodatedPremiumRoomGuests.size(),
                sumPrices(accommodatedPremiumRoomGuests),
                accommodatedEconomyRoomGuests.size(),
                sumPrices(accommodatedEconomyRoomGuests)
        );
    }

    private void allocateMostPayingGuestsIntoPremiumRooms(HotelSchema hotelSchema, List<PotentialGuest> premiumGuests) {

        List<PotentialGuest> highPayingPremiumGuests =
                findLimitedNumberOfHighPayingGuests(premiumGuests, hotelSchema.getPremiumRoomsCount());

        hotelSchema.setPremiumGuests(highPayingPremiumGuests);
    }

    private void allocateMostPayingEconomyGuestsIntoEconomyRooms(HotelSchema hotelSchema, List<PotentialGuest> economyGuests) {

        List<PotentialGuest> highPayingEconomicGuests =
                findLimitedNumberOfHighPayingGuests(economyGuests, hotelSchema.getEconomyRoomsCount());

        hotelSchema.setEconomyGuests(highPayingEconomicGuests);
    }

    private void tryToMoveMostPayingEconomyGuestsIntoPremiumRoomsPuttingOtherEconomyGuestsInTheirPlace(HotelSchema hotelSchema,
                                                                                                       List<PotentialGuest> economyGuests) {
        int totalPotentialEconomyGuests = economyGuests.size();
        int occupiedEconomyRooms = hotelSchema.getEconomyRoomsOccupancy();
        int freeRooms = totalPotentialEconomyGuests - occupiedEconomyRooms;

        if (freeRooms == 0) {
            return;
        }

        boolean tryToPromote;
        do {
            tryToPromote = promoteMostPayingEconomyGuestIfPossible(hotelSchema, economyGuests);
        } while (tryToPromote);

    }

    /** Promotes most paying economy guest to premium room and returns true. If the operation was not possible returns false.
     */
    private boolean promoteMostPayingEconomyGuestIfPossible(HotelSchema hotelSchema, List<PotentialGuest> economyGuests) {
        boolean thereIsEmptyPremiumRoom = hotelSchema.getPremiumRoomsCount() - hotelSchema.getPremiumRoomsOccupancy() > 0;
        boolean thereAreMoreEconomyPotentialGuestsThanEconomyRooms = economyGuests.size() - hotelSchema.getEconomyRoomsCount() > 0;

        if (thereIsEmptyPremiumRoom && thereAreMoreEconomyPotentialGuestsThanEconomyRooms) {

            promoteEconomyGuestToPremiumRoom(hotelSchema);

            economyGuests.stream()
                    .filter(not(hotelSchema.getEconomyGuests()::contains))
                    .filter(not(hotelSchema.getPremiumGuests()::contains))
                    .max(Comparator.comparing(PotentialGuest::price))
                    .ifPresent(hotelSchema::addEconomyGuest);

            return true;
        }

        return false;
    }

    private void promoteEconomyGuestToPremiumRoom(HotelSchema hotelSchema) {
        hotelSchema.getEconomyGuests()
                .stream()
                .max(Comparator.comparing(PotentialGuest::price))
                .ifPresent(hotelSchema::promoteEconomyGuestToPremiumRoom);
    }

    private List<PotentialGuest> findLimitedNumberOfHighPayingGuests(List<PotentialGuest> guests, int limit) {
        return guests.stream()
                .sorted(Comparator.comparing(PotentialGuest::price).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private BigDecimal sumPrices(List<PotentialGuest> guests) {
        return guests.stream()
                .map((PotentialGuest::price))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

}
