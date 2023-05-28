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
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

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


    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getEvent(@PathVariable("id") Integer id) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if(optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Event event =optionalEvent.get();
        EntityModel<Event> entityModel = EventResource.of(event);
        entityModel.add(Link.of("/docs/index.html#resource-events-get").withRel("profile"));
        return ResponseEntity.ok(entityModel);
    }



    /** 수정하기 */
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable("id") Integer id,
                                         @RequestBody @Valid  EventDto eventDto, Errors errors){
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if(optionalEvent.isEmpty()) return ResponseEntity.notFound().build();

        if(errors.hasErrors())return badRequest(errors);

        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors())return badRequest(errors);  //커스텀 에러 로직상 에러잡기


        Event existEvent=optionalEvent.get();
        existEvent.updateEvent(eventDto);
        //서비스 객체를 만들지 않아서 더티체킹이 일어나지 않는다. 따라서, repository 실질적으로 저장처리
        eventRepository.save(existEvent);


        EntityModel<Event> entityModel = EventResource.of(existEvent);
        entityModel.add(Link.of("/docs/index.html#resource-events-update").withRel("profile"));
        return ResponseEntity.ok(entityModel);
    }





    private ResponseEntity<EntityModel> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(errorsResource.addLink(errors));
    }




}
