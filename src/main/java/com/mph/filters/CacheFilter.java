package com.mph.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletResponse;

public class CacheFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse httpResponse = (HttpServletResponse) response;

		httpResponse.setHeader("Expires", "0");
		httpResponse.setDateHeader("Last-Modified", System.currentTimeMillis());
		httpResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, pre-check=0, post-check=0");
		httpResponse.setHeader("Pragma", "no-cache");

        chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
	}

}