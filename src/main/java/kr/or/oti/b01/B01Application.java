package kr.or.oti.b01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class B01Application {

	//주석을 추가함 
	public static void main(String[] args) {
		SpringApplication.run(B01Application.class, args);
	}

}
