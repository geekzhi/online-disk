package com.geekzhang.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz @ suixingpay.com>
 * @date: 2018/1/19 下午4:17
 * @version: V1.0
 */
public class TokenUtils {
    private static final String SECRET = "ThisIsASecret";

    public static String getJWTString(String usrId, Map<String, Object> claims) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        String jwtString = Jwts.builder()
                .setSubject(usrId)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .signWith(signatureAlgorithm, SECRET)
                .compact();
        return jwtString;
    }

    /**
     * 验证token数据是否正确
     *
     * @param token
     * @return
     */
    public static boolean isValid(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取usrId
     * @param jwsToken
     * @return
     */
    public static String getUserId(String jwsToken) {
        if (isValid(jwsToken)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwsToken);
            String userCode = String.valueOf(claimsJws.getBody().get("usrId"));
            return userCode;
        }
        return null;
    }

    /**
     * 获取userName
     * @param jwsToken
     * @return
     */
    public static String getUserName(String jwsToken) {
        if (isValid(jwsToken)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwsToken);
            String userCode = String.valueOf(claimsJws.getBody().get("usrName"));
            return userCode;
        }
        return null;
    }
}
