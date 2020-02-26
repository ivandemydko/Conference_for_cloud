package one.servises.controllerservices.impl;


import one.persistence.entity.Speaker;
import one.servises.controllerservices.ControllerService;
import one.servises.managers.parameterManager.ParameterManager;
import one.servises.managers.spaekerManager.SpeakerManager;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddBonusesService implements ControllerService {
    @Autowired
    private ParameterManager pm;
    @Autowired
    private SpeakerManager speakerManager;

   // private Logger logger = Logger.getLogger(AddBonusesService.class);
    private String bonuses;
    private String email;

    public AddBonusesService(String bonuses, String email) {
        this.bonuses = bonuses;
        this.email = email;
    }

    public AddBonusesService() {
    }

    @Override
    public String handle() {

        if (!pm.isNumberCorrect(bonuses)) {
           // logger.info("Number was input incorrectly: " + bonuses);
            return "errorNumber";
        }
        if (!pm.isEmailCorrect(email)) {
          //  logger.info("Email was imputed incorrectly: " + email);
            return "errorEmailForm";
        }

        Speaker speaker = speakerManager.getSpeakerByEmail(email);
        if (speaker == null) {
           // logger.info("Speaker with such " + email + " email does not exist");
            return "errorSpeakerNotExists";
        }

        int result = speakerManager.setSpeakerBonuses(Integer.parseInt(bonuses), speaker);
        speaker.setBonuses(result);
        speakerManager.saveSpeaker(speaker);
       // logger.info("Were added " + bonuses + " bonuses to speaker " + speaker.getEmail());
        return "successfulChanges";
    }

    public void setBonuses(String bonuses) {
        this.bonuses = bonuses;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
