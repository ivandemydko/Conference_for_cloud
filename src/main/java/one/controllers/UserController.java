package one.controllers;

import one.persistence.entity.User;
import one.servises.managers.userManager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserManager userManager;


    @PostMapping("/registration")
    public ModelAndView registration(@RequestParam(name = "reportId") String reportId,
                                     HttpSession session) {
        User user = (User) session.getAttribute("user");
        String result = userManager.registerToConference(user, Long.parseLong(reportId));
        ModelAndView mv = new ModelAndView();
        if (!result.equals("success")) {
            mv.addObject("message", result);
            mv.addObject("reportId", Integer.parseInt(reportId));
            mv.setViewName("forward:/cabinet/futureReportsPage");
            return mv;
        }
        mv.setViewName("redirect:/cabinet/futureReports");
        return mv;
    }
}
