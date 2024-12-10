package com.hyun3.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {
	public void initialize() throws ServletException;
	public Object getHandler(final HttpServletRequest req);
}
