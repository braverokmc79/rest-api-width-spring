package net.macaronics.restapi.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@SpringBootTest
@Transactional
@RunWith(JUnitParamsRunner.class)
class EventTest {


    @Autowired
    private  ModelMapper modelMapper;


    @Test
    public void builder(){
        Event event=Event.builder().
            name("Inflearn Spsring  REST API").description("REST API development with Spring")
            .build();
        Assertions.assertThat(event).isNotNull();
    }


    @Test
    public void javaBean(){
        //Given
        String name = "Event";
        String description = "Spring";

        //When
        Event event=new Event();
//        event.setName(name);
//        event.setDescription(description);

        //Then
        Assertions.assertThat(event.getName()).isEqualTo(name);
        Assertions.assertThat(event.getDescription()).isEqualTo(description);
    }



    @Test
    public void 모델메퍼테스트(){

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

        EventDto eventDto = modelMapper.map(event, EventDto.class);

        try {
            modelMapper.validate();
            System.out.println("모델 매퍼 DTO 출력 : "+ eventDto.toString());
        }catch (ValidationException e){
            e.printStackTrace();
        }
        Assertions.assertThat(event.getName()).isEqualTo(eventDto.getName());


        //PropertyMap<EventDto, Event> eventDtoEventPropertyMap=new PropertyMap<EventDto, Event>() {
//            @Override
//            protected void configure() {
//                map().setId("d");
//            }
//        };
//
//
//        Event event2 = modelMapper.map(eventDto, Event.class);
//        try {
//            modelMapper.validate();
//            System.out.println("2 모델 매퍼 event2  출력 : "+ event2.toString());
//        }catch (ValidationException e){
//            e.printStackTrace();
//        }

    }



    @ParameterizedTest
   // @CsvSource({"0,0,true", "100,0,false"})
   // @MethodSource("paramsForTestFree")
    @MethodSource
    public void testFree(int basePrice, int maxPrice, boolean isFree){
        //Given
        Event event=Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        //When
        event.update();

        //Then
        Assertions.assertThat(event.isFree()).isEqualTo(isFree);
    }

    static  Object[] testFree(){
        return new Object[]{
                new Object[] {0, 0,true},
                new Object[]{100,0, false},
                new Object[]{0,100, false},
                new Object[]{100, 200, false}
        };
    }




    @ParameterizedTest
    @MethodSource
    public void testOffline(String location, boolean isOffline){
        //Given
        Event event =Event.builder()
                .location(location)

                .build();
        //When
        event.update();

        //Then
        Assertions.assertThat(event.isOffline()).isEqualTo((isOffline));
    }


    static Object[] testOffline(){
      return  new Object[]{
                  new Object[]{"강남", true},
                  new Object[]{null, false},
                  new Object[]{"  ", false}
        };
    }





}