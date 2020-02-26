package one.servises.controllerservices.impl;

import one.persistence.data.IPosition;
import one.persistence.entity.Language;
import one.persistence.entity.Position;
import one.persistence.entity.Speaker;
import one.persistence.entity.User;
import one.servises.controllerservices.ControllerService;
import one.servises.managers.languageManager.LanguageManager;
import one.servises.managers.parameterManager.ParameterManager;
import one.servises.managers.userManager.UserManager;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class RegisterService implements ControllerService {
  //  private Logger logger = Logger.getLogger(RegisterService.class);
    @Autowired
    private ParameterManager pm;
    @Autowired
    UserManager userManager;
    @Autowired
    LanguageManager languageManager;
    @Autowired
    IPosition iPosition;


    private String email;
    private String password;
    private String name;
    private String surname;
    private String position;
    private Locale locale;
    private User user;

    public RegisterService(String email, String password, String name, String surname, String position, Locale locale) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.position = position;
        this.locale = locale;
    }

    public RegisterService() {
    }

    @Override
    public String handle() {
        if (pm.isEmpty(name, surname, position)) {
         //   logger.info("Form was not filled out");
            return "errorEmptyForm";
        }
        if (!pm.isEmailCorrect(email)) {
          //  logger.info("Email was imputed incorrectly: " + email);
            return "errorEmailForm";
        }
        if (!pm.isPasswordCorrect(password)) {
           // logger.info("Password was imputed incorrectly" + password);
            return "errorPassword";
        }
        if (!pm.isNameAndSurnameCorrect(name, surname)) {
          //  logger.info("Name or surname were imputed incorrectly" + name + " " + surname);
            return "errorNameOrSurname";
        }
        if (pm.isUserExist(email)) {
          //  logger.info("User intended to register under email " + email + " that already exists in system");
            return "errorUserExists";
        }
        Language lang = languageManager.getLanguage(locale);
        Position pos = iPosition.getPosition(position);
        if (position.equals("Speaker")) {
            user = new Speaker(name, surname, email, password, pos, lang, 0, 0);
            user = userManager.saveUser(user);
        } else {
            user = new User(name, surname, email, password, pos, lang);
            user = userManager.saveUser(user);
        }
        user.setPassword(null);
      //  logger.info("Registration of user with email: " + email + " was successful.");
        return "success";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public User getUser() {
        return user;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

}
