package com.bloom.pium.controller;

import com.bloom.pium.data.dto.UserInfoDto;
import com.bloom.pium.data.dto.UserinfoResponseDto;
import com.bloom.pium.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("user")
public class UserInfoController {
    private UserInfoService userInfoService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserInfoController(UserInfoService userInfoService, PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.userInfoService=userInfoService;
    }

    @GetMapping("")
    public String signupForm() {
//        UserInfoDto userInfoDto = new UserInfoDto(); // Create a new UserInfoDto Model model
//        model.addAttribute("userInfoDto", userInfoDto); // Add it to the model
        return "SignupPage";
    }

    @GetMapping("mainPage")
    public String goToMain(){
        return "mainPage";
    }

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입")
    public String signup(@ModelAttribute UserInfoDto userInfoDto, Model model){

        if (!userInfoService.isUsernameUnique(userInfoDto.getUsername())) {
            model.addAttribute("error", "이미 사용 중인 아이디입니다.");
            return "SignupPage"; // 중복 아이디인 경우 회원가입 폼으로 다시 이동
        }else{
            userInfoService.join(userInfoDto);
            return "redirect:/user/login"; // 회원가입 후 로그인 페이지로 리다이렉트
        }

    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // 로그인 페이지로 이동
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        // 로그인 처리 로직을 여기에 추가
        UserinfoResponseDto checkUserDto = userInfoService.findUsername(username);

        // 입력값과 데이터베이스에서 조회한 엔티티 비교
        if (username != null && checkUserDto.getUsername().equals(username)) {
            // 값이 일치하는 경우

            if(password != null && passwordEncoder.matches(password,checkUserDto.getPassword())){
                return "mainPage";
            } else {
                model.addAttribute("error", "비밀번호를 확인해주세요.");
            }
        }

        // 값이 불일치하는 경우
        model.addAttribute("error", "회원가입을 진행해주세요");
        return "redirect:/SignupPage";


    }

}