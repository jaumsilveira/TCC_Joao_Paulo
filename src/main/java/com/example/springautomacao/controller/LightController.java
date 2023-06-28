package com.example.springautomacao.controller;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/light")
public class LightController {

    @PostMapping("/{led}/{brightness}")
    public ResponseEntity<String> brightness(@PathVariable("led") int led, @PathVariable("brightness") int brightness) throws Exception {
        return alterarBrilhoLampada(led, brightness);
    }

    @PostMapping("/ligarTodas")
    public ResponseEntity<String> ligarTodas() throws IOException, ParseException {
        return alterarBrilhoLampada(5, 100);
    }

    @PostMapping("/desligarTodas")
    public ResponseEntity<String> desligarTodas() throws IOException, ParseException {
        return alterarBrilhoLampada(5, 0);
    }

    private static ResponseEntity<String> alterarBrilhoLampada(int led, int brightness) throws IOException, ParseException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "http://" + "192.168.0.57" + "/?lamp=" + led + "&brightness=" + brightness;
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());
        return ResponseEntity.ok().body(responseBody);
    }
}
