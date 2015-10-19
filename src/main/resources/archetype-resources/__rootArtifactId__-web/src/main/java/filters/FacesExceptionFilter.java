package ${package}.filters;

import static com.vedrax.util.Exceptions.unwrap;
import java.io.FileNotFoundException;
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
public class FacesExceptionFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, HttpSession session, FilterChain chain)
            throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (FileNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        } catch (ServletException e) {
            throw new ServletException(unwrap(e.getRootCause()));
        }
    }
    
}
