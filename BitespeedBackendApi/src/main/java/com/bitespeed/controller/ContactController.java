package com.bitespeed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bitespeed.dto.IdentifyRequest;
import com.bitespeed.dto.IdentifyResponseDto;
import com.bitespeed.service.ContactServiceImpl;

@RestController
public class ContactController {

    @Autowired
    private ContactServiceImpl contactService;

    @PostMapping("/identify")
    public ResponseEntity<IdentifyResponseDto> identify(@RequestBody IdentifyRequest request) {
    	IdentifyResponseDto contactResponse = contactService.identify(request);
        return new ResponseEntity<IdentifyResponseDto>(contactResponse, HttpStatus.OK);
    }
}
