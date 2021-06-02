package mezyk.mateusz.hotelapi.infrastructure.endpoint.dto;

import java.math.BigDecimal;

public record HotelAccommodationResponseDto(int premiumRoomsOccupancy, BigDecimal premiumRoomsTotalPrice,
                                            int economyRoomsOccupancy, BigDecimal economyRoomsTotalPrice) {
}
