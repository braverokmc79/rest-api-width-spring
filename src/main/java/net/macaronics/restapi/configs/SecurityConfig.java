package net.macaronics.restapi.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import net.macaronics.restapi.accounts.CustomAuthFailureHandler;
import net.macaronics.restapi.accounts.PrincipalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 1.securedEnabled
 *
 * @Secured 애노테이션을 사용하여 인가 처리를 하고 싶을때 사용하는 옵션이다. 기본값은 false 2.prePostEnabled
 *
 *          @PreAuthorize, @PostAuthorize 애노테이션을 사용하여 인가 처리를 하고 싶을때 사용하는 옵션이다.
 *          기본값은 false 3.jsr250Enabled
 *
 * @RolesAllowed 애노테이션을 사용하여 인가 처리를 하고 싶을때 사용하는 옵션이다. 기본값은 false
 *
 *               @Secured, @RolesAllowed 특정 메서드 호출 이전에 권한을 확인한다. SpEL 지원하지 않는다.
 * @Secured 는 스프링에서 지원하는 애노테이션이며, @RolesAllowed는 자바 표준
 *
 *          @Secured("ROLE_ADMIN") @RolesAllowed("ROLE_ADMIN")
 *
 */

//구글로그인이 완료된 뒤의 후처리가 필요함. 1.코드 받기(인증) , 2.엑세스토큰(권한), 3.사용자프로필 정보를 가져오기
//4.그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
//4-2 (이메일,전화번호,이름,아이디) 쇼핑몰 -> (집주소),

@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됩니다.
@RequiredArgsConstructor
@Component
public class SecurityConfig {

    private final CustomAuthFailureHandler customFailureHandler;

    private final ObjectMapper objectMapper;

    @Autowired
    PrincipalDetailsService principalDetailsService;


    private final BCryptPasswordEncoder passwordEncoder;


    @Bean
    public TokenStore tokenStore(){
        return  new InMemoryTokenStore();
    }



    /**
     * Spring Secureity 에서 인증은 AuthenticationManager 를 통해 이루어지며
     * AuthenticationManagerBuilder 가 AuthenticationManager 를 생성합니다.
     * userDetailsService 를 구현하고 있는 객체로 principalDetailsServicee를 지정해 주며, 비밀번호 암호화를
     * 위해 passwordEncoder 를 지정해줍니다.
     */
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailsService).passwordEncoder(passwordEncoder);
    }


    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(principalDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("*");
            }

//			@Override
//			public void addResourceHandlers(ResourceHandlerRegistry registry) {
//				// /images/** 은 /resources/images/ 으로 시작하는 uri호출은 /resources/images/ 경로 하위에 있는 리소스 파일이다 라는 의미입니다.
//				registry.addResourceHandler("/resources/upload/**").addResourceLocations("file:///C:/upload/");
//			}


        };
    }


    /**
     * 정적인 영역 무시
     * @return
     */
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().requestMatchers(
                "/h2-console/**"
        );
    }


//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable().cors().disable()
//
//                .authorizeHttpRequests(request -> request
//                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
//
//                        .requestMatchers("/h2-console","/h2-console/**", "/docs/index.html").permitAll()
//                        .requestMatchers(HttpMethod.GET,"/api/**").authenticated()
//                        .anyRequest().authenticated()
//
//
//                )
//
//                .headers().frameOptions().sameOrigin()  // 여기!
//                .and()
//
//                .formLogin(
//                        login -> login.usernameParameter("email")
//                            .passwordParameter("password")
//                                .failureHandler(customFailureHandler).permitAll()
//                )
//
//
////                .formLogin(login -> login
////                        .loginPage("/loginForm")
////                        .loginProcessingUrl("/login")
////                        .usernameParameter("userId")
////                        .passwordParameter("password")
////                        .defaultSuccessUrl("/", true)
////                        .failureHandler(customFailureHandler) // 로그인 오류 실패 체크 핸들러
////                        .permitAll()
//               // )
//                .logout();
//
//        return http.build();
//    }





}