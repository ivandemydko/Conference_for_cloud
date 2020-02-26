package one.controllers;

import one.persistence.entity.User;
import one.servises.managers.paginationManager.PaginationManager;
import one.servises.managers.userManager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cabinet")
public class CabinetController {
    @Autowired
    private  PaginationManager paginationManager;
    @Autowired
    private UserManager userManager;

    @RequestMapping("/mainPage")
    public String cabinet() {
        return "cabinet";
    }

    @RequestMapping("/futureReportsPage")
    public String futureReportsPage() {
        return "futureReports";
    }

    @RequestMapping("/pastReportsPage")
    public String pastReportsPage() {
        return "pastReports";
    }

    @GetMapping("/futureReports")
    public String futureReports(HttpSession session, Pageable pageable,
                                @RequestParam(name = "size", required = false) String size,
                                @RequestParam(name = "page", required = false) String page) {
        Integer sessionPage = (Integer) session.getAttribute("futurePage");
        Integer sessionSize = (Integer) session.getAttribute("futureSize");
        paginationManager.setSessionPage(sessionPage);
        paginationManager.setSessionSize(sessionSize);
        paginationManager.setPageable(pageable);
        paginationManager.setRequestSize(size);
        paginationManager.setRequestPage(page);
        paginationManager.pagination("future");
        User user = (User) session.getAttribute("user");
        userManager.checkUserRegistration(user,paginationManager.getPage());
        session.setAttribute("futurePage", paginationManager.getResultPage());
        session.setAttribute("futureSize", paginationManager.getResultSize());
        session.setAttribute("futureButtons", paginationManager.getButtons());
        session.setAttribute("futureReports", paginationManager.getPage());
        return "futureReports";
    }

    @GetMapping("/pastReports")
    public String pastReports(HttpSession session, Pageable pageable,
                                @RequestParam(name = "size", required = false) String size,
                                @RequestParam(name = "page", required = false) String page) {
        Integer sessionPage = (Integer) session.getAttribute("pastPage");
        Integer sessionSize = (Integer) session.getAttribute("pastSize");
        paginationManager.setSessionPage(sessionPage);
        paginationManager.setSessionSize(sessionSize);
        paginationManager.setPageable(pageable);
        paginationManager.setRequestSize(size);
        paginationManager.setRequestPage(page);
        paginationManager.pagination("past");
        session.setAttribute("pastPage", paginationManager.getResultPage());
        session.setAttribute("pastSize", paginationManager.getResultSize());
        session.setAttribute("pastButtons", paginationManager.getButtons());
        session.setAttribute("pastReports", paginationManager.getPage());
        return "pastReports";
    }

}
