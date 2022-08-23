package hanghaeclone8a7.twotead.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JobPostImgUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 식별번호

    /*@JoinColumn(name = "jobPost_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private JobPost jobPost;*/

    @Column(nullable = false)
    private Long jobPostId;

    @Column(nullable = false)
    private String imgUrl;


}
