package one.servises.controllerservices.impl;



import one.persistence.entity.Language;
import one.persistence.entity.User;
import one.servises.controllerservices.ControllerService;
import one.servises.managers.languageManager.LanguageManager;
import one.servises.managers.parameterManager.ParameterManager;
import one.servises.managers.userManager.UserManager;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements ControllerService {
   // private Logger logger = Logger.getLogger(LoginService.class);
    private String email;
    private String password;

    private User user;
    @Autowired
    private ParameterManager pm;
    @Autowired
    private UserManager userManager;
    @Autowired
    LanguageManager languageManager;


    public LoginService(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginService() {
    }

    @Override
    public String handle() {
        if (!pm.isEmailCorrect(email)) {
           // logger.info("Email was imputed incorrectly: " + email);
            return "errorEmailForm";
        }
        if (!pm.isPasswordCorrect(password)) {
          //  logger.info("Password was imputed incorrectly");
            return "errorPassword";
        }

        user = userManager.getUserByEmail(email);

        if (user == null || !password.equals(user.getPassword())) {
           // logger.info("User input incorrect dada to login");
            return "errorUserNotExists";
        }
        Language language = user.getLanguage();
        languageManager.setLanguageToSession(language);

       // logger.info("User with email: " + email + " successfully login");
        return "success";
    }

    public User getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
