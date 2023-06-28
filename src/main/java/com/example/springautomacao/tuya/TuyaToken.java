package com.example.springautomacao.tuya;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


public class TuyaToken {
//    private static String secret = "1ad499c7493342a0a4c6321e20772e1a";
//    private static String client_id = "vegnmsa9wyvtmge4wnqk";
//    private static String accessToken;
//    private static String nonce = "";
//
//    /*   private String getSign() throws Exception {
//           String requestBody = "";
//           String hash = DigestUtils.sha256Hex(requestBody);
//
//           String strSign = "GET" + "\n" + hash + "\n" + "\n" + "/v1.0/token?grant_type=1";
//           //String strSign = method + "\n" + hash + "\n" + "\n" + endpoint;
//           //HttpCall.Get(requestBody,"/v1.0/token?grant_type=1");
//           String strSign2 = clientId  + System.currentTimeMillis() + strSign;
//           Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
//           SecretKeySpec secret_key = new SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//           sha256_HMAC.init(secret_key);
//           byte[] trab = sha256_HMAC.doFinal(strSign2.getBytes(StandardCharsets.UTF_8));
//
//           //Convert the hash to Hexadecimal
//           StringBuilder hexString = new StringBuilder();
//           for (byte b : trab) {
//               String hex = Integer.toHexString(0xff & b);
//               if (hex.length() == 1) hexString.append('0');
//               hexString.append(hex);
//           }
//           hash256 = hexString.toString().toUpperCase();
//           //System.out.println("HMAC-SHA256 hash: " + hash256);
//           return hash256;
//       }*/
//    private static long tokenExpirationTime;
//
//    public String accessToken() throws Exception {
//        if (isAccessTokenValid()) {
//            System.out.println("access token valid: " + accessToken);
//            return accessToken;
//        } else {
//            System.out.println("access token invalid, generating a new one ");
//            TuyaToken token = new TuyaToken();
//            accessToken = token.obtainAccessToken();
//            tokenExpirationTime = System.currentTimeMillis() + (7200 * 1000); // Set expiration time to 7200 seconds from now
//            return accessToken;
//        }
//    }
//    private static boolean isAccessTokenValid() {
//        return tokenExpirationTime > System.currentTimeMillis();
//    }
//    public String getSign(String requestBody, String endpoint, String method, boolean requiresAccessToken)
//            throws Exception {
//        String strSign2;
//        String hash = DigestUtils.sha256Hex(requestBody);
//        String strSign = method + "\n" + hash + "\n" + "\n" + endpoint;
//        if (requiresAccessToken) {
//            accessToken();
//            strSign2 = client_id + accessToken + System.currentTimeMillis() + nonce + strSign;
//        } else {
//            strSign2 = client_id + System.currentTimeMillis() + strSign;
//        }
//        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
//        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//        sha256_HMAC.init(secret_key);
//        byte[] crypto = sha256_HMAC.doFinal(strSign2.getBytes(StandardCharsets.UTF_8));
//        StringBuilder hexString = new StringBuilder();
//        for (byte b : crypto) {
//            String hex = Integer.toHexString(0xff & b);
//            if (hex.length() == 1) hexString.append('0');
//            hexString.append(hex);
//        }
//        return hexString.toString().toUpperCase();
//    }
//
//    public String obtainAccessToken() throws Exception {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet("https://openapi.tuyaus.com/v1.0/token?grant_type=1");
//        String sign = getSign("", "/v1.0/token?grant_type=1", "GET",false);
//        httpGet.setHeader("sign_method", "HMAC-SHA256");
//        httpGet.setHeader("client_id", client_id);
//        httpGet.setHeader("t", String.valueOf(System.currentTimeMillis()));
//        httpGet.setHeader("mode", "cors");
//        httpGet.setHeader("Content-Type", "application/json");
//        httpGet.setHeader("sign", sign);
//        StringEntity requestEntity = new StringEntity("");
//        httpGet.setEntity(requestEntity);
//        CloseableHttpResponse response = httpClient.execute(httpGet);
//        Gson gson = new Gson();
//        HttpEntity responseEntity = response.getEntity();
//        String responseString = EntityUtils.toString(responseEntity);
//        Type type = new TypeToken<Map<String, Object>>() {
//        }.getType();
//        Map<String, Object> jsonMap = gson.fromJson(responseString, type);
//        Map<String, Object> resultMap = (Map<String, Object>) jsonMap.get("result");
//        return resultMap.get("access_token").toString();
//    }

    private final String clientId;
    private final String clientSecret;
    private long timestamp;

    public TuyaToken(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    private String getSign() throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String requestBody = "";
        String hash = DigestUtils.sha256Hex(requestBody);

        String strSign = "GET" + "\n" + hash + "\n" + "\n" + "/v1.0/token?grant_type=1";

        String strSign2 = clientId  + timestamp + strSign;
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] trab = sha256_HMAC.doFinal(strSign2.getBytes(StandardCharsets.UTF_8));

        //Convert the hash to Hexadecimal
        StringBuilder hexString = new StringBuilder();
        for (byte b : trab) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }

    public String obtainAccessToken() throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException, ClientProtocolException, IOException, ParseException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://openapi.tuyaus.com/v1.0/token?grant_type=1");
        timestamp = System.currentTimeMillis();
        String hash256 = getSign();
        httpGet.setHeader("sign_method", "HMAC-SHA256");
        httpGet.setHeader("client_id", clientId);
        httpGet.setHeader("t", timestamp);
        httpGet.setHeader("mode", "cors");
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("sign", hash256);
        StringEntity requestEntity = new StringEntity("");
        httpGet.setEntity(requestEntity);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        Gson gson = new Gson();
        HttpEntity responseEntity = response.getEntity();
        String responseString = EntityUtils.toString(responseEntity);
        System.out.println("responsestring: " + responseString);
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> jsonMap = gson.fromJson(responseString, type);
        Map<String, Object> resultMap = (Map<String, Object>) jsonMap.get("result");
        System.out.println("access_token: " + resultMap.get("access_token"));
        System.out.println("expires_in: " + resultMap.get("expires_in"));
        String accessToken = resultMap.get("access_token").toString();
        return accessToken;
    }

}