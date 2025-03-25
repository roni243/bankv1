package com.metacoding.bankv1.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    private final HttpSession session;

    @GetMapping("/logout")
    public String logout() {
        //session.removeAttribute("sessionUser");
        session.invalidate();
        return "redirect:/";
    }

    // 로그인만 예외로 @Post (조회시에도)
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO loginDTO) {
        User sessionUser = userService.로그인(loginDTO);
        session.setAttribute("sessionUser", sessionUser); // stateful req에 저장못함, 세션에 저장(삭제될수도)
        return "redirect:/";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        session.setAttribute("metacoding", "apple");
        return "user/login-form";
    }

    @GetMapping("/join-form")
    public String joinForm() {

        String value = (String) session.getAttribute("metacoding");
        System.out.println("value: " + value);
        return "user/join-form";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute UserRequest.JoinDTO joinDTO) {
        userService.회원가입(joinDTO);
        return "redirect:/login-form";
    }
}
