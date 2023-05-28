package net.macaronics.restapi.accounts;

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

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

    @Rule
    public ExpectedException expectedException=ExpectedException.none();

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

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
        accountRepository.save(account);

        //WHEN
        UserDetailsService userDetailsService=(UserDetailsService) accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        //Then
        Assertions.assertThat(userDetails.getPassword()).isEqualTo(password);
    }



    @Test
    public void findByUsernameFail(){
        String username = "test@gmail.com";
        try{
            accountService.loadUserByUsername(username);
            Assert.fail("supposed to be failed");
        }catch (UsernameNotFoundException e){
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
        accountService.loadUserByUsername(username);
    }


}