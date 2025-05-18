package com.bitespeed.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bitespeed.entity.Contact;

@Repository
public interface ContactRepository  extends JpaRepository<Contact, Integer> {
	public List<Contact> findByEmailOrPhoneNumber(String email, String phoneNumber);
	List<Contact> findByLinkedIdOrId(Integer linkedId, Integer id);
}
