package mezyk.mateusz.hotelapi.infrastructure.endpoint.mappers;

import mezyk.mateusz.hotelapi.domain.model.RoomsCalculationResponse;
import mezyk.mateusz.hotelapi.infrastructure.endpoint.dto.HotelAccommodationResponseDto;
import org.springframework.stereotype.Service;

@Service
public class ModelDtoMapper {

    public HotelAccommodationResponseDto domainResponseToDto(RoomsCalculationResponse domainResponse) {
        return new HotelAccommodationResponseDto(
                domainResponse.premiumRoomsOccupancy(),
                domainResponse.premiumRoomsTotalPrice(),
                domainResponse.economyRoomsOccupancy(),
                domainResponse.economyRoomsTotalPrice());
    }
}
