package net.macaronics.restapi.configs;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper=new ModelMapper();
        //setter 아닌  필드로 주입
        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);
        return modelMapper;
    }


//    @Bean
//    public PasswordEncoder passwordEncoder(){
//            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }

}
