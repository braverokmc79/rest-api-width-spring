package net.macaronics.restapi.index;

import net.macaronics.restapi.events.EventController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class IndexController {


    @GetMapping("/")
    public String index(){
        return "hello";
    }


   @GetMapping("/api")
    public RepresentationModel indexApi(){
        RepresentationModel index=new RepresentationModel();
        index.add(linkTo(EventController.class).withRel("events"));
        return index;
    }

    @GetMapping("/sample")
    public String sample(){
        return "samplePage";
    }


}
