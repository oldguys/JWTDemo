package com.example.demo.service;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2019/3/14 0014.
 */
public class TestDemo {

    String token = "eyJhbGciOiJITWFjU0hBMjU2IiwidHlwIjoiSldUIn0=.eyJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTUyNzI2NDgxNTMzLCJuYW1lIjoiYXNkIiwic3ViIjoiMTIzNDU2IiwidXNlcklkIjoiMTIzNDU2In0=.rpksXPD/ZkrIvSWOLcecSPI0xUJJTxvTtQH4PD+0+M4=";

    @Test
    public void test() throws UnsupportedEncodingException {

        String[] tokens = token.split("\\.");
        for (String t : tokens) {
            System.out.println("t:\t" + t);

            byte[] temp = Base64.decode(t);
            String word = new String(temp,"UTF-8");
            System.out.println(word);

        }



    }
}
