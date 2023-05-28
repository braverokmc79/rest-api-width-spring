package net.macaronics.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.macaronics.restapi.common.RestDocsConfiguration;
import net.macaronics.restapi.common.TestDescription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Autowired
    EventRepository eventRepository;


    /**
     * Mock은 껍데기만 있는 객체를 얘기합니다.
     * 인터페이스의 추상메소드가 메소드 바디는 없고 파라미터 타입과 리턴타입만 선언된 것처럼, Mock Bean은
     * 기존에 사용되던 Bean의 껍데기만 가져오고 내부의 구현 부분은 모두 사용자에게 위임한 형태입니다.
     *
     *
     //Mockito.when(eventRepository.save(event)).thenReturn(event);

     //MediaType.APPLICATION_JSON_UTF8   --> 버전 문제 다음 사용할것
     //MediaTypes.HAL_JSON_VALUE
     // Headers = [Location:"http://localhost/api/events/10", Content-Type:"application/hal+json"]

     */


    @Test
    @DisplayName("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws  Exception{
        Event event =Event.builder()
              //  .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 05,  06, 19 , 20 ))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 05,  20,  20 , 20))
                .beginEventDateTime(LocalDateTime.of(2023, 05, 20, 20, 20))
                .endEventDateTime(LocalDateTime.of(2023, 11, 26,  20, 20))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("1.강남역 D2 스타텀 팩토리")
             //   .free(false)
             //   .offline(false)
             //   .eventStatus(EventStatus.DRAFT)
                .build();

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
               // .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))

               .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/hal+json;charset=UTF-8"))

                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
               // .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.toString()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())

                //스프링  REST Docs  적용
                .andDo(document("create-event",
                        links(linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event"),
                                linkWithRel("profile").description("link to update an existing event")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),

                        //주의 : 요청 파라미터와 동일하게 빠짐없이 작성해 줘야 한다.
                        requestFields(
                               fieldWithPath("id").description("id of new Event"),
                               fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                               fieldWithPath("name").description("name of new Enrollment"),
                               fieldWithPath("description").description("ddescription of new event"),
                               fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime  of  new event"),
                               fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime  of  new event"),
                                fieldWithPath("location").description("location  of  new event"),
                                fieldWithPath("basePrice").description("basePrice  of  new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("offline").description("offline of new event"),
                                fieldWithPath("free").description("free of new event"),
                                fieldWithPath("eventStatus").description("eventStatus of new Enrollment")


                        ),

                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        
                        // relaxedResponseFields 응답의 일부분만 해당하는 자료 문서화
                        responseFields(

                                fieldWithPath("id").description("id of new Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("name").description("name of new Enrollment"),
                                fieldWithPath("description").description("ddescription of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime  of  new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime  of  new event"),
                                fieldWithPath("location").description("location  of  new event"),
                                fieldWithPath("basePrice").description("basePrice  of  new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("offline").description("offline of new event"),
                                fieldWithPath("free").description("free of new event"),
                                fieldWithPath("eventStatus").description("eventStatus of new Enrollment"),


                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query event list"),
                                fieldWithPath("_links.update-event.href").description("link to update existing event"),
                                fieldWithPath("_links.profile.href").description("link to profile event")
                        )
                ));



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
                .location("2.강남역 D2 스타텀 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo((print()))
                .andExpect(status().isCreated());
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
    @DisplayName("이벤트  입력 값이 잘못된 경우에 에러가 발생하는 테스트")
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
                .location("3.강남역 D2 스타텀 팩토리")
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


    
    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception{
        //Given
        IntStream.range(0, 30).forEach(this::generateEvent);

        //when
        this.mockMvc.perform(get("/api/events")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "name,DESC")

                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query_events"));
    }



    @Test
    @DisplayName("기존의 이벤트를 하나 조회하기")
    public void getEvent() throws Exception{
        //Given
       Event event=  this.generateEvent(100);

        //When &  Then
        this.mockMvc.perform(
                 get("/api/events/{id}",event.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"));
    }


    @Test
    @DisplayName("없는 이벤트를 조회 했을때 404 응답받기")
    public void getEvent404() throws  Exception{
        //When & Then
        this.mockMvc.perform(get("/api/events/11883"))
                .andExpect(status().isNotFound());
    }




    private Event generateEvent(int index) {
        Event event=Event.builder()
                .name("event "+index)
                .description("test event")
                .build();
        return this.eventRepository.save(event);
    }






}


