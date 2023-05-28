package net.macaronics.restapi.events;

import jakarta.persistence.*;
import lombok.*;
import net.macaronics.restapi.accounts.Account;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)-
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;



    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;


    /**
     * //단반향 매핑
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account manager;


    public void update() {
        //Update Free
        if(this.basePrice==0 && this.maxPrice==0){
            this.free=true;
        }else{
            this.free=false;
        }

        //Update offline
        if(StringUtils.hasText(this.location)){
            this.offline=true;
        }else {
            this.offline=false;
        }
    }


    public  EventDto  toEventDto(){
        return  EventDto.builder()
                .name(this.name)
                .description(this.description)
                .beginEnrollmentDateTime(this.beginEnrollmentDateTime)
                .closeEnrollmentDateTime(this.closeEnrollmentDateTime)
                .beginEventDateTime(this.beginEventDateTime)
                .endEventDateTime(this.endEventDateTime)
                .location(this.location)
                .basePrice(this.basePrice)
                .maxPrice(this.maxPrice)
                .limitOfEnrollment(this.limitOfEnrollment)
                .build();
    }


    public Event updateEvent(EventDto eventDto){
        this.name=eventDto.getName();
        this.description=eventDto.getDescription();
        this.beginEnrollmentDateTime=eventDto.getBeginEnrollmentDateTime();
        this.closeEnrollmentDateTime=eventDto.getCloseEnrollmentDateTime();
        this.beginEventDateTime=eventDto.getBeginEventDateTime();
        this.endEventDateTime=eventDto.getEndEventDateTime();
        this.location=eventDto.getLocation();
        this.basePrice=eventDto.getBasePrice();
        this.maxPrice=eventDto.getMaxPrice();
        this.limitOfEnrollment=eventDto.getLimitOfEnrollment();

        return  this;
    }


}
