package ${package}.filters;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 *
 * @author penchenatremy
 */
public class AuthenticationFilter extends HttpFilter {

    private static final String INIT_PARAM_TIMEOUT_PAGE = "timeout-page";
    private static final String CURRENT_USER_ID_ATTRIBUTE = "currentUserId";
    private String timeoutPage = "/login.xhtml";

    @Override
    public void init() throws ServletException {
        String pageName = getInitParameter(INIT_PARAM_TIMEOUT_PAGE);
        if (pageName != null) {
            this.timeoutPage = pageName;
        }
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, FilterChain chain) throws ServletException, IOException {

        String loginURL = request.getContextPath() + timeoutPage;

        if (session == null || session.getAttribute(CURRENT_USER_ID_ATTRIBUTE) == null) {

            if (isAjaxRequest(request)) {
                ajaxRedirection(response, loginURL);
            } else {
                response.sendRedirect(loginURL);
            }

        } else {
            chain.doFilter(request, response);
        }
    }
    
}
