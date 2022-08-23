package hanghaeclone8a7.twotead.dto.response;

import hanghaeclone8a7.twotead.domain.Gender;
import hanghaeclone8a7.twotead.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private String username;
    private String email;
    private Gender gender;
    private String userImgUrl;
    private String birthday;

    public static MemberResponseDto createMemberResponseDto(Member member){

        String usernameSplit[] = member.getUsername().split("_");

        return MemberResponseDto.builder()
                .email(member.getEmail())
                .birthday(member.getBirthday())
                .gender(member.getGender())
                .userImgUrl(member.getUserImgUrl())
                .username(usernameSplit[0])
                .build();
    }
}
