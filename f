[33mcommit 9b4dc27d20ca3e0280ee9671594b91b7a1c1862a[m[33m ([m[1;36mHEAD -> [m[1;32mmain[m[33m, [m[1;31morigin/main[m[33m)[m
Author: myspringbootstyle <108319537+myspringbootstyle@users.noreply.github.com>
Date:   Sun May 28 17:40:31 2023 +0900

    30. 이벤트 수정 API 구현

[1mdiff --git a/src/main/java/net/macaronics/restapi/events/Event.java b/src/main/java/net/macaronics/restapi/events/Event.java[m
[1mindex b07643f..c8bd9f7 100644[m
[1m--- a/src/main/java/net/macaronics/restapi/events/Event.java[m
[1m+++ b/src/main/java/net/macaronics/restapi/events/Event.java[m
[36m@@ -11,7 +11,7 @@[m [mimport java.time.LocalDateTime;[m
 @AllArgsConstructor[m
 @NoArgsConstructor[m
 @Getter[m
[31m-//@Setter[m
[32m+[m[32m@Setter[m
 @EqualsAndHashCode(of="id")[m
 @Entity[m
 public class Event {[m
[36m@@ -51,4 +51,37 @@[m [mpublic class Event {[m
         }[m
     }[m
 [m
[32m+[m
[32m+[m[32m    public  EventDto  toEventDto(){[m
[32m+[m[32m        return  EventDto.builder()[m
[32m+[m[32m                .name(this.name)[m
[32m+[m[32m                .description(this.description)[m
[32m+[m[32m                .beginEnrollmentDateTime(this.beginEnrollmentDateTime)[m
[32m+[m[32m                .closeEnrollmentDateTime(this.closeEnrollmentDateTime)[m
[32m+[m[32m                .beginEventDateTime(this.beginEventDateTime)[m
[32m+[m[32m                .endEventDateTime(this.endEventDateTime)[m
[32m+[m[32m                .location(this.location)[m
[32m+[m[32m                .basePrice(this.basePrice)[m
[32m+[m[32m                .maxPrice(this.maxPrice)[m
[32m+[m[32m                .limitOfEnrollment(this.limitOfEnrollment)[m
[32m+[m[32m                .build();[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m
[32m+[m[32m    public Event updateEvent(EventDto eventDto){[m
[32m+[m[32m        this.name=eventDto.getName();[m
[32m+[m[32m        this.description=eventDto.getDescription();[m
[32m+[m[32m        this.beginEnrollmentDateTime=eventDto.getBeginEnrollmentDateTime();[m
[32m+[m[32m        this.closeEnrollmentDateTime=eventDto.getCloseEnrollmentDateTime();[m
[32m+[m[32m        this.beginEventDateTime=eventDto.getBeginEventDateTime();[m
[32m+[m[32m        this.endEventDateTime=eventDto.getEndEventDateTime();[m
[32m+[m[32m        this.location=eventDto.getLocation();[m
[32m+[m[32m        this.basePrice=eventDto.getBasePrice();[m
[32m+[m[32m        this.maxPrice=eventDto.getMaxPrice();[m
[32m+[m[32m        this.limitOfEnrollment=eventDto.getLimitOfEnrollment();[m
[32m+[m
[32m+[m[32m        return  this;[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m
 }[m
[1mdiff --git a/src/main/java/net/macaronics/restapi/events/EventController.java b/src/main/java/net/macaronics/restapi/events/EventController.java[m
[1mindex 1429cc7..ef16d8c 100644[m
[1m--- a/src/main/java/net/macaronics/restapi/events/EventController.java[m
[1m+++ b/src/main/java/net/macaronics/restapi/events/EventController.java[m
[36m@@ -127,6 +127,31 @@[m [mpublic class EventController {[m
 [m
 [m
 [m
[32m+[m[32m    /** 수정하기 */[m
[32m+[m[32m    @PatchMapping("/{id}")[m
[32m+[m[32m    public ResponseEntity<?> updateEvent(@PathVariable("id") Integer id,[m
[32m+[m[32m                                         @RequestBody @Valid  EventDto eventDto, Errors errors){[m
[32m+[m[32m        Optional<Event> optionalEvent = this.eventRepository.findById(id);[m
[32m+[m[32m        if(optionalEvent.isEmpty()) return ResponseEntity.notFound().build();[m
[32m+[m
[32m+[m[32m        if(errors.hasErrors())return badRequest(errors);[m
[32m+[m
[32m+[m[32m        eventValidator.validate(eventDto, errors);[m
[32m+[m[32m        if(errors.hasErrors())return badRequest(errors);  //커스텀 에러 로직상 에러잡기[m
[32m+[m
[32m+[m
[32m+[m[32m        Event existEvent=optionalEvent.get();[m
[32m+[m[32m        existEvent.updateEvent(eventDto);[m
[32m+[m[32m        //서비스 객체를 만들지 않아서 더티체킹이 일어나지 않는다. 따라서, repository 실질적으로 저장처리[m
[32m+[m[32m        eventRepository.save(existEvent);[m
[32m+[m
[32m+[m
[32m+[m[32m        EntityModel<Event> entityModel = EventResource.of(existEvent);[m
[32m+[m[32m        entityModel.add(Link.of("/docs/index.html#resource-events-update").withRel("profile"));[m
[32m+[m[32m        return ResponseEntity.ok(entityModel);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m
 [m
 [m
 [m
[1mdiff --git a/src/main/java/net/macaronics/restapi/events/EventDto.java b/src/main/java/net/macaronics/restapi/events/EventDto.java[m
[1mindex 61d9041..24a8620 100644[m
[1m--- a/src/main/java/net/macaronics/restapi/events/EventDto.java[m
[1m+++ b/src/main/java/net/macaronics/restapi/events/EventDto.java[m
[36m@@ -12,6 +12,7 @@[m [mimport java.time.LocalDateTime;[m
 @AllArgsConstructor[m
 @EqualsAndHashCode(of="id")[m
 @Getter[m
[32m+[m[32m@Setter[m
 @ToString[m
 public class EventDto {[m
 [m
[36m@@ -40,6 +41,8 @@[m [mpublic class EventDto {[m
     private int limitOfEnrollment;[m
 [m
 [m
[32m+[m
[32m+[m
     public Event  toEvent(){[m
         return  Event.builder()[m
                 .name(this.name)[m
[1mdiff --git a/src/test/java/net/macaronics/restapi/events/EventControllerTests.java b/src/test/java/net/macaronics/restapi/events/EventControllerTests.java[m
[1mindex 1f5440e..7675d29 100644[m
[1m--- a/src/test/java/net/macaronics/restapi/events/EventControllerTests.java[m
[1m+++ b/src/test/java/net/macaronics/restapi/events/EventControllerTests.java[m
[36m@@ -18,6 +18,7 @@[m [mimport org.springframework.test.web.servlet.MockMvc;[m
 import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;[m
 [m
 import java.time.LocalDateTime;[m
[32m+[m[32mimport java.util.Optional;[m
 import java.util.stream.IntStream;[m
 [m
 import static org.springframework.restdocs.headers.HeaderDocumentation.*;[m
[36m@@ -25,6 +26,7 @@[m [mimport static org.springframework.restdocs.hypermedia.HypermediaDocumentation.li[m
 import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;[m
 import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;[m
 import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;[m
[32m+[m[32mimport static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;[m
 import static org.springframework.restdocs.payload.PayloadDocumentation.*;[m
 import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;[m
 import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;[m
[36m@@ -124,8 +126,7 @@[m [mpublic class EventControllerTests {[m
                                fieldWithPath("name").description("name of new Enrollment"),[m
                                fieldWithPath("description").description("ddescription of new event"),[m
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime  of  new event"),[m
[31m-                               fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),[m
[31m-                                fieldWithPath("endEventDateTime").description("endEventDateTime  of  new event"),[m
[32m+[m[32m                               fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"), fieldWithPath("endEventDateTime").description("endEventDateTime  of  new event"),[m
                                 fieldWithPath("location").description("location  of  new event"),[m
                                 fieldWithPath("basePrice").description("basePrice  of  new event"),[m
                                 fieldWithPath("maxPrice").description("maxPrice of new event"),[m
[36m@@ -296,7 +297,6 @@[m [mpublic class EventControllerTests {[m
                 .andDo(document("get-an-event"));[m
     }[m
 [m
[31m-[m
     @Test[m
     @Dis