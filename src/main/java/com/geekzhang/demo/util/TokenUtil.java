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
public class TokenUtil {
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
            String userId = String.valueOf(claimsJws.getBody().get("usrId"));
            return userId;
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
            String userName = String.valueOf(claimsJws.getBody().get("usrName"));
            return userName;
        }
        return null;
    }

    /**
     * 获取用户会员状态 1会员 0非会员
     * @param jwsToken
     * @return
     */
    public static String getUserVip(String jwsToken) {
        if (isValid(jwsToken)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwsToken);
            String userVip = String.valueOf(claimsJws.getBody().get("usrVip"));
            return userVip;
        }
        return null;
    }

    /**
     * 获取用户网盘大小
     * @param jwsToken
     * @return
     */
    public static String getUserSize(String jwsToken) {
        if (isValid(jwsToken)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwsToken);
            String userSize = String.valueOf(claimsJws.getBody().get("usrSize"));
            return userSize;
        }
        return null;
    }

    /**
     * 获取用户已使用网盘大小
     * @param jwsToken
     * @return
     */
    public static String getUserUse(String jwsToken) {
        if (isValid(jwsToken)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwsToken);
            String userUse = String.valueOf(claimsJws.getBody().get("usrUse"));
            return userUse;
        }
        return null;
    }
}
