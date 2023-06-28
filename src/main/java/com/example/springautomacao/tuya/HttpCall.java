package com.example.springautomacao.tuya;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class  HttpCall {

    public static Map<String, Object> get() throws Exception{
        String requestBody = "";
        String hash = DigestUtils.sha256Hex(requestBody);
        System.out.println("hash: " + hash);
        String secret = "1ad499c7493342a0a4c6321e20772e1a";
        String client_id = "vegnmsa9wyvtmge4wnqk";
        String method = "GET";
        String endpoint = "/v1.0/devices/eb29790d99df3fa32exeek/status";
        TuyaToken token = new TuyaToken("vegnmsa9wyvtmge4wnqk", "1ad499c7493342a0a4c6321e20772e1a");
        String access_token = token.obtainAccessToken();
        System.out.println("access_token: " + access_token);
        String t = String.valueOf(System.currentTimeMillis());
        String nonce = "";

        String strSign = method + "\n" + hash + "\n" + "\n" + endpoint;
        System.out.println("String sign: \n" + strSign);

        String strSign2 = client_id + access_token + t + nonce + strSign;
        System.out.println("\nstrSign2: \n" + strSign2);

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] crypto = sha256_HMAC.doFinal(strSign2.getBytes(StandardCharsets.UTF_8));

        //Convert the hash to Hexadecimal
        StringBuilder hexString = new StringBuilder();
        for (byte b : crypto) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        String hash256 = hexString.toString().toUpperCase();
        System.out.println("HMAC-SHA256 hash: " + hash256);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://openapi.tuyaus.com/v1.0/devices/eb29790d99df3fa32exeek/status");


// add headers
        httpGet.setHeader("sign_method", "HMAC-SHA256");
        httpGet.setHeader("client_id", client_id);
        httpGet.setHeader("t", t);
        httpGet.setHeader("mode", "cors");
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("sign", hash256);
        httpGet.setHeader("access_token", access_token);

// add body
        StringEntity requestEntity = new StringEntity(requestBody);
        httpGet.setEntity(requestEntity);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getCode();
        Gson gson = new Gson();
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity);
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> jsonMap = gson.fromJson(responseString, type);
        return jsonMap;
    }
    public Map<String, Object> post(String code, String value) throws Exception{
        String requestBody = "{\n" +
                "\t\"commands\":[\n" +
                "\t\t{\n" +
                "\t\t\t\"code\": \"" + code + "\",\n" +
                "\t\t\t\"value\": " + value + "\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";
        String hash = DigestUtils.sha256Hex(requestBody);
        System.out.println("hash: " + hash);
        String secret = "1ad499c7493342a0a4c6321e20772e1a";
        String client_id = "vegnmsa9wyvtmge4wnqk";
        String method = "POST";
        String endpoint = "/v1.0/devices/eb29790d99df3fa32exeek/commands";
        TuyaToken token = new TuyaToken("vegnmsa9wyvtmge4wnqk", "1ad499c7493342a0a4c6321e20772e1a");
        String access_token = token.obtainAccessToken();
        System.out.println("access_token: " + access_token);
        String t = String.valueOf(System.currentTimeMillis());
        String nonce = "";

        String strSign = method + "\n" + hash + "\n" + "\n" + endpoint;
        System.out.println("String sign: \n" + strSign);

        String strSign2 = client_id + access_token + t + nonce + strSign;
        System.out.println("\nstrSign2: \n" + strSign2);

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] crypto = sha256_HMAC.doFinal(strSign2.getBytes(StandardCharsets.UTF_8));

        //Convert the hash to Hexadecimal
        StringBuilder hexString = new StringBuilder();
        for (byte b : crypto) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        String hash256 = hexString.toString().toUpperCase();
        System.out.println("HMAC-SHA256 hash: " + hash256);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://openapi.tuyaus.com/v1.0/devices/eb29790d99df3fa32exeek/commands");


    // add headers
        httpPost.setHeader("sign_method", "HMAC-SHA256");
        httpPost.setHeader("client_id", client_id);
        httpPost.setHeader("t", t);
        httpPost.setHeader("mode", "cors");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("sign", hash256);
        httpPost.setHeader("access_token", access_token);

    // add body
        StringEntity requestEntity = new StringEntity(requestBody);
        httpPost.setEntity(requestEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getCode();
        Gson gson = new Gson();
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity);
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> jsonMap = gson.fromJson(responseString, type);
        System.out.println("Código: " + statusCode);
        System.out.println("Resultado: " + jsonMap.get("result"));
        System.out.println("Success: " + jsonMap.get("success"));
        System.out.println("t: " + jsonMap.get("t"));
        System.out.println("tid: " + jsonMap.get("tid"));
        return jsonMap;
    }

}
