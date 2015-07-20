package ${package}.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author VEDRAX SAS
 */
public abstract class HttpFilter implements Filter {

    private static final String ERROR_NO_FILTERCONFIG = "FilterConfig is not available."
            + " It seems that you've overriden HttpFilter#init(FilterConfig)."
            + " You should be overriding HttpFilter#init() instead, otherwise you have to call super.init(config).";

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        init();
    }

    public void init() throws ServletException {
        //
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        doFilter(httpRequest, httpResponse, session, chain);
    }

    public abstract void doFilter(HttpServletRequest request, HttpServletResponse response, HttpSession session, FilterChain chain)
            throws ServletException, IOException;

    @Override
    public void destroy() {
        filterConfig = null;
    }

    protected FilterConfig getFilterConfig() {
        checkFilterConfig();
        return filterConfig;
    }

    protected String getInitParameter(String name) {
        checkFilterConfig();
        return filterConfig.getInitParameter(name);
    }

    protected ServletContext getServletContext() {
        checkFilterConfig();
        return filterConfig.getServletContext();
    }

    private void checkFilterConfig() {
        if (filterConfig == null) {
            throw new IllegalStateException(ERROR_NO_FILTERCONFIG);
        }
    }

    protected boolean isAjaxRequest(HttpServletRequest httpServletRequest) {
        boolean check = false;
        String facesRequest = httpServletRequest.getHeader("Faces-Request");
        if (facesRequest != null && facesRequest.equals("partial/ajax")) {
            check = true;
        }
        return check;
    }

    protected void ajaxRedirection(HttpServletResponse response, String url) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml");
        response.getWriter()
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .printf("<partial-response><redirect url=\"%s\"></redirect></partial-response>", url);
    }

}
