package _1danhebojo.coalarm.coalarm_service;

import _1danhebojo.coalarm.coalarm_service.global.config.JwtProperties;
import _1danhebojo.coalarm.coalarm_service.global.config.KakaoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties({KakaoProperties.class, JwtProperties.class})
public class CoalarmServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoalarmServiceApplication.class, args);
	}

}