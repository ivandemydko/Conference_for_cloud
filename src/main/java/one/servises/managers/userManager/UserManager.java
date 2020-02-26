package one.servises.managers.userManager;



import one.persistence.data.IReport;
import one.persistence.data.IUser;
import one.persistence.entity.Position;
import one.persistence.entity.Report;
import one.persistence.entity.Speaker;
import one.persistence.entity.User;
import one.servises.managers.mailManager.MailManager;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * This class is used for execute some logic for {@link User}
 */
@Service
public class UserManager {
   // private Logger logger = Logger.getLogger(UserManager.class);

    @Autowired
    IUser iUser;
    @Autowired
    IReport iReport;
    @Autowired
    MailManager mailManager;


    public User getUserByEmail(String email) {
        return iUser.findUserByEmail(email);
    }


    public User setUserPosition(User user, Position position) {
        if (position.getPosition().equals("Speaker")) {
            Speaker speaker = new Speaker();
            speaker.setName(user.getName());
            speaker.setSurname(user.getSurname());
            speaker.setPassword(user.getPassword());
            speaker.setEmail(user.getEmail());
            speaker.setLanguage(user.getLanguage());
            speaker.setPosition(position);
            speaker.setRating(0);
            speaker.setBonuses(0);
            return iUser.changeUsers(user, speaker);
        } else {
            User newUser = new User();
            newUser.setName(user.getName());
            newUser.setSurname(user.getSurname());
            newUser.setPassword(user.getPassword());
            newUser.setEmail(user.getEmail());
            newUser.setLanguage(user.getLanguage());
            newUser.setPosition(position);
            return iUser.changeUsers(user, newUser);
        }
    }

    public User saveUser(User user) {
        return iUser.saveUser(user);
    }

    public String registerToConference(User user, Long reportId) {
        for (Report r : user.getReportList()) {
            if (r.getId().equals(reportId)) {
                return "errorAlreadyRegistered";
            }
        }
        Optional<Report> optional = iReport.getReportBuId(reportId);
        Report report = optional.get();
        report.addUser(user);
        iReport.saveReport(report);
        user.addReport(report);
        mailManager.notifyUserRegistration(user, report);
      //  logger.info("User  " + user.getEmail() + " has successfully registered on conference with id " + report.getId());
        return "success";
    }

    /**
     * Checks if {@link User} registered for certain {@link Report}
     */
    public void checkUserRegistration(User user, Page<Report> pages) {
        for (Report r : pages) {
            for (Report report : user.getReportList()) {
                if (report.getId().equals(r.getId())) {
                    r.setIsUserRegistered(true);
                }
            }
        }
    }
}
