package net.macaronics.restapi.accounts;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of="id")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue
    private Integer id;

    private String email;

    private String password;


    //권한 정보
    //ADMIN, USER 권한 모두 있는 경우
    //ADMIN 권한만 있는 경우
    //USER 권한만 있는 경우
    //컬렉션 타입으로 설정  @ElementCollection
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;

}
