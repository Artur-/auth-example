package com.example.application.security.vaadinspring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.application.security.vaadin.VaadinFramework;

import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

public class NoInternalFrameworkRequestCache extends HttpSessionRequestCache {

	@Override
	public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
		if (VaadinFramework.isFrameworkInternalRequest(request)) {
			return;
		}

		super.saveRequest(request, response);
	}

}