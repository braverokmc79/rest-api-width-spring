package net.macaronics.restapi.events;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

//언랩을 하지 않아도 언랩이 된다
public class EventResource extends RepresentationModel {
    public EventResource(Event content, Link... links){
       super();
    }
}
