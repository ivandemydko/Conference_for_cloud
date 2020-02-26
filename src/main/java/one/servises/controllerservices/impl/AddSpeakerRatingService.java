package one.servises.controllerservices.impl;


import one.persistence.entity.Speaker;
import one.servises.controllerservices.ControllerService;
import one.servises.managers.parameterManager.ParameterManager;
import one.servises.managers.spaekerManager.SpeakerManager;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddSpeakerRatingService implements ControllerService {
    @Autowired
   private ParameterManager parameterManager;
    @Autowired
   private SpeakerManager speakerManager;

   // private Logger logger = Logger.getLogger(AddSpeakerRatingService.class);
    private String rating;
    private String email;

    public AddSpeakerRatingService(String rating, String email) {
        this.rating = rating;
        this.email = email;
    }

    public AddSpeakerRatingService() {
    }

    @Override
    public String handle() {
        if (!parameterManager.isEmailCorrect(email)) {
          //  logger.info("Email was imputed incorrectly: " + email);
            return "errorEmailForm";
        }

        Speaker speaker = speakerManager.getSpeakerByEmail(email);
        if (speaker == null) {
          //  logger.info("Speaker with such " + email + " email does not exist");
            return "errorSpeakerNotExists";
        }

        speakerManager.addSpeakerRating(speaker, Integer.parseInt(rating));
       // logger.info("Was added rating " + rating + " to speaker " + speaker.getEmail());
        return "successfulChanges";
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
