package net.macaronics.restapi.events;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebMvcTest
public class EventControllerTests {

//
//    @Autowired
//    MockMvc mockMvc;
//
//
//    @Test
//    public void createEvent(){
//        try {
//            mockMvc.perform(
//                            post("/api/events/")
//                            .contentType(MediaType.APPLICATION_JSON_UTF8)
//                             .accept(MediaTypes.HAL_JSON)
//                        )
//                    .andExpect(status().isCreated());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//


}