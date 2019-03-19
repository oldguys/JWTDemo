package com.example.demo.services;

import com.alibaba.fastjson.JSON;
import com.example.demo.dto.JwtHead;
import com.example.demo.dto.PayloadEntity;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2019/3/14 0014.
 */
@Service
public class MyJwtService {

    private String MAC_NAME = "HMacSHA256";

    public static String secretKeyString;

    public static String jwtHeadBase64URLString;

    private KeyGenerator keyGenerator;

    private String encodeType = "UTF-8";

    private Mac mac = null;

    @Scheduled(cron = "30 * * * *  ?")
    @PostConstruct
    public void refreshSecretKey() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        //得到一个 指定算法密钥的密钥生成器
        if (null == keyGenerator) {
            keyGenerator = KeyGenerator.getInstance(MAC_NAME);
        }
        if (null == mac) {
            mac = Mac.getInstance(MAC_NAME);
        }

        System.out.println("刷新秘钥");

        //生成一个密钥
        SecretKey secretKey = keyGenerator.generateKey();
        mac.init(secretKey);
        secretKeyString = Hex.encodeHexString(secretKey.getEncoded());
    }

    @PostConstruct
    public void initHead() {

        JwtHead jwtHead = new JwtHead(MAC_NAME, "JWT");
        jwtHeadBase64URLString = JSON.toJSONString(jwtHead);
        System.out.println(jwtHeadBase64URLString);
        jwtHeadBase64URLString = Base64.encodeBase64URLSafeString(jwtHeadBase64URLString.getBytes());
    }

    public String createToken(PayloadEntity entity) {
        String parseJson = JSON.toJSONString(entity);
        System.out.println(parseJson);
        String payloadString = Base64.encodeBase64URLSafeString(parseJson.getBytes());
        String prefix = jwtHeadBase64URLString + "." + payloadString;

        String signature = createSignature(prefix);
        return prefix + "." + signature;
    }

    private String createSignature(String prefix) {
        String signature = "";
        if (null != mac) {
            signature = Base64.encodeBase64URLSafeString(mac.doFinal(prefix.getBytes()));
        }
        return signature;
    }

    public boolean checkToken(String token) {

        if (StringUtils.isEmpty(token)) {
            System.out.println("空字符串！");
            return false;
        }

        String[] tokens = token.split("\\.");
        if (tokens.length < 3) {
            System.out.println("无效token");
            return false;
        }

        System.out.println("signature:" + tokens[2]);
        String signature = createSignature(tokens[0] + "." + tokens[1]);
        System.out.println("new:" + signature);

        // 校验结果
        boolean result = tokens[2].equals(signature);
        System.out.println("result:" + result);
        if (result) {
            try {
                String json = new String(Base64.decodeBase64(tokens[1]), encodeType);
                PayloadEntity entity = JSON.parseObject(json, PayloadEntity.class);

                if (entity.getExp() < System.currentTimeMillis()) {
                    System.out.println("token超时,无效");
                    return false;
                }
                return true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("无效signature");
        }

        return false;
    }
}
