package com.example.demo.dto;

/**
 * Created by Administrator on 2019/3/14 0014.
 */
public class JwtHead {

    /**
     *  加密方式
     */
    private String alg;

    /**
     *  默认 JWT
     */
    private String typ;

    public JwtHead(String alg, String typ) {
        this.alg = alg;
        this.typ = typ;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }
}
