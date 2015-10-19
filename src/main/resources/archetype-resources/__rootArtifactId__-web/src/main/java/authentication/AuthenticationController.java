package ${package}.authentication;

//import com.vedrax.model.Agent;
//import com.vedrax.service.GlobalEntityService;
import com.vedrax.util.Faces;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author penchenatremy
 */
@ManagedBean
@SessionScoped
public class AuthenticationController implements Serializable {

    private static final long serialVersionUID = 1L;
    private final static String AUTHENTICATED = "authenticated";
    private final static String IDENTIFICATION = "currentUserId";
    private final static String SECURITY_ROLE = "securityRole";

    private String mail = null;
    private String password = null;
    private boolean authenticated = false;
    //private Agent user;

    //@EJB
    //private GlobalEntityService entityService;

    public AuthenticationController() {
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*
    public Agent getUser() {
        return user;
    }

    public void setUser(Agent user) {
        this.user = user;
    }
    */

    public boolean isAuthenticated() {
        if (getSession().getAttribute(AUTHENTICATED) != null) {
            boolean auth = (Boolean) getSession().getAttribute(AUTHENTICATED);
            if (auth) {
                authenticated = true;
            }
        } else {
            authenticated = false;
        }
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    private HttpSession getSession() {
        return Faces.getSession(true);
    }

    public String login() {
        //Get session
        HttpSession session = getSession();
        String result = null;
        try {
            //String result = entityService.validateUser(mail, password);

            if (result != null) {
                //get session attributes
                session.setAttribute(IDENTIFICATION, mail);
                session.setAttribute(SECURITY_ROLE, result);
                //set authenticated to true
                setAuthenticated(true);
                session.setAttribute(AUTHENTICATED, true);

                //setUser(entityService.findAgentByMail(mail));
                return "/secure/index?faces-redirect=true";
            } else {
                //set authenticated to false
                setAuthenticated(false);
                setMail(null);
                //setUser(null);
                session.setAttribute(AUTHENTICATED, false);
                Faces.addGlobalWarn("Invalid Login! Please Try again.");
                return null;
            }
        } finally {
            setPassword(null);
        }
    }

    public String logout() {
        //setUser(null);
        setAuthenticated(false);
        setMail(null);
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        externalContext.invalidateSession();
        return "/login?faces-redirect=true";
    }

}
