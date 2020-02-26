package one.servises.controllerservices.impl;


import one.persistence.data.IAddress;
import one.persistence.data.IReport;
import one.persistence.entity.Address;
import one.persistence.entity.Report;
import one.persistence.entity.Speaker;
import one.persistence.entity.User;
import one.servises.controllerservices.ControllerService;
import one.servises.managers.dateTimeManager.DateTimeManager;
import one.servises.managers.mailManager.MailManager;
import one.servises.managers.parameterManager.ParameterManager;
import one.servises.managers.spaekerManager.SpeakerManager;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.sql.Time;
import java.util.Set;

@Service
public class UpdateReportService implements ControllerService {
    @Autowired
    SpeakerManager speakerManager;
    @Autowired
    ParameterManager parameterManager;
    @Autowired
    DateTimeManager dateTimeManager;
    @Autowired
    MailManager mail;
    @Autowired
    IReport iReport;
    @Autowired
    IAddress iAddress;

  //  private Logger logger = Logger.getLogger(UpdateReportService.class);
    private String theme;
    private String sDate;
    private String sTime;
    private String city;
    private String street;
    private String building;
    private String room;
    private String email;
    private Report report;

    public UpdateReportService() {
    }

    @Override
    public String handle() {
        if (parameterManager.isAllEmpty(theme, sDate, sTime, city, street, building, room, email)) {
         //   logger.info("No action done");
            return "noActionDone";
        }
        Report oldReport = report;
        Speaker newSpeaker;
        if (!email.isEmpty()) {
            newSpeaker = speakerManager.getSpeakerByEmail(email);
            if (newSpeaker == null) {
              //  logger.info("Speaker with such " + email + " email does not exist");
                return "errorSpeakerNotExists";
            }
        } else {
            newSpeaker = oldReport.getSpeaker();
        }

        Speaker oldSpeaker = oldReport.getSpeaker();
        Address oldAddress = oldReport.getAddress();

        String newTheme = theme.isEmpty() ? oldReport.getName() : theme;
        String newCity = city.isEmpty() ? oldAddress.getCity() : city;
        String newStreet = street.isEmpty() ? oldAddress.getStreet() : street;
        String newBuilding = building.isEmpty() ? oldAddress.getBuilding() : building;
        String newRoom = room.isEmpty() ? oldAddress.getRoom() : room;
        Address newAddress = new Address(newCity, newStreet, newBuilding, newRoom);
        Date date = sDate.isEmpty() ? dateTimeManager.fromUtilDateToSqlDate(oldReport.getDate()) : dateTimeManager.fromStringToSqlDate(sDate);
        Time time = sTime.isEmpty() ? oldReport.getTime() : dateTimeManager.fromStringToTime(sTime);

        if (new java.util.Date().getTime() > date.getTime()) {
           // logger.info("The date was imputed incorrectly");
            return "errorDate";
        }
        if (!parameterManager.isAddressCorrect(newAddress)) {
          //  logger.info("Address was imputed incorrectly");
            return "errorAddress";
        }

        if (newAddress.isEqual(oldAddress)) {
            newAddress = oldAddress;
        } else {
            Address dbAddress = iAddress.findAddress(city, street, building, room);
            newAddress = (dbAddress != null) ? dbAddress : newAddress;
        }

        if (!parameterManager.isThemeCorrect(newTheme)) {
           // logger.info("Selected incorrect name of theme: " + theme);
            return "errorTheme";
        }

        Long reportId = oldReport.getId();
        Report newReport = new Report();
        newReport.setId(reportId);
        newReport.setName(newTheme);
        newReport.setAddress(newAddress);
        newReport.setSpeaker(newSpeaker);
        newReport.setDate(date);
        newReport.setTime(time);
        newReport.setUserList(oldReport.getUserList());

        report = iReport.saveReport(newReport);
        Set<User> userList = report.getUserList();

        if (!email.isEmpty() && parameterManager.isAllEmpty(theme, sDate, sTime, city, street, building, room)) {
            mail.notifySpeakerAppointment(newSpeaker, oldReport);
            mail.notifySpeakerDismiss(oldSpeaker, oldReport);
        } else if (!email.isEmpty() && !parameterManager.isAllEmpty(theme, sDate, sTime, city, street, building, room)) {
            mail.notifySpeakerAppointment(newSpeaker, newReport);
            mail.notifySpeakerDismiss(oldSpeaker, oldReport);
            mail.notifyChangeConference(newReport, oldReport, userList);
        } else {
            mail.notifySpeakerChangeConference(newReport, oldReport);
            mail.notifyChangeConference(newReport, oldReport, userList);
        }
       // logger.info("Report with id: " + reportId + "  was successfully updated ");
        return "successfulChanges";
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setDate(String sDate) {
        this.sDate = sDate;
    }

    public void setTime(String sTime) {
        this.sTime = sTime;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Report getReport() {
        return report;
    }

}
