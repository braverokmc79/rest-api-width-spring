package net.macaronics.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@WebMvcTest

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * Mock은 껍데기만 있는 객체를 얘기합니다.
     * 인터페이스의 추상메소드가 메소드 바디는 없고 파라미터 타입과 리턴타입만 선언된 것처럼, Mock Bean은
     * 기존에 사용되던 Bean의 껍데기만 가져오고 내부의 구현 부분은 모두 사용자에게 위임한 형태입니다.
     */
//    @MockBean
//    EventRepository eventRepository;


    @Test
    @DisplayName("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws  Exception{
        Event event =Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 05,  06, 19 , 20 ))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 05,  20,  20 , 20))
                .beginEventDateTime(LocalDateTime.of(2023, 05, 20, 20, 20))
                .endEventDateTime(LocalDateTime.of(2023, 11, 26,  20, 20))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텀 팩토리")
                .free(true)
                .offline(false)
                .build();

        //Mockito.when(eventRepository.save(event)).thenReturn(event);

        //MediaType.APPLICATION_JSON_UTF8   --> 버전 문제 다음 사용할것
        //MediaTypes.HAL_JSON_VALUE
        // Headers = [Location:"http://localhost/api/events/10", Content-Type:"application/hal+json"]

        System.out.println("event:  "+objectMapper.writeValueAsString(event));
        mockMvc.perform(
                        post("/api/events")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .accept(MediaTypes.HAL_JSON)
                                .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)) );

    }



    @Test
    @DisplayName("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    //spring.jackson.deserialization.fail-on-properties=true
    public void createEvent_Bad_Request() throws Exception{
        Event event =Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 05,  06, 19 , 20 ))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 05,  20,  20 , 20))
                .beginEventDateTime(LocalDateTime.of(2023, 05, 20, 20, 20))
                .endEventDateTime(LocalDateTime.of(2023, 11, 26,  20, 20))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텀 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo((print()))
                .andExpect(status().isBadRequest());
    }




    @Test
    @DisplayName("입력값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws  Exception{
        EventDto eventDto=EventDto.builder().build();

        this.mockMvc.perform(
                post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
         ).andExpect(status().isBadRequest());

    }



    @Test
    @DisplayName("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception{
        Event event =Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 05,  06, 19 , 20 ))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 05,  20,  20 , 20))
                .beginEventDateTime(LocalDateTime.of(2023, 05, 20, 20, 20))
                .endEventDateTime(LocalDateTime.of(2023, 11, 26,  20, 20))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텀 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo((print()))
                .andExpect(status().isBadRequest());
    }







}





