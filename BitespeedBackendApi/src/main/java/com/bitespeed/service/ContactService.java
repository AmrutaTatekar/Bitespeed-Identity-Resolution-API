package com.bitespeed.service;

import com.bitespeed.dto.IdentifyRequest;
import com.bitespeed.dto.IdentifyResponseDto;

public interface ContactService {
	public IdentifyResponseDto identify(IdentifyRequest identifyRequest);
}
