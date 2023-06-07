package net.macaronics.restapi;

import net.macaronics.restapi.accounts.Account;
import net.macaronics.restapi.accounts.AccountRole;
import net.macaronics.restapi.accounts.PrincipalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

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
			@Override
			public void run(ApplicationArguments args) throws Exception {
				Account newAccount = Account.builder()
						.email("test@gmail.com")
						.password("1111")
						.roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
						.build();
				Account saveAccount = principalDetailsService.saveAccount(newAccount);
				System.out.println( "저장된 비밀번호 " +saveAccount.getPassword());
			}
		};
	}



}
