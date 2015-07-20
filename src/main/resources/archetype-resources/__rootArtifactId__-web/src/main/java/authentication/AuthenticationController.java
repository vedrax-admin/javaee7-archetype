package ${package}.authentication;

//import com.vedrax.model.User;
//import com.vedrax.service.UserFacade;
import com.vedrax.util.Faces;
import java.io.Serializable;
import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author VEDRAX SAS
 */
@ManagedBean
@SessionScoped
public class AuthenticationController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public String logout() {
        return null;
    }
    
}
