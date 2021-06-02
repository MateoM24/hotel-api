package mezyk.mateusz.hotelapi.domain.model;

import java.math.BigDecimal;

public record RoomsCalculationResponse (int premiumRoomsOccupancy, BigDecimal premiumRoomsTotalPrice,
                                        int economyRoomsOccupancy, BigDecimal economyRoomsTotalPrice) {}