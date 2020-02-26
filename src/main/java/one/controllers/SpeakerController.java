package one.controllers;

import one.persistence.entity.User;
import one.servises.controllerservices.impl.OfferReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/speaker")
public class SpeakerController {
    @Autowired
    private OfferReportService offerService;

    @PostMapping("/offerReport")
    public String offerReport(@RequestParam(name = "theme") String theme,
                              Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        offerService.setTheme(theme);
        offerService.setUser(user);
        String result = offerService.handle();
        model.addAttribute("message", result);
        return "forward:/cabinet/mainPage";
    }
}
