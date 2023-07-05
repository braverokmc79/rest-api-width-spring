package net.macaronics.restapi.index;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import net.macaronics.restapi.common.BaseControllerTest;
import net.macaronics.restapi.common.RestDocsConfiguration;
import net.macaronics.restapi.events.Event;
import net.macaronics.restapi.events.EventStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Log4j2
public class IndexControllerTest  extends BaseControllerTest {


    @Test
     public void makeJwtToken(){
         Date now =new Date();

        String compact = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // (1)
                .setIssuer("fresh")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis()))
                .claim("id", "test1")
                .claim("email", "test1@gmail.com")
                .signWith(SignatureAlgorithm.HS256, "ressfsae532qwerweqrqwerwr25ressfsae532qwerweqrqwerwr25ressfsae532qwerweqrqwerwr25ressfsae532qwerweqrqwerwr25ressfsae532qwerweqrqwerwr25")
                .compact();

        log.info("compact Tocke : {} " , compact);

    }




    @Test
    public void index() throws  Exception{
        this.mockMvc.perform(
                get("/api")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        ).andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("_links.events").exists());

    }


    @Test
    @DisplayName("인덱스 입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception{
        Event event =Event.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 05,  06, 19 , 20 ))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 05,  20,  20 , 20))
                .beginEventDateTime(LocalDateTime.of(2023, 05, 20, 20, 20))
                .endEventDateTime(LocalDateTime.of(2023, 11, 26,  20, 20))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("4.강남역 D2 스타텀 팩토리")
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo((print()))
                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("_links.index").exists()); // 추가
    }


}
