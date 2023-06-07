package net.macaronics.restapi.configs;

import net.macaronics.restapi.accounts.Account;
import net.macaronics.restapi.accounts.AccountRole;
import net.macaronics.restapi.accounts.AccountService;
import net.macaronics.restapi.accounts.PrincipalDetailsService;
import net.macaronics.restapi.common.BaseControllerTest;
import net.macaronics.restapi.common.TestDescription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;


/**
 *
 * 구글 검색 :  스프링시큐리티 oauth2  jwt 설정
 * 개발 :  https://velog.io/@jkijki12/Spring-Boot-OAuth2-JWT-적용해보리기
 *
 *
 */
public class AuthSeverConfigTest extends BaseControllerTest {

    @Autowired
    PrincipalDetailsService principalDetailsService;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception{

        String username = "keesun@gmail.con";
        String password = "keesun";

        Account keesun = Account.builder().
                email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        Account saveAccount = this.principalDetailsService.saveAccount(keesun);


        String clientId="myApp";
        String clientSecret="pass";


        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId ,clientSecret))
                                .param("username",username)
                                .param("password", password)
                                .param("grant_type", "password")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("access_token").exists());

    }



}