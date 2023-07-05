package net.macaronics.restapi.configs;


import net.macaronics.restapi.accounts.PrincipalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;


/**
 * api Oauth2 를 사용을 위한 인증 서버
 * 토큰 발급
 * 오류 사항 참조:
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 *
 *
   ★★★ 버전  업데이트로 SecurityConfig 에서 설정할것.
 */
//@Component
//@EnableAuthorizationServer
public class AuthSeverConfig  extends AuthorizationServerConfigurerAdapter {


    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    /** 유저정보를 가지고 있는 principalDetailsService 의존성 주입 */
    @Autowired
    PrincipalDetailsService principalDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    TokenStore tokenStore;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        System.out.println("******************  ClientDetailsServiceConfigurer ");
        //원래는 inMemory 가 아니라 DB 에서 관리해야 한다.
        clients.inMemory().withClient("myApp")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write")
                .secret(this.passwordEncoder.encode("pass"))
                .accessTokenValiditySeconds(10 * 60) //10분
                .refreshTokenValiditySeconds(6 *10  * 60) ; //갱신 토큰
    }

    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(principalDetailsService)
                .tokenStore(tokenStore);
    }


}
