package mezyk.mateusz.hotelapi.domain.ports.input;

import mezyk.mateusz.hotelapi.domain.model.RoomsCalculationRequest;
import mezyk.mateusz.hotelapi.domain.model.RoomsCalculationResponse;

public interface RoomOccupancyCalculator {

    RoomsCalculationResponse calculate(RoomsCalculationRequest request);

}
