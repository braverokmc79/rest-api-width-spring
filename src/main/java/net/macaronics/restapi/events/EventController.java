package net.macaronics.restapi.events;


import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.ValidationException;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.print.attribute.standard.Destination;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;


    @Autowired
    private  ModelMapper modelMapper;

    public EventController(EventRepository eventRepository){
        this.eventRepository=eventRepository;
    }

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
    public ResponseEntity createEvent(@RequestBody EventDto eventDto){
        //Event event=modelMapper.map(eventDto, Event.class);
        Event event = eventDto.toEvent();
        Event newEvent=this.eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createdUri).body(event);
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
