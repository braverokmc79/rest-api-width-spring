package net.macaronics.restapi.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

//현재 언랩을 하지 않아도 언랩이 된다
//다음코드 사용안함
public class EventResource extends RepresentationModel {
    public EventResource(Event content, Link... links){
       super();
        EntityModel eventResource = EntityModel.of(content);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
    }
}
