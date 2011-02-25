package org.comet4j.core.test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * 
 */
public class ServletLoadOnStart extends HttpServlet  {
	private static final long serialVersionUID = 1L;
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public ServletLoadOnStart() {
        super();
    }
	
    /* (non-Javadoc)
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    public void init() throws ServletException {
    	super.init();
    	log("---------------------------------Loaded!---------------------------------");
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.GenericServlet#destroy()
     */
    public void destroy() {
      
    }

}
