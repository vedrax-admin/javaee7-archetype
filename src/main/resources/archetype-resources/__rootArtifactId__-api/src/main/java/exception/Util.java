package ${package}.exception;

import org.apache.commons.lang3.exception.ContextedRuntimeException;

/**
 *
 * @author VEDRAX SAS
 */
public class Util {

    public static void checkForBusinessException(ContextedRuntimeException busException) throws ContextedRuntimeException {
        if (busException.getContextEntries().size() > 0) {
            throw busException;
        }
    }

}
