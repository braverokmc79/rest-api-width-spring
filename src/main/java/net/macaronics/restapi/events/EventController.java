package net.macaronics.restapi.events;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@Log4j2
public class EventController {

    private final EventRepository eventRepository;

   // private final  ModelMapper modelMapper;

    private final EventValidator eventValidator;


    /**
     *  methodOn  사용
     * @return
     */
//    @PostMapping("/method")
//    public ResponseEntity createEventMethod(){
//        WebMvcLinkBuilder webMvcLinkBuilder = linkTo(methodOn(EventController.class).createEvent(null));
//        return ResponseEntity.created(webMvcLinkBuilder.toUri()).build();
//    }


    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid  EventDto eventDto, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }

        //커스텀 validate 검사
        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }

        //modelMapper 오류
        //Event event=modelMapper.map(eventDto, Event.class);
        Event event = eventDto.toEvent();
        Integer eventId = event.getId();
        //유료인지 무료인지 변경처리
        event.update();
        Event newEvent=this.eventRepository.save(event);//저장

        /**
         *
         * ★ 링크 생성하기
         * EntityModel.of(newEvent); Resource 객체를 가져와서 사용
         *
         * **/
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(eventId);
        URI createdUri = selfLinkBuilder.toUri();

        EntityModel eventResource = EntityModel.of(newEvent);
        eventResource.add(linkTo(EventController.class).slash(eventId).withSelfRel());
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        return ResponseEntity.created(createdUri).body(eventResource);
    }



    /**

     {
     "id": 1,
     "name": "Spring",
     "description": "REST API Development with Spring",
     "beginEnrollmentDateTime": "2023-05-06T19:20:00",
     "closeEnrollmentDateTime": "2023-05-20T20:20:00",
     "beginEventDateTime": "2023-05-20T20:20:00",
     "endEventDateTime": null,
     "location": "강남역 D2 스타텀 팩토리",
     "basePrice": 100,
     "maxPrice": 200,
     "limitOfEnrollment": 100,
     "offline": false,
     "free": false,
     "eventStatus": null
     }

     *
     *
     */

}
