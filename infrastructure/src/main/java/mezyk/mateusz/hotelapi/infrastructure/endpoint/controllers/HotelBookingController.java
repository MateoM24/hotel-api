package mezyk.mateusz.hotelapi.infrastructure.endpoint.controllers;

import mezyk.mateusz.hotelapi.domain.model.RoomsCalculationRequest;
import mezyk.mateusz.hotelapi.domain.model.RoomsCalculationResponse;
import mezyk.mateusz.hotelapi.domain.ports.input.RoomOccupancyCalculator;
import mezyk.mateusz.hotelapi.infrastructure.endpoint.dto.HotelAccommodationResponseDto;
import mezyk.mateusz.hotelapi.infrastructure.endpoint.mappers.ModelDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController("hotel/accommodation")
public class HotelBookingController {

    private final RoomOccupancyCalculator roomOccupancyCalculator;
    private final ModelDtoMapper modelDtoMapper;


    public HotelBookingController(RoomOccupancyCalculator roomOccupancyCalculator, ModelDtoMapper modelDtoMapper) {
        this.roomOccupancyCalculator = roomOccupancyCalculator;
        this.modelDtoMapper = modelDtoMapper;
    }

    @GetMapping
    public ResponseEntity<HotelAccommodationResponseDto> calculateHotelAccommodation(@RequestParam Integer freePremiumRooms, @RequestParam Integer freeEconomicRooms) {
        RoomsCalculationResponse calculationResponse = roomOccupancyCalculator.calculate(new RoomsCalculationRequest(freePremiumRooms, freeEconomicRooms));
        return ResponseEntity.ok(modelDtoMapper.domainResponseToDto(calculationResponse));
    }

}
