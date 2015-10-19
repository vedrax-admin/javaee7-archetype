package ${package}.filters;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author VEDRAX SAS
 */
public class AdminFilter extends HttpFilter {

    private static final String INIT_PARAM_TIMEOUT_PAGE = "timeout-page";
    private static final String ACCESS_ERROR_PAGE = "access-not-allowed-page";
    private static final String CURRENT_USER_ID_ATTRIBUTE = "currentUserId";
    private static final String SECURITY_ROLE = "securityRole";
    private String timeoutPage = "/login.xhtml";
    private String accessPage = "/access-denied.xhtml";

    @Override
    public void init() throws ServletException {
        String pageName = getInitParameter(INIT_PARAM_TIMEOUT_PAGE);
        String accessName = getInitParameter(ACCESS_ERROR_PAGE);
        if (pageName != null) {
            this.timeoutPage = pageName;
        }
        if (accessName != null) {
            this.accessPage = accessName;
        }
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, FilterChain chain) throws ServletException, IOException {

        String loginURL = request.getContextPath() + timeoutPage;
        String accessErrorURL = request.getContextPath() + accessPage;

        if (session == null || session.getAttribute(CURRENT_USER_ID_ATTRIBUTE) == null) {

            if (isAjaxRequest(request)) {
                ajaxRedirection(response, loginURL);
            } else {
                response.sendRedirect(loginURL);
            }

        } else {
            if (session.getAttribute(SECURITY_ROLE).equals("ADMIN")) {
                chain.doFilter(request, response);
            } else {
                if (isAjaxRequest(request)) {
                    ajaxRedirection(response, accessErrorURL);
                } else {
                    response.sendRedirect(accessErrorURL);
                }
            }

        }
    }
}
