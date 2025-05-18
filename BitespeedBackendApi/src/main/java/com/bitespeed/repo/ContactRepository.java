package com.bitespeed.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bitespeed.entity.Contact;

@Repository
public interface ContactRepository  extends JpaRepository<Contact, Integer> {
	
}
