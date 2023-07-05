package net.macaronics.restapi;

import net.macaronics.restapi.accounts.Account;
import net.macaronics.restapi.accounts.AccountRole;
import net.macaronics.restapi.accounts.PrincipalDetailsService;
import net.macaronics.restapi.common.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

/**
 *
 * https://velog.io/@jkijki12/Spring-Boot-OAuth2-JWT-적용해보리기
 */
@SpringBootApplication
public class RestapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestapiApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}



	@Bean
	public ApplicationRunner applicationRunner(){
		return new ApplicationRunner() {

			@Autowired
			PrincipalDetailsService principalDetailsService;

			@Autowired
			AppProperties appProperties;

			@Override
			public void run(ApplicationArguments args) throws Exception {
				Account adminAccount = Account.builder()
						.email(appProperties.getAdminUsername())
						.password(appProperties.getAdminPassword())
						.roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
						.build();
				Account saveAdminAccount = principalDetailsService.saveAccount(adminAccount);


				Account newAccount = Account.builder()
						.email(appProperties.getUserUsername())
						.password(appProperties.getUserPassword())
						.roles(Set.of(AccountRole.USER))
						.build();
				Account saveAccount = principalDetailsService.saveAccount(newAccount);



				System.out.println( "저장된 비밀번호 " +saveAccount.getPassword());
			}
		};
	}



}
