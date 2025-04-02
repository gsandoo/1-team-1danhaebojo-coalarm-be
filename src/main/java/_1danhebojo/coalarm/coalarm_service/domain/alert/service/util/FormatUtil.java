package _1danhebojo.coalarm.coalarm_service.domain.alert.service.util;

import org.springframework.stereotype.Component;

@Component
public class FormatUtil {
    public static String convertMarketFormat(String market) {
        String[] parts = market.split("-");

        return parts[1] + "/" + parts[0]; // "BTC/KRW"
    }
}
