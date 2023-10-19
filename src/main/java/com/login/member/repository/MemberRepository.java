package com.login.member.repository;

import com.login.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // Repository는 DB와 연결이 되는 최종 요소
    // DB와 작업할때는 반드시 Entity 객체로 넘겨야함.

    // 앞서 MemberService에서 save 메서드를 사용했지만
    // MemberRepository에선 save 메서드가 존재하지 않아도 사용이 가능한 이유는,
    // 부모 클래스인 JpaRepository에서 자식 인터페이스인 MemberRepository에 상속을 해주기 때문이다.

    // 이메일로 회원 정보 조회 메서드 (select * from member_table where member_email=?)
    Optional<MemberEntity> findByMemberEmail(String memberEmail);
    // Optional은 java.util에서 제공해주는 유틸인데, null을 방지하기 위해 사용.

}
