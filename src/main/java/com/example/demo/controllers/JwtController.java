package com.example.demo.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.services.JwtService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2019/3/14 0014.
 */
@RestController
@RequestMapping("jwt")
public class JwtController {

    @PostMapping("login")
    public String login(String userId, String username) {
        return JwtService.createToken(userId, username);
    }

    @GetMapping("check")
    public Object check(@RequestHeader("token") String token) throws UnsupportedEncodingException {

        DecodedJWT decodedJWT = JwtService.jwtVerifier.verify(token);

        String stringPayload = new String(Base64.decodeBase64(decodedJWT.getPayload()), "utf-8");
        return stringPayload;
    }
}
