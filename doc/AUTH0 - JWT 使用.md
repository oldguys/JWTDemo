##AUTH0 - JWT 使用

> JWT（JSON WEB TOKEN）: 官网介绍 [https://jwt.io/introduction/](https://jwt.io/introduction/)
> github: [https://github.com/oldguys/JWTDemo](https://github.com/oldguys/JWTDemo)
> 

##### 使用
Step1：引入Maven
```
		<dependency>
			<groupId>com.auth0</groupId>
			<artifactId>java-jwt</artifactId>
			<version>3.4.0</version>
		</dependency>

		<dependency>
			<groupId>commons-codec</groupId>
			<artifactId>commons-codec</artifactId>
		</dependency>

		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>fastjson</artifactId>
			<version>1.2.3</version>
		</dependency>
```

Step2：编写JWT服务类
```
package com.example.demo.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Verification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


/**
 * Created by Administrator on 2019/3/17 0017.
 */
@Service
public class JwtService {

    /**
     *  加密算法 可以抽象到环境变量中配置
     */
    private String MAC_NAME = "HMacSHA256";

    /**
     *  秘钥生成器
     */
    private KeyGenerator keyGenerator;

    /**
     *  加密算法
     */
    private static Algorithm algorithm;

    /**
     *  校验类
     */
    public static JWTVerifier jwtVerifier;

    /**
     *  校验器 用于生成 JWTVerifier 校验器
     */
    public static Verification verification;

    @PostConstruct
    @Scheduled(cron = "0 5 * * *  ?")
    public void initAndRefresh() throws NoSuchAlgorithmException {

        if (null == keyGenerator) {
            keyGenerator = KeyGenerator.getInstance(MAC_NAME);
        }

        SecretKey secretKey =keyGenerator.generateKey();
        algorithm = Algorithm.HMAC256(secretKey.getEncoded());
        verification = JWT.require(algorithm);
        jwtVerifier = verification.build();
    }

    public static String createToken(String userId, String username) {

        JWTCreator.Builder builder = JWT.create();
        builder.withSubject(userId);
        builder.withIssuer(username);
//        builder.withExpiresAt(new Date(System.currentTimeMillis() + (24 * 3600 * 1000)));
        builder.withExpiresAt(new Date());

        return builder.sign(algorithm);
    }
}

```

Step3：JWT使用

```
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

```

Step4：统一异常校验

```
package com.example.demo.handles;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/3/19 0019.
 */
@RestControllerAdvice
public class JwtExceptionHandles {

    @ExceptionHandler(TokenExpiredException.class)
    public Object TokenExpiredException(TokenExpiredException exception) {
        System.out.println();
        System.out.println(exception.getMessage());
        System.out.println();

        Map<String, String> map = new HashMap<>(16);
        map.put(TokenExpiredException.class.getSimpleName(), exception.getMessage());
        return map;
    }

    @ExceptionHandler(JWTVerificationException.class)
    public Object JWTVerificationException(JWTVerificationException exception) {
        System.out.println();
        System.out.println(exception.getMessage());
        System.out.println();

        Map<String, String> map = new HashMap<>(16);
        map.put(JWTVerificationException.class.getSimpleName(), exception.getMessage());
        return map;
    }

}

```