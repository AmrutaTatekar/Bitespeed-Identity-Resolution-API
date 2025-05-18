package com.bitespeed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitespeed.dto.IdentifyRequest;
import com.bitespeed.dto.IdentifyResponseDto;
import com.bitespeed.service.ContactService;

@RestController
@RequestMapping("/identify")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity<IdentifyResponseDto> identify(@RequestBody IdentifyRequest request) {
        return new ResponseEntity<IdentifyResponseDto>(new IdentifyResponseDto(), HttpStatus.OK);
    }
}
