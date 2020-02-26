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
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;

@Service
public class EditReportService implements ControllerService {
   // private Logger logger = Logger.getLogger(EditReportService.class);
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

    private String sDate;
    private String sTime;
    private String city;
    private String street;
    private String building;
    private String room;
    private Report report;

    public EditReportService() {
    }

    @Override
    public String handle() {
        if (parameterManager.isEmpty(sDate, sTime, city, street, building, room)) {
         //   logger.info("Form was not filled out");
            return "errorEmptyForm";
        }
        Address address = new Address(city, street, building, room);

        if (!parameterManager.isAddressCorrect(address)) {
           // logger.info("Address was imputed incorrectly");
            return "errorAddress";
        }

        Address dbAddress = iAddress.findAddress(city, street, building, room);
        address = (dbAddress != null) ? dbAddress : address;

        Date date = dateTimeManager.fromStringToSqlDate(sDate);
        Time time = dateTimeManager.fromStringToTime(sTime);

        report.setTime(time);
        report.setDate(date);
        report.setAddress(address);

        iReport.saveReport(report);
        Speaker speaker = report.getSpeaker();
        mail.notifySpeakerAppointment(speaker, report);

       // logger.info("The report was successfully edited");
        return "successfulChanges";
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

    public void setReport(Report report) {
        this.report = report;
    }

    public Report getReport() {
        return report;
    }
}
