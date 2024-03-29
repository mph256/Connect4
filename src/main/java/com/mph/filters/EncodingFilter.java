package com.mph.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;

public class EncodingFilter implements Filter {

	private String encoding = "UTF-8";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		String param = filterConfig.getInitParameter("encoding");

		if(param != null)
			encoding = param;

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		request.setCharacterEncoding(encoding);
		response.setCharacterEncoding(encoding);

        chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
	}

}