package com.jiangfucheng.im.httpserver.utils;

import com.jiangfucheng.im.httpserver.bo.UserTokenPayloadBo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/15
 * Time: 0:05
 *
 * @author jiangfucheng
 */
public class JwtUtil {

	private static final String SECRET = "774664136ADE456880F267C9C9875146";
	private static final long TOKEN_EXPIRED_MILLION_SECOND = 60 * 60 * 24 * 1000 * 10;

	/**
	 * jwt公式: base64UrlEncode(header) + "." + base64UrlEncode(payload) + "." + HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload),secret)
	 */
	public static String generateToken(UserTokenPayloadBo userInfo) {
		Map<String, Object> jwtHeader = new HashMap<>();
		jwtHeader.put("type", "JWT");
		jwtHeader.put("alg", "HS256");
		Map<String, Object> payload = new HashMap<>();
		payload.put("userId", userInfo.getUserId());
		payload.put("account", userInfo.getAccount());
		payload.put("nickName", userInfo.getNickName());
		return Jwts.builder()
				.addClaims(payload)
				.setHeader(jwtHeader)
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRED_MILLION_SECOND))
				.signWith(SignatureAlgorithm.HS256, SECRET)
				.compact();
	}

	public static boolean verify(String token) {
		return Jwts.parser()
				.setSigningKey(SECRET)
				.isSigned(token);
	}

	public static UserTokenPayloadBo getTokenBody(String token) {
		DefaultClaims claims = (DefaultClaims) Jwts.parser()
				.setSigningKey(SECRET)
				.parse(token)
				.getBody();
		UserTokenPayloadBo userInfo = new UserTokenPayloadBo();
		Object userId = claims.get("userId");
		if (userId instanceof Integer) {
			userInfo.setUserId(Long.valueOf((Integer) userId));
		} else if (userId instanceof Long) {
			userInfo.setUserId((Long)userId);
		}
		userInfo.setAccount((String) claims.get("account"));
		userInfo.setNickName((String) claims.get("nickName"));
		return userInfo;
	}

}
