package one.servises.controllerservices.impl;


import one.persistence.data.IReport;
import one.persistence.entity.Report;
import one.servises.controllerservices.ControllerService;
import one.servises.managers.parameterManager.ParameterManager;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AddPresenceService implements ControllerService {
  //  private Logger logger = Logger.getLogger(AddPresenceService.class);
    @Autowired
    private ParameterManager pm;
    @Autowired
    IReport iReport;
    private Page<Report> page;

    private String index;
    private String presence;


    @Override
    public String handle() {
        if (!pm.isNumberCorrect(presence)) {
          //  logger.info("Number was input incorrectly: " + presence);
            return "errorNumber";
        }
        Report report = page.get().collect(Collectors.toList()).get(Integer.parseInt(index));
        report.setPresence(Integer.parseInt(presence));
        iReport.saveReport(report);
     //   logger.info("Was added presence  " + presence + " to report with id " + report.getId());

        return "successfulChanges";
    }

    public void setPage(Page<Report> page) {
        this.page = page;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setPresence(String presence) {
        this.presence = presence;
    }
}
