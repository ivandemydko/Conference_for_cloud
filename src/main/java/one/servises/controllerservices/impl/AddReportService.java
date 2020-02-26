package one.servises.controllerservices.impl;


import one.persistence.data.IAddress;
import one.persistence.data.IReport;
import one.persistence.entity.Address;
import one.persistence.entity.Report;
import one.persistence.entity.Speaker;
import one.servises.controllerservices.ControllerService;
import one.servises.managers.dateTimeManager.DateTimeManager;
import one.servises.managers.mailManager.MailManager;
import one.servises.managers.parameterManager.ParameterManager;
import one.servises.managers.spaekerManager.SpeakerManager;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.sql.Time;

@Service
public class AddReportService implements ControllerService {
    @Autowired
    private ParameterManager parameterManager;
    @Autowired
    private DateTimeManager dateTimeManager;
    @Autowired
    private SpeakerManager speakerManager;
    @Autowired
    private IReport iReport;
    @Autowired
    private MailManager mail;
    @Autowired
    private IAddress iAddress;

  //  private Logger logger = Logger.getLogger(AddReportService.class);
    private String sDate;
    private String sTime;
    private String theme;
    private String city;
    private String street;
    private String building;
    private String room;
    private String email;

    public AddReportService(String sDate, String sTime,
                            String theme, String city, String street,
                            String building, String room, String email) {
        this.sDate = sDate;
        this.sTime = sTime;
        this.theme = theme;
        this.city = city;
        this.street = street;
        this.building = building;
        this.room = room;
        this.email = email;
    }

    public AddReportService() {
    }

    @Override
    public String handle() {

        if (parameterManager.isEmpty(sDate, sTime, theme, city, street, building, room, email)) {
           // logger.info("Form was not filled out");
            return "errorEmptyForm";
        }

        Date date = dateTimeManager.fromStringToSqlDate(sDate);
        if (new java.util.Date().getTime() > date.getTime()) {
         //   logger.info("Date was not input incorrectly" + date);
            return "errorDate";
        }

        if (!parameterManager.isThemeCorrect(theme)) {
           // logger.info("Selected incorrect name of theme: " + theme);
            return "errorTheme";
        }

        Speaker speaker = speakerManager.getSpeakerByEmail(email);
        if (speaker == null) {
           // logger.info("Speaker with such " + email + " email does not exist");
            return "errorSpeakerNotExists";
        }

        Address address = new Address(city, street, building, room);
        if (!parameterManager.isAddressCorrect(address)) {
           // logger.info("Address was imputed incorrectly");
            return "errorAddress";
        }

        Address dbAddress = iAddress.findAddress(city, street, building, room);
        if (dbAddress != null) {
            address = dbAddress;
        } else {
            address = iAddress.saveAddress(address);
        }

        Time time = dateTimeManager.fromStringToTime(sTime);
        Report report = new Report(theme, address, date, time, speaker);
        report = iReport.saveReport(report);

        if (report != null) {
            mail.notifySpeakerAppointment(speaker, report);
        }

      //  logger.info("Report was successfully added");
        return "successfulChanges";
    }

    public void setDate(String Date) {
        this.sDate = Date;
    }

    public void setTime(String Time) {
        this.sTime = Time;
    }

    public void setTheme(String theme) {
        this.theme = theme;
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
}
