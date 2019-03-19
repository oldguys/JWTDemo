package com.example.demo.controllers;

import com.example.demo.dto.HttpResult;
import com.example.demo.dto.PayloadEntity;
import com.example.demo.services.MyJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2019/3/14 0014.
 */
@RestController
@RequestMapping("my")
public class MyJwtLoginController {

    @Autowired
    private MyJwtService jwtService;

    @GetMapping("secretKey")
    public String secretKeyString() {
        return MyJwtService.secretKeyString;
    }

    @GetMapping("jwtHead")
    public String jwtHeadBase64URLString() {
        return MyJwtService.jwtHeadBase64URLString;
    }

    @GetMapping("checkToken")
    public Object checkToken(@RequestHeader("token") String token) {

        System.out.println("token:" + token);

        return jwtService.checkToken(token);
    }

    @PostMapping("login")
    public Object login(String userId, String password, HttpSession session) {

        System.out.println("sessionId:" + session.getId());
        System.out.println("userId:" + userId);
        System.out.println("password:" + password);

//        long time = System.currentTimeMillis() + (30 * 1000);
        long time = System.currentTimeMillis() + (48 * 3600 * 1000);

        PayloadEntity entity = new PayloadEntity();
        entity.setAdmin(true);
        entity.setUserId(userId);
        entity.setName(password);
        entity.setSub(userId);
        entity.setExp(time);

        String token = jwtService.createToken(entity);

        return new HttpResult("success", token);
    }

}
