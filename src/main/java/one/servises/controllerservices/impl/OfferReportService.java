package one.servises.controllerservices.impl;


import one.persistence.data.IReport;
import one.persistence.entity.Report;
import one.persistence.entity.Speaker;
import one.persistence.entity.User;
import one.servises.controllerservices.ControllerService;
import one.servises.managers.parameterManager.ParameterManager;
import one.servises.managers.spaekerManager.SpeakerManager;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfferReportService implements ControllerService {
    @Autowired
    ParameterManager pm;
    @Autowired
    SpeakerManager speakerManager;
    @Autowired
    IReport iReport;
  //  private Logger logger = Logger.getLogger(OfferReportService.class);
    private String theme;
    private User user;

    public OfferReportService(String theme, User user) {
        this.theme = theme;
        this.user = user;
    }

    public OfferReportService() {
    }

    @Override
    public String handle() {

        if (theme == null || theme.isEmpty()) {
         //   logger.info("No action done.");
            return "noActionDone";
        }
        if (!pm.isThemeCorrect(theme)) {
           // logger.info("Selected incorrect name of theme:" + theme);
            return "errorTheme";
        }

//        Speaker speaker = one.managers.speakerManager.getSpeakerById(user.getId());
        Report report = new Report();
        report.setSpeaker((Speaker) user);
        report.setName(theme);
        iReport.saveReport(report);
       // logger.info("Was added new theme:" + theme);
//        ReportManager reportManager = new ReportManager();
//        int result = reportManager.addReport(theme, speaker);
//
//        if (result != 0) {
//            logger.info("Was added new theme:" + theme);
//            return "successfulChanges";
//        }
//        logger.info("Was nod added new theme:" + theme);
        return "successfulChanges";
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
