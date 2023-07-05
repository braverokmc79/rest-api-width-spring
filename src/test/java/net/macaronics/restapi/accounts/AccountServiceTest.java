package net.macaronics.restapi.accounts;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Log4j2
class AccountServiceTest {

    @Rule
    public ExpectedException expectedException=ExpectedException.none();

    @Autowired
    PrincipalDetailsService principalDetailsService;

    @Autowired
    AccountRepository accountRepository;


    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Test
    public void findByUsername(){
        //Given
        String password="1111";
        String username="junho@gmail.com";
        Account account=Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        Account save = principalDetailsService.saveAccount(account);

        //WHEN
        UserDetailsService userDetailsService=(UserDetailsService) principalDetailsService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        log.info(" save ==> {} , password : {}",save.getEmail(), save.getPassword());
        //Then
        Assertions.assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
    }



    @Test
    public void findByUsernameFail(){
        String username = "test2@gmail.com";
        try{
            UserDetails userDetails = principalDetailsService.loadUserByUsername(username);
           // log.info(userDetails.getUsername());
            Assert.fail("Not Found Exception");
        }catch (UsernameNotFoundException e){
            log.info(" 실패 ");
            Assertions.assertThat(e.getMessage()).containsSequence(username);
        }
    }



    @Test
    public void findByUsernameFail2(){
        //에러 발생 가능성을 먼저 코딩한다
        String username="test@gmail.com";
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        //When
        principalDetailsService.loadUserByUsername(username);
    }


}