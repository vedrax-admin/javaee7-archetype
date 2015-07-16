package ${package}.util;

import java.security.Principal;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.el.ELContext;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author VEDRAX SAS
 */
public final class Faces {

    private Faces() {
    }

    public static FacesContext getContext() {
        return FacesContext.getCurrentInstance();
    }

    public static FacesContext getContext(ELContext elContext) {
        return (FacesContext) elContext.getContext(FacesContext.class);
    }

    public static boolean hasContext() {
        return getContext() != null;
    }

    public static ExternalContext getExternalContext() {
        return getContext().getExternalContext();
    }

    public static Application getApplication() {
        return getContext().getApplication();
    }

    public static ELContext getELContext() {
        return getContext().getELContext();
    }

    public static ViewHandler getViewHandler() {
        return getApplication().getViewHandler();
    }

    public static void navigationHandler(FacesContext context, String page) {
        context.getApplication().getNavigationHandler().handleNavigation(context, null, page);
    }

    public static ResourceBundle getBundle(String path) {
        FacesContext ctx = getContext();
        return ctx.getApplication().getResourceBundle(ctx, path);
    }

    public static String getBundleKey(ResourceBundle bundle, String key) {
        String value = null;
        try {
            if (bundle != null) {
                value = bundle.getString(key);
            }
        } catch (MissingResourceException ex) {
            value = key + " not found in resource bundle";
        }
        return value;
    }

    // JSF views ------------------------------------------------------------------------------------------------------
    public static UIViewRoot getViewRoot() {
        return getContext().getViewRoot();
    }

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }
    
    

    public static SelectItem[] getSelectItems(Object[] entities, boolean selectOne) {
        int size = selectOne ? entities.length + 1 : entities.length;
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i] = new SelectItem(x, x.toString());
            i++;
        }
        return items;
    }

    public static HttpServletRequest getRequest(FacesContext context) {
        return (HttpServletRequest) context.getExternalContext().getRequest();
    }

    public static boolean isAjaxRequest(FacesContext context) {
        return context.getPartialViewContext().isAjaxRequest();
    }

    public static boolean isUserInRole(FacesContext context, String role) {
        return context.getExternalContext().isUserInRole(role);
    }

    public static Principal getPrincipal(FacesContext context) {
        return context.getExternalContext().getUserPrincipal();
    }

    public static String getUsername(FacesContext context) {
        Principal principal = context.getExternalContext().getUserPrincipal();
        String username = null;
        if (principal != null) {
            username = principal.getName();
        }
        return username;
    }

    public static void invalidateSession(FacesContext context) {
        context.getExternalContext().invalidateSession();
    }

    public static FacesMessage create(FacesMessage.Severity severity, String message) {
        return new FacesMessage(severity, message, null);
    }

    public static FacesMessage createInfo(String message) {
        return create(FacesMessage.SEVERITY_INFO, message);
    }
    
    public static FacesMessage createWarn(String message){
        return create(FacesMessage.SEVERITY_WARN,message);
    }
    
    public static FacesMessage createError(String message){
        return create(FacesMessage.SEVERITY_ERROR,message);
    }
    
    public static FacesMessage createFatal(String message){
        return create(FacesMessage.SEVERITY_FATAL,message);
    }
    
    public static void add(String clientId,FacesMessage message){
        getContext().addMessage(clientId, message);
    }
    
    public static void add(FacesMessage.Severity severity,String clientId,String message){
        add(clientId,create(severity,message));
    }
    
    public static void addInfo(String clientId,String message){
        add(clientId,createInfo(message));
    }
    
    public static void addWarn(String clientId,String message){
        add(clientId,createWarn(message));
    }
    
    public static void addError(String clientId,String message){
        add(clientId,createError(message));
    }
    
    public static void addFatal(String clientId,String message){
        add(clientId,createFatal(message));
    }
    
    public static void addGlobal(FacesMessage message){
        add(null,message);
    }
    
    public static void addGlobal(FacesMessage.Severity severity,String message){
        addGlobal(create(severity,message));
    }
    
    public static void addGlobalInfo(String message){
        addGlobal(createInfo(message));
    }
    
    public static void addGlobalWarn(String message){
        addGlobal(createWarn(message));
    }
    
    public static void addGlobalError(String message){
        addGlobal(createError(message));
    }
    
    public static void addGlobalFatal(String message){
        addGlobal(createFatal(message));
    }
    
     public static HttpSession getSession(boolean create) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.getSession(create);
    }

    public static boolean hasSession() {
        return getSession(false) != null;
    }

    public static String getSessionAttribute(String attribute) {
        HttpSession session = getSession(false);
        String result = null;
        if (session != null) {
            if (session.getAttribute(attribute) != null) {
                result = (String) session.getAttribute(attribute);
            }
        }
        return result;
    }
}

