package ${package}.filter;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author VEDRAX SAS
 */
public class CharacterEncodingFilter extends HttpFilter {

    private static final Charset DEFAULT_ENCODING = UTF_8;
    private Charset encoding = DEFAULT_ENCODING;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, HttpSession session, FilterChain chain) throws ServletException, IOException {
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding.name());
        }

        chain.doFilter(request, response);
    }

}
