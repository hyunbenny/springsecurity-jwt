package com.hyunbenny.springsecurityjwt.controller;

import com.hyunbenny.springsecurityjwt.controller.request.JoinRequest;
import com.hyunbenny.springsecurityjwt.domain.UserAccount;
import com.hyunbenny.springsecurityjwt.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final UserAccountService userAccountService;

    @GetMapping("/home")
    public String home() {
        return "<h1>HOME</h1>";
    }

    @PostMapping("/token")
    public String token() {
        return "hello world";
    }

    @PostMapping("/join")
    public String join(@RequestBody JoinRequest request) {
        log.info("request: {}", request);

        UserAccount userEntity = userAccountService.join(request);
        log.info("userEntity: {}", userEntity);

        return userEntity.toString();
    }

    @GetMapping("/v1/user/hello")
    public String userHello() {
        return "hello world!!";
    }

    @GetMapping("/v1/manager/hello")
    public String managerHello() {
        return "hello manager!!";
    }

    @GetMapping("/v1/admin/hello")
    public String adminHello() {
        return "hello admin!!";
    }
}
