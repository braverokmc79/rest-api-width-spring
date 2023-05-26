package net.macaronics.restapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.RepresentationModel;

public class EventResourceORI extends RepresentationModel{

    //JSON 반환시 Event 객체 지우기
    //RepresentationModel 은 @JsonUnwrapped 사용하지 않아도 unwraaped 적용이된다.
   // @JsonUnwrapped
    private Event event;

    public EventResourceORI(Event event){
        this.event=event;
    }

    public Event getEvent() {
        return event;
    }
}
