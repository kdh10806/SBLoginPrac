package com.example.demo.member.controller;

import com.example.demo.member.dto.MemberDto;
import com.example.demo.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    //회원가입 이동
    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    //회원가입
    @PostMapping("/signup")
    public String save(@ModelAttribute MemberDto memberDto) {
        memberService.signup(memberDto);
        return "signin";
    }

    //로그인 이동
    @GetMapping("/signin")
    public String signinForm(){
        return "signin";
    }

    //로그인
    @PostMapping("/signin")
    public String signin(@ModelAttribute MemberDto memberDto, HttpSession session){
        MemberDto loginResult = memberService.signin(memberDto);
        if(loginResult != null){
            //성공
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return "main";
        } else {
            //실패
            return "signin";
        }
    }

    //회원 목록
    @GetMapping("/")
    public String selectAll(Model model){
        List<MemberDto> memberDtoList = memberService.findAll();
        model.addAttribute("memberDtoList", memberDtoList);

        return "list";
    }

    //회원 목록 상세
    @GetMapping("/{id}")
    public String selectById(@PathVariable("id") Long id, Model model){
        MemberDto memberDto = memberService.selectById(id);
        model.addAttribute("memberDto", memberDto);

        return "detail";
    }

    //회원 정보 수정 이동
    @GetMapping("/update")
    public String updateForm(HttpSession session, Model model){
        String myEmail = (String) session.getAttribute("loginEmail");
        MemberDto memberDto = memberService.updateForm(myEmail);
        model.addAttribute("updateDto", memberDto);

        return "update";
    }

    //회원 정보 수정
    @PostMapping("/update")
    public String update(@ModelAttribute MemberDto memberDto){
        memberService.update(memberDto);
        return "redirect:/member/" + memberDto.getId();
    }

    //회원 정보 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        memberService.deleteById(id);
        return "redirect:/member/";
    }

    //로그 아웃
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate(); //세션 무효화
        return "index";
    }

}
