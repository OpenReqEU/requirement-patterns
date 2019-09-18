package edu.upc.gessi.rptool.rest.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

@Provider
public class CORSFilter implements Filter {
    static final Logger logger = Logger.getLogger(CORSFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        //not implemented WHY?
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	    throws IOException, ServletException {
	HttpServletResponse res = (HttpServletResponse) response;
	res.addHeader("Access-Control-Allow-Origin", "*");
	res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
	res.addHeader("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization");
	chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
        //not implemented WHY?
    }

}
