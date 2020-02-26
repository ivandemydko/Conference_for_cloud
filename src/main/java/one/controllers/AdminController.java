package one.controllers;

import one.persistence.data.IReport;
import one.persistence.entity.Report;
import one.persistence.entity.User;
import one.servises.controllerservices.impl.AddBonusesService;
import one.servises.controllerservices.impl.AddPresenceService;
import one.servises.controllerservices.impl.AddSpeakerRatingService;
import one.servises.controllerservices.impl.AssignPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AssignPositionService positionService;
    @Autowired
    private AddBonusesService bonusesService;
    @Autowired
    private AddSpeakerRatingService ratingService;
    @Autowired
    private AddPresenceService presenceService;
    @Autowired
    private IReport iReport;

    @PostMapping("/assignToPosition")
    public String assignToPosition(@RequestParam(name = "email") String email,
                                   @RequestParam(name = "userType") String userType,
                                   Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        positionService.setCurrentUser(user);
        positionService.setEmail(email);
        positionService.setUserType(userType);
        String result = positionService.handle();
        session.setAttribute("user", positionService.getCurrentUser());
        model.addAttribute("userType", userType);
        model.addAttribute("message", result);
        return "forward:/cabinet/mainPage";
    }

    @PostMapping("/addBonuses")
    public String addBonuses(@RequestParam(name = "email") String email,
                             @RequestParam(name = "bonuses") String bonuses,
                             Model model) {
        bonusesService.setEmail(email);
        bonusesService.setBonuses(bonuses);
        String result = bonusesService.handle();
        model.addAttribute("message", result);
        return "forward:/cabinet/mainPage";
    }

    @PostMapping("/deletePastReport")
    public String deletePastReport(@RequestParam(name = "reportId") String reportId) {
        iReport.deleteReportById(Long.parseLong(reportId));
        return "redirect:/cabinet/pastReports";
    }

    @PostMapping("/addPresence")
    public String addPresence(@RequestParam(name = "index") String index,
                              @RequestParam(name = "presence") String presence,
                              HttpSession session, Model model) {
        Page<Report> page = (Page<Report>) session.getAttribute("pastReports");
        presenceService.setIndex(index);
        presenceService.setPresence(presence);
        presenceService.setPage(page);
        String result = presenceService.handle();
        model.addAttribute("message", result);
        return "forward:/cabinet/pastReportsPage";
    }

    @PostMapping("/addSpeakerRating")
    public String addSpeakerRating(@RequestParam(name = "email") String email,
                                   @RequestParam(name = "rating") String rating, Model model) {
        ratingService.setEmail(email);
        ratingService.setRating(rating);
        String result = ratingService.handle();
        model.addAttribute("message", result);
        return "forward:/cabinet/mainPage";
    }
}
