package ${package}.exception;

import com.vedrax.logging.Logger;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.PhaseId;
import static javax.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static javax.servlet.RequestDispatcher.ERROR_MESSAGE;
import static javax.servlet.RequestDispatcher.ERROR_REQUEST_URI;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author VEDRAX SAS
 */
public class VedraxExceptionHandler extends ExceptionHandlerWrapper {

    private final Logger logger = new Logger(getClass().getName());
    private static final String VIEW_EXPIRED = "VIEW";
    private static final String UNEXPECTED_EXCEPTION = "UNEXP";
    private static final String LOG_RENDER_EXCEPTION_UNHANDLED
            = "FullAjaxExceptionHandler: An exception occurred during rendering JSF ajax response."
            + " Error page '%s' CANNOT be shown as response is already committed."
            + " Consider increasing 'javax.faces.FACELETS_BUFFER_SIZE' if it really needs to be handled.";
    private static final String LOG_ERROR_PAGE_ERROR
            = "FullAjaxExceptionHandler: Well, another exception occurred during rendering error page '%s'."
            + " Trying to render a hardcoded error page now.";
    private static final String ERROR_PAGE_ERROR
            = "<?xml version='1.0' encoding='UTF-8'?><partial-response id='error'><changes><update id='javax.faces.ViewRoot'>"
            + "<![CDATA[<html lang='en'><head><title>Error in error</title></head><body><section><h2>Oops!</h2>"
            + "<p>A problem occurred during processing the ajax request. Subsequently, another problem occurred during"
            + " processing the error page which should inform you about that problem.</p></section>"
            + "</body></html>]]></update></changes></partial-response>";
    private ExceptionHandler wrapped;

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    public VedraxExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void handle() throws FacesException {
        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable t = context.getException();

            FacesContext fc = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
            String message;
            String errorPageLocation = "/WEB-INF/errorpages/error.xhtml";
            try {

                //Examine the root cause
                Throwable causingEx = ExceptionUtils.getRootCause(t);
                if (causingEx == null) {
                    causingEx = t;
                }

                if (causingEx instanceof ViewExpiredException) {
                    message = createErrorCode(VIEW_EXPIRED);
                    errorPageLocation = "/WEB-INF/errorpages/viewExpired.xhtml";
                } else if (causingEx instanceof ContextedRuntimeException) {
                    ContextedRuntimeException bue = (ContextedRuntimeException) causingEx;
                    message = bue.getMessage();
                    errorPageLocation = "/WEB-INF/errorpages/process.xhtml";
                } else {
                    message = createErrorCode(UNEXPECTED_EXCEPTION);
                    logger.error(message, t);
                }

                // Set the necessary servlet request attributes which a bit decent error page may expect.
                request.setAttribute(ERROR_MESSAGE, message);
                request.setAttribute(ERROR_REQUEST_URI, request.getRequestURI());

                if (!canRenderErrorPageView(fc, t, errorPageLocation)) {
                    return; // If error page cannot be rendered, then it's end of story.
                }
                //Render Error Page
                renderErrorPageView(fc, request, errorPageLocation);

            } catch (IOException e) {
                throw new FacesException(e);
            } finally {
                i.remove();
            }
        }

        //parent handle
        getWrapped()
                .handle();
    }

    private String createErrorCode(String code) {
        String randomNumber = String.valueOf(Math.abs(new Date().hashCode()));
        return code + "_" + randomNumber;
    }

    private void renderErrorPageView(FacesContext context, final HttpServletRequest request, String errorPageLocation) throws IOException {
        try {
            if (context == null || !context.getPartialViewContext().isAjaxRequest()) {
                ViewHandler viewHandler = context.getApplication().getViewHandler();
                UIViewRoot viewRoot = viewHandler.createView(context, errorPageLocation);
                context.setViewRoot(viewRoot);
                context.getPartialViewContext().setRenderAll(true);
                context.renderResponse();
            } else {
                NavigationHandler nav = context.getApplication().getNavigationHandler();
                nav.handleNavigation(context, null, errorPageLocation);
                context.renderResponse();
            }
        } catch (Exception e) {
            logger.error(String.format(LOG_ERROR_PAGE_ERROR, errorPageLocation), e);
            ExternalContext externalContext = context.getExternalContext();
            if (!externalContext.isResponseCommitted()) {
                resetResponse(context);
                externalContext.setResponseContentType("text/xml");
                externalContext.getResponseOutputWriter().write(ERROR_PAGE_ERROR);
                context.responseComplete();
            } else {
                // Well, it's too late to handle. Just let it go.
                throw new FacesException(e);
            }
        } finally {
            // Prevent some servlet containers from handling error page itself afterwards. So far Tomcat/JBoss
            // are known to do that. It would only result in IllegalStateException "response already committed"
            // or "getOutputStream() has already been called for this response".
            request.removeAttribute(ERROR_EXCEPTION);
        }
    }

    private void resetResponse(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        String contentType = externalContext.getResponseContentType();
        String characterEncoding = externalContext.getResponseCharacterEncoding();
        externalContext.responseReset();
        externalContext.setResponseContentType(contentType);
        externalContext.setResponseCharacterEncoding(characterEncoding);
    }

    private boolean canRenderErrorPageView(FacesContext context, Throwable exception, String errorPageLocation) {
        //Check if we're inside render response and if the response is committed
        if (context.getCurrentPhaseId() != PhaseId.RENDER_RESPONSE) {
            return true;
        } else if (!context.getExternalContext().isResponseCommitted()) {
            resetResponse(context); // If the exception was thrown in midst of rendering the JSF response, then reset (partial) response.
            return true;
        } else {
            logger.error(String.format(LOG_RENDER_EXCEPTION_UNHANDLED, errorPageLocation), exception);
            return false;
        }
    }

}
