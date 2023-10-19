package com.login.member.service;

import com.login.member.dto.MemberDTO;
import com.login.member.entity.MemberEntity;
import com.login.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service // Spring이 관리하는 객체로써 Bean에 등록하는 service 어노테이션.
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원가입
    public void save(MemberDTO memberDTO) {
        // dto -> entity 변환
        // Repository의 save 메서드 호출

        // alt + enter를 이용하면 좌변을 만들 수 있다.
        MemberEntity memberEntity = MemberEntity.toMemberEntity((memberDTO));
        memberRepository.save(memberEntity);
        // 조건. entity 객체를 넘겨줘야 함.

    }

    // 로그인
    public MemberDTO login(MemberDTO memberDTO) {
        // 로그인 처리 과정
        // 1. 회원이 입력한 이메일로 DB에서 조회
        // 2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단.
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if (byMemberEmail.isPresent()) {
            // 조회 결과가 있을 경우, (== 해당 이메일을 가진 회원 정보가 있다는 의미)
            MemberEntity memberEntity = byMemberEmail.get();
            if (memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                // 비밀번호 일치
                // entity 객체를 DTO 객체로 변환 후 리턴
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            } else {
                // 비밀번호 불일치
                return null;
            }

        } else {
            // 조회 결과가 없을 경우, (== 해당 이메일을 가진 회원 정보가 없다는 의미)
            return null;
        }
    }

    // 회원 목록 페이지 이동 및 회원 목록 출력
    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>(); // findAll 메서드를 실행한 후 담아갈 객체 생성

        // memberEntityList를 memberDTOList에 대입하는게 불가능해서, 하나씩 옮겨담는 과정이 필요하다.
        for (MemberEntity memberEntity : memberEntityList) {
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
            // memberDTOList는 DTO 객체를 담기위한 List이다.

            // 위 코드와 같은 동작은 한다.
            // MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
            // memberEntity를 memberDTO로 변환해준다.
            // memberDTOList.add(memberDTO);
            // memberDTO로 변환된 객체를 memberDTOList에 담는다.
        }

        return memberDTOList;
    }

    // 회원 목록 페이지 내, 회원 조회 기능
    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isPresent()) {
            // 위 내용을 풀어서 써보면,
            // MemberEntity memberEntity = optionalMemberEntity.get();
            // MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
            // retrun memberDTO;
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    // 회원 목록 페이지 내, 회원 수정 기능
    public MemberDTO updateForm(String myEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(myEmail);
        if (optionalMemberEntity.isPresent()) {
            System.out.println("접근 확인용, 위치는 MemberSerivce");
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    // 회원 정보 수정 기능
        public void update(MemberDTO memberDTO) {
            System.out.println("접근 확인용, 위치는 회원정보 수정버튼 누른 후");
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    // 이메일 중복확인 기능
    public String emailCheck(String memberEmail) {
        Optional<MemberEntity>byMemberEmail = memberRepository.findByMemberEmail(memberEmail);

        if(byMemberEmail.isPresent()) {
            // 조회 결과가 있다 -> 이메일이 중복됨
            return null;
        } else {
            // 조회 결과가 없다 -> 입력한 이메일을 사용할 수 있다.
            return "ok";
        }
    }
}