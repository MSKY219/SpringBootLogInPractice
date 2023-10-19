package com.login.member.controller;

import com.login.member.dto.MemberDTO;
import com.login.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor // 생성자를 주입하기 위해 사용.
public class MemberController {
    // 생성자 주입
    private final MemberService memberService;


    // 회원가입 페이지 출력 요청
    @GetMapping("/member/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/member/save")
    public String save(
//            @RequestParam("memberEmail") String memberEmail
//            , @RequestParam("memberPassword") String memberPassword
//            , @RequestParam("memberName") String memberName
            // 위처럼 @RequestParam을 사용해도 무관하지만 각각의 파라미터를 입력해줘야하는 불편함이 있다.
            // 그래서 아래와 같이 @ModelAttribute를 사용해 모든 파라미터를 한번에 호출할 수 있다.
            @ModelAttribute MemberDTO memberDTO
    ) {
        // soutp, 파라미터를 출력하는 명령어
        System.out.println("memberDTO = " + memberDTO);
        // soutm, main을 출력하는 명령어
        System.out.println("MemberController.save");
        memberService.save(memberDTO);

        // 회원가입 완료 시, login.html을 출력
        return "login";
    }

    // 로그인 화면으로 이동
    @GetMapping("/member/login")
    public String loginForm() {
        return "login";
    }

    // 로그인 기능
    @PostMapping("member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            // login 성공
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            System.out.println("로그인 성공");
            return "main";
        } else {
            // login 실패
            return "login";
        }
    }

    // 회원 목록 페이지 이동 및 회원 목록 출력
    @GetMapping("/member/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        // 여러개의 데이터를 가지고 올 떄는 java.util 의 ArraysList를 사용한다.

        // 프론트단으로 가져갈 데이터가 있다면 model을 사용한다.
        model.addAttribute("memberList", memberDTOList);
        System.out.println(memberDTOList);
        return "list";
    }

    // 회원 목록 페이지 내, 회원 조회 기능
    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model) {
        // @GetMapping("/member/{id}") 안의 {id}는 경로 상에 존재하는 id 라는 값을 가지겠다는 의미이다.
        // 이 때 {id}를 받아주는 역할을 하는 어노테이션이 @PathVariable 이다.
        // /member/{id}의 {id}를 @PathVariable의 Long id로 받아온다.
        // Model model은 DB에서 해당되는 아이디의 값을 찾아 전달해기 위해 사용.
        MemberDTO memberDTO = memberService.findById(id);
        // 조회하는 데이터가 하나이기 때문에 MemberDTO 타입으로 할 수 있다.
        model.addAttribute("member", memberDTO);

        return "detail";
    }

    // 회원 목록 페이지 내, 회원 수정 선택 시 데이터 조회
    @GetMapping("/member/update")
    public String updateForm(HttpSession session, Model model) {
        // String myEmail = session.getAttribute("loginEmail");
        // session은 Object 타입이다. 하지만 String 타입으로 담으려고 해서 오류가 발생한다.
        // 따라서 아래와 같이 형변환을 해주어야 한다.
        String myEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember", memberDTO);
        return "update";
    }

    // 회원 정보 수정 기능
    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        System.out.println("업데이트 여부 확인 : " + memberDTO);
        return "redirect:/member/" + memberDTO.getId();
    }

    // 회원 삭제 기능
    @GetMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/member/";

    }

    // 로그아웃 기능
    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // 이메일 중복확인 기능
    @PostMapping("/member/email-check")
    @ResponseBody
    public String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        System.out.println("memberEmail = " + memberEmail);

        String checkResult = memberService.emailCheck(memberEmail);
        if (checkResult != null) {
            return "ok";
        }

        return "체크완료";
    }

}
