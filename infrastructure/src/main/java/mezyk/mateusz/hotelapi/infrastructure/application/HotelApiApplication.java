package mezyk.mateusz.hotelapi.infrastructure.application;

import mezyk.mateusz.hotelapi.domain.ports.input.BasicRoomOccupancyCalculator;
import mezyk.mateusz.hotelapi.domain.ports.output.PotentialGuestsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication(scanBasePackages = "mezyk.mateusz")
public class HotelApiApplication {

    @Value("${min.premium.guest.price}")
    private BigDecimal minPremiumGuestPrice;

    public static void main(String[] args) {
        SpringApplication.run(HotelApiApplication.class);
    }

    @Bean
    public BasicRoomOccupancyCalculator roomOccupancyCalculator(PotentialGuestsRepository potentialGuestsRepository) {
        return new BasicRoomOccupancyCalculator(minPremiumGuestPrice, potentialGuestsRepository);
    }

}
