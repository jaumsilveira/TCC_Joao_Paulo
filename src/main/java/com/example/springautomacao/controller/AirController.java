package com.example.springautomacao.controller;

import com.example.springautomacao.tuya.HttpCall;
import com.example.springautomacao.tuya.TuyaToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/air")
public class AirController {

    @Autowired
    private HttpCall httpCall;

    @GetMapping("/access")
    public ResponseEntity getAccess() throws Exception {
        TuyaToken token = new TuyaToken("vegnmsa9wyvtmge4wnqk", "1ad499c7493342a0a4c6321e20772e1a");
        String access_token = token.obtainAccessToken();
        System.out.println(access_token);
        return ResponseEntity.ok(access_token);
    }
    //temp = int
    @PostMapping("/temp")
    public ResponseEntity temperature(@RequestBody String valor) throws Exception {
        Map<String, Object> map = httpCall.post("temp",valor);
        return ResponseEntity.ok(map);
    }
    //"values": "{\"range\":[\"low\",\"mid\",\"high\",\"auto\"]}"
    @PostMapping("/fan/{fan}")
    public ResponseEntity fanSpeed(@PathVariable String fan) throws Exception {
        String fanCommand = String.format("\"%s\"", fan);
        Map<String, Object> map = httpCall.post("fan",fanCommand);
        return ResponseEntity.ok(map);
    }
    //"values": "{\"range\":[\"dehumidification\",\"cold\",\"auto\",\"wind_dry\",\"heat\"]}"
    @PostMapping("/mode/{mode}")
    public ResponseEntity mode(@PathVariable String mode) throws Exception {
        Map<String, Object> map = httpCall.post("mode",mode);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/switch")
    public ResponseEntity<Map<String, Object>> onOff(@RequestBody Boolean onOff) throws Exception {
        Map<String, Object> map = httpCall.post("switch", String.valueOf(onOff));
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/status")
    public ResponseEntity status() throws Exception {
        Map<String, Object> map = httpCall.get();
        return ResponseEntity.ok(map);
    }
}
