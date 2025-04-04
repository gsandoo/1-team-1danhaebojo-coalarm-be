package _1danhebojo.coalarm.coalarm_service;

import _1danhebojo.coalarm.coalarm_service.global.properties.AlarmProperties;
import _1danhebojo.coalarm.coalarm_service.global.properties.JwtProperties;
import _1danhebojo.coalarm.coalarm_service.global.properties.KakaoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties({KakaoProperties.class, JwtProperties.class, AlarmProperties.class})
public class CoalarmServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoalarmServiceApplication.class, args);
	}

}