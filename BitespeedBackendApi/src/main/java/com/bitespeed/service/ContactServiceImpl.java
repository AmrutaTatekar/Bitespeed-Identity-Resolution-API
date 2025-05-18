package com.bitespeed.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitespeed.dto.ContactDto;
import com.bitespeed.dto.IdentifyRequest;
import com.bitespeed.dto.IdentifyResponseDto;
import com.bitespeed.entity.Contact;
import com.bitespeed.repo.ContactRepository;
import com.bitespeed.util.Constants;

@Service
public class ContactServiceImpl implements ContactService {
	
	@Autowired
	private ContactRepository contactRep;
	
	@Override
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
		
		// primary from existing contacts
		Contact primaryContact = existingContacts.stream()
			    .filter(c -> Constants.PRIMARY.equals(c.getLinkPrecedence()))
			    .min(Comparator.comparing(Contact::getCreatedAt))
			    .orElse(existingContacts.get(0)); // fallback

		Integer primaryId = primaryContact.getId();
		
		// create secondary contact if email or phone is new
		boolean emailExists = existingContacts.stream().anyMatch(c -> email != null && email.equals(c.getEmail()));
		boolean phoneExists = existingContacts.stream().anyMatch(c -> phoneNumber != null && phoneNumber.equals(c.getPhoneNumber()));

		if (!emailExists || !phoneExists) {
		    Contact newSecondary = new Contact();
		    newSecondary.setEmail(email);
		    newSecondary.setPhoneNumber(phoneNumber);
		    newSecondary.setLinkPrecedence(Constants.SECONDARY);
		    newSecondary.setLinkedId(primaryId);
		    contactRep.save(newSecondary);
		}

		// build response by fetching all linked contacts
		List<Contact> allLinkedContacts = contactRep.findByLinkedIdOrId(primaryId, primaryId);

		Set<String> emails = new LinkedHashSet<>();
		Set<String> phones = new LinkedHashSet<>();
		List<Integer> secondaryIds = new ArrayList<>();

		for (Contact c : allLinkedContacts) {
		    if (c.getEmail() != null) emails.add(c.getEmail());
		    if (c.getPhoneNumber() != null) phones.add(c.getPhoneNumber());

		    if (Constants.SECONDARY.equals(c.getLinkPrecedence())) {
		        secondaryIds.add(c.getId());
		    }
		}

		ContactDto dto = new ContactDto(
		    primaryId,
		    new ArrayList<>(emails),
		    new ArrayList<>(phones),
		    secondaryIds
		);

		return new IdentifyResponseDto(dto);
	}
	
}
