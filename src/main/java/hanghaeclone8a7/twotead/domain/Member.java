package hanghaeclone8a7.twotead.domain;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 테이블 식별번호
    @Column(nullable = false)
    private String kakaoId;
    @Column(nullable = false)
    private String username;
    @Column
    private String email;
    @Column
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @Column
    private String userImgUrl;
    @Column
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Column
    private Long companyId;
    @Column
    private String birthday;

}
