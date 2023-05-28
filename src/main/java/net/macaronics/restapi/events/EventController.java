package net.macaronics.restapi.events;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.macaronics.restapi.common.ErrorsResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final ErrorsResource errorsResource;



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
    public ResponseEntity createEvent(@RequestBody @Valid  EventDto eventDto, Errors errors) throws Exception {

        if (errors.hasErrors()) {
            //  return ResponseEntity.badRequest().body(errors);
            return badRequest(errors);
        }

        //커스텀 validate 검사
        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()){
            // return ResponseEntity.badRequest().body(errors);
            return badRequest(errors);
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
        log.info("*  createdUri  {} " , createdUri);
        //출력 =>  *  createdUri  http://localhost/api/events

        EntityModel eventResource = EntityModel.of(newEvent);
        //셀프링크 추가 방법
        eventResource.add(linkTo(EventController.class).slash(eventId).withSelfRel());
        //1)링크추가방법
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        //2)링크추가방법
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        eventResource.add(Link.of("/docs/index.html#resource-events-create").withRel("profile"));
        return ResponseEntity.created(createdUri).body(eventResource);
    }

//    @GetMapping
//    public ResponseEntity queryEvent(Pageable pageable, RepresentationModel assembler ){
//        Page<Event> page = this.eventRepository.findAll(pageable);
//
//        assembler.add(linkTo(page).withRel("events"));
//        return  ResponseEntity.ok().body(assembler);
//
//    }

    /**페이징 처리 */
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Event>>> queryEvent(Pageable pageable ,
                                                                     PagedResourcesAssembler<Event> assembler){
        Page<Event> page = this.eventRepository.findAll(pageable);
        PagedModel<EntityModel<Event>> entityModel=assembler.toModel(page,e-> EventResource.of(e));
        //링크 추가
        entityModel.add(Link.of("/docs/index.html#resource-events-list").withRel("profile"));
        return  ResponseEntity.ok(entityModel);
    }



    private ResponseEntity<EntityModel> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(errorsResource.addLink(errors));
    }




}
