package com.bitespeed.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitespeed.dto.ContactDto;
import com.bitespeed.dto.IdentifyRequest;
import com.bitespeed.dto.IdentifyResponseDto;
import com.bitespeed.entity.Contact;
import com.bitespeed.repo.ContactRepository;
import com.bitespeed.util.*;

@Service
public class ContactService {
	
	@Autowired
	private ContactRepository contactRep;
	
	public IdentifyResponseDto identify(IdentifyRequest identifyRequest) {
		
		String email = identifyRequest.getEmail();
		String phoneNumber = identifyRequest.getPhoneNumber();
		
		List<Contact> existingContacts = contactRep.findByEmailOrPhoneNumber(email, phoneNumber);
		
		if(existingContacts.isEmpty()) {  
			// Create new "PRIMARY" contact
			Contact newContact = new Contact();
			BeanUtils.copyProperties(identifyRequest, newContact);
			newContact.setLinkPrecedence(Constants.PRIMARY);
			Contact DBContact = contactRep.save(newContact);
			
			// Create response for new contact
			ContactDto dto = new ContactDto(
					DBContact.getId(), 
					Collections.singletonList(email),
					Collections.singletonList(phoneNumber), 
					Collections.emptyList());
			
			return new IdentifyResponseDto(dto);
		}
		return null;
	}
	
}
