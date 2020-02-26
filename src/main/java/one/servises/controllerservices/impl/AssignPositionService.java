package one.servises.controllerservices.impl;


import one.persistence.data.IPosition;
import one.persistence.entity.Position;
import one.persistence.entity.User;
import one.servises.controllerservices.ControllerService;
import one.servises.managers.mailManager.MailManager;
import one.servises.managers.parameterManager.ParameterManager;
import one.servises.managers.userManager.UserManager;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AssignPositionService implements ControllerService {
    @Autowired
    ParameterManager parameterManager;
    @Autowired
    UserManager userManager;
    @Autowired
    MailManager mail;
    @Autowired
    IPosition iPosition;
  //  private Logger logger = Logger.getLogger(AssignPositionService.class);

    private String email;
    private User currentUser;
    private String userType;



    @Override
    public String handle() {

        if (!parameterManager.isEmailCorrect(email)) {
           // logger.info("Email was imputed incorrectly: " + email);
            return "errorEmailForm";
        }

        User user = userManager.getUserByEmail(email);
        if (user == null) {
           // logger.info("User with email: " + email + " does not exist");
            return "errorUser";
        }

        if (user.getPosition().getPosition().equals(userType)) {
          //  logger.info("User is already on position: " + userType);
            return "errorPosition";
        }

        Position position = iPosition.getPosition(userType);
        user = userManager.setUserPosition(user, position);

        if (currentUser.getEmail().equals(user.getEmail())) {
            currentUser = user;
        } else {
            mail.assignment(user);
        }

      //  logger.info("User with email: " + email + " was assign to position: " + position);
        return "successfulChanges";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
