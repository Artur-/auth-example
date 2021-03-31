package com.example.application.security.vaadinspring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Component;

@Component
public class NoInternalVaadinRequestsCache extends HttpSessionRequestCache {

	@Autowired
	private EU endpointUtil;

	@Autowired
	private RequestUtil requestUtil;

	@Override
	public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
		if (requestUtil.isFrameworkInternalRequest(request)) {
			return;
		}
		if (endpointUtil.isEndpointRequest(request)) {
			return;
		}

		super.saveRequest(request, response);
	}

}