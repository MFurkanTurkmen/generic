package com.mft.uageneric.controller;

import com.mft.uageneric.service.SozlesmeBilgiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RequiredArgsConstructor
@RestController
public class SozlesmeBilgiController {
    private final SozlesmeBilgiService sozlesmeBilgiService;

    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestParam Long id) {
        return ResponseEntity.ok(sozlesmeBilgiService.getSbdGeneric(id));
    }
}
