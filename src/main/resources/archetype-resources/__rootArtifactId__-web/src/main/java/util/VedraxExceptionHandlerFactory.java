package ${package}.util;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 *
 * @author penchenatremy
 */
public class VedraxExceptionHandlerFactory extends ExceptionHandlerFactory {

    private final ExceptionHandlerFactory wrapped;

    /**
     * Construct a new custom exception handler factory around the given wrapped
     * factory
     *
     * @param wrapped The wrapped factory
     */
    public VedraxExceptionHandlerFactory(ExceptionHandlerFactory wrapped) {
        this.wrapped = wrapped;
    }

    /**
     *
     * Returns a new instance of CustomExceptionHandler which wraps the original
     * exception handler
     *
     * @return
     */
    @Override
    public ExceptionHandler getExceptionHandler() {
        return new VedraxExceptionHandler(wrapped.getExceptionHandler());
    }

}
