package mezyk.mateusz.hotelapi.domain.model;

import java.util.ArrayList;
import java.util.List;

public class HotelSchema {

    private final int premiumRoomsCount;
    private List<PotentialGuest> premiumGuests = new ArrayList<>();

    private final int economyRoomsCount;
    private List<PotentialGuest> economyGuests = new ArrayList<>();

    public HotelSchema(int premiumRoomsCount, int economyRoomsCount) {
        this.premiumRoomsCount = premiumRoomsCount;
        this.economyRoomsCount = economyRoomsCount;
    }

    public int getPremiumRoomsCount() {
        return premiumRoomsCount;
    }

    public int getEconomyRoomsCount() {
        return economyRoomsCount;
    }

    public List<PotentialGuest> getPremiumGuests() {
        return this.premiumGuests;
    }

    public List<PotentialGuest> getEconomyGuests() {
        return this.economyGuests;
    }

    public void setPremiumGuests(List<PotentialGuest> premiumGuests) {
        if (premiumGuests.size() > premiumRoomsCount) {
            throw new HotelSchemaViolationException(premiumGuests.size(), premiumRoomsCount);
        }
        this.premiumGuests = premiumGuests;
    }

    public void setEconomyGuests(List<PotentialGuest> economyGuests) {
        if (economyGuests.size() > economyRoomsCount) {
            throw new HotelSchemaViolationException(economyGuests.size(), economyRoomsCount);
        }
        this.economyGuests = economyGuests;
    }

    public void addPremiumGuest(PotentialGuest premiumGuest) {
        if (this.premiumGuests.size() == premiumRoomsCount) {
            throw new HotelSchemaViolationException("Can't host any more premium guests. Hotel is fully booked");
        }
        this.premiumGuests.add(premiumGuest);
    }

    public void addEconomyGuest(PotentialGuest economyGuest) {
        if (this.economyGuests.size() == premiumRoomsCount) {
            throw new HotelSchemaViolationException("Can't host any more economy guests. Hotel is fully booked");
        }
        this.economyGuests.add(economyGuest);
    }

    public void promoteEconomyGuestToPremiumRoom(PotentialGuest economyGuest) {
        if (this.premiumGuests.size() == premiumRoomsCount) {
            throw new HotelSchemaViolationException("Can't host any more premium guests. Hotel is fully booked");
        }
        this.economyGuests.remove(economyGuest);
        this.premiumGuests.add(economyGuest);
    }

    public int getPremiumRoomsOccupancy() {
        return this.getPremiumGuests().size();
    }

    public int getEconomyRoomsOccupancy() {
        return this.getEconomyGuests().size();
    }

    public class HotelSchemaViolationException extends RuntimeException {

        public HotelSchemaViolationException(int intendedGuestsCount, int availableRooms) {
            super(String.format("Tried to put %d guests into %d rooms", intendedGuestsCount, availableRooms));
        }

        public HotelSchemaViolationException(String message) {
            super(message);
        }
    }
}
