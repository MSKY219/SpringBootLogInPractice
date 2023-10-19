package com.login.member.dto;

import com.login.member.entity.MemberEntity;
import lombok.*;

// @Data
// Data는 @ToString, @EqualsAndHashCode, @Getter, @Setter
// , @NoArgsConstructor, @AllArgsConstructor
// 등을 하나로 합친 어노테이션이다

// @Getter, @Setter : get메서드명, set메서드명 을 자동으로 만들어줌.
// @NoArgsConstructor : 기본 생성자를 만들어줌.
// @AllArgsConstructor : 필드를 모두 매개변수로 하는 생성자를 만들어줌.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {

    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;

    // 로그인 시, 사용할 데이터들을 Entity객체에서 DTO 객체로 변환해주는 매서드.
    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDTO.setMemberName(memberEntity.getMemberName());
        return memberDTO;
    }
}
