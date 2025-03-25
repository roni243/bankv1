package com.metacoding.bankv1.account;

import com.metacoding.bankv1.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AccountController {

    private final AccountService accountService;
    private final HttpSession session;


    @GetMapping("/account")
    public String list(HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인 후 사용해주세요");

        //select *
        List<Account> accountList = accountService.나의계좌목록(sessionUser.getId());
        request.setAttribute("models", accountList);
        
        return "account/list";
    }


    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/account/save-form")
    public String saveForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인 후 사용해주세요");
        return "account/save-form";
    }

    @PostMapping("/account/save")
    public String save(AccountRequest.SaveDTO saveDTO) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인 후 사용해주세요"); //두줄 인증체크

        accountService.계좌생성(saveDTO, sessionUser.getId()); // 세션.겟아이디- 터짐, 서버에서 꺼냄, 인증정보
        return "redirect:/";
    }

}
