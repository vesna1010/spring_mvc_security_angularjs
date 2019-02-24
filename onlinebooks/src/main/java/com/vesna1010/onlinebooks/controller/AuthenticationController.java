package com.vesna1010.onlinebooks.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vesna1010.onlinebooks.enums.Language;
import com.vesna1010.onlinebooks.enums.Authority;

@RestController
public class AuthenticationController {

	@GetMapping("/authenticated")
	public boolean authenticated(Principal principal) {
		return (principal != null);
	}

	@GetMapping("/authorities")
	public List<Authority> authorities() {
		return Arrays.asList(Authority.values());
	}

	@GetMapping("/languages")
	public List<Language> languages() {
		return Arrays.asList(Language.values());
	}

}