package com.ureca.user.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.user.dto.User;
import com.ureca.user.model.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String registerForm() {
        System.out.println("GET /register - 회원가입 폼 요청");
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        System.out.println("POST /register - 회원가입 요청: user=" + user);
        if ( user.getPassword() == null) {
            System.out.println("회원가입 실패 - 아이디와 비밀번호가 입력되지 않음");
            model.addAttribute("error", "아이디와 비밀번호에 한 글자 이상 입력해주세요.");
            return "register";
        }

        try {
            userService.insert(user);
            System.out.println("회원가입 성공: user=" + user);
            return "redirect:/user/login";
        } catch (DataIntegrityViolationException e) {
            System.out.println("회원가입 실패 - 중복 EMAIL 사용: " + e.getMessage());
            model.addAttribute("error", "이미 사용중인 Email입니다. 다른 Email를 사용해 주세요.");
            return "register";
        } catch (SQLException e) {
            System.out.println("DB 에러 발생: " + e.getMessage());
            e.printStackTrace();  // 전체 예외 스택 트레이스 출력
            model.addAttribute("error", "DB 오류가 발생했습니다. 다시 시도해 주세요.");
            return "register";
        } catch (Exception e) {
            System.out.println("알 수 없는 오류 발생: " + e.getMessage());
            e.printStackTrace();  // 전체 예외 스택 트레이스 출력
            model.addAttribute("error", "알 수 없는 오류가 발생했습니다.");
            return "register";
        }
    }

    @GetMapping("/login")
    public String loginForm() {
        System.out.println("GET /login - 로그인 폼 요청");
        return "login";
    }

    @PostMapping("/login")
    public String login(User user, HttpSession session, Model model) throws SQLException {
        System.out.println("POST /login - 로그인 요청: user=" + user);
        try {
            User result = userService.login(user);
            if (result != null) {
                System.out.println("로그인 성공: user=" + result);
                session.setAttribute("id", result.getUser_id());
                return "redirect:/task/list";
            } else {
                System.out.println("로그인 실패 - 아이디 혹은 비밀번호가 틀렸습니다.");
                model.addAttribute("error", "아이디 혹은 비밀번호가 틀렸습니다. 다시 입력해주세요.");
                return "login";
            }
        } catch (DataIntegrityViolationException e) {
            System.out.println("로그인 실패 - 데이터 무결성 위반: " + e.getMessage());
            model.addAttribute("error", "아이디와 비밀번호에 한 글자 이상 입력해주세요");
            return "login";
        } catch (SQLException e) {
            System.out.println("DB 에러 발생: " + e.getMessage());
            e.printStackTrace();  // 전체 예외 스택 트레이스 출력
            model.addAttribute("error", "DB 에러가 발생했습니다.");
            return "login";
        } catch (Exception e) {
            System.out.println("알 수 없는 오류 발생: " + e.getMessage());
            e.printStackTrace();  // 전체 예외 스택 트레이스 출력
            model.addAttribute("error", "알 수 없는 오류가 발생했습니다.");
            return "login";
        }
    }
}