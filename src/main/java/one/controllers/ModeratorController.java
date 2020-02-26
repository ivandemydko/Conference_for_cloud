package one.controllers;

import one.persistence.data.IReport;
import one.persistence.entity.Report;
import one.servises.controllerservices.impl.AddReportService;
import one.servises.controllerservices.impl.EditReportService;
import one.servises.controllerservices.impl.UpdateReportService;
import one.servises.managers.paginationManager.PaginationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/moderator")
public class ModeratorController {
    @Autowired
    private PaginationManager paginationManager;
    @Autowired
    private UpdateReportService updateReportService;
    @Autowired
    private EditReportService editReportService;
    @Autowired
    private AddReportService addReportService;
    @Autowired
    private IReport iReport;

    @GetMapping("/report")
    public String report(@RequestParam(name = "index") String index,
                         HttpSession session) {
        Page<Report> page = (Page<Report>) session.getAttribute("futureReports");
        Report report = page.get().collect(Collectors.toList()).get(Integer.parseInt(index));
        session.setAttribute("report", report);
        return "forward:updateReportPage";
    }

    @PostMapping("/addReport")
    public String addReport(@RequestParam(name = "date") String date,
                            @RequestParam(name = "time") String time,
                            @RequestParam(name = "theme") String theme,
                            @RequestParam(name = "city") String city,
                            @RequestParam(name = "street") String street,
                            @RequestParam(name = "building") String building,
                            @RequestParam(name = "room") String room,
                            @RequestParam(name = "speakerEmail") String speakerEmail,
                            Model model) {
        addReportService.setDate(date);
        addReportService.setTime(time);
        addReportService.setTheme(theme);
        addReportService.setCity(city);
        addReportService.setStreet(street);
        addReportService.setBuilding(building);
        addReportService.setRoom(room);
        addReportService.setEmail(speakerEmail);
        String result = addReportService.handle();
        model.addAttribute("message", result);
        return "forward:/cabinet/mainPage";
    }

    @PostMapping("/updateReport")
    public String updateReport(@RequestParam(name = "theme", required = false) String theme,
                               @RequestParam(name = "date", required = false) String date,
                               @RequestParam(name = "time", required = false) String time,
                               @RequestParam(name = "city", required = false) String city,
                               @RequestParam(name = "street", required = false) String street,
                               @RequestParam(name = "building", required = false) String building,
                               @RequestParam(name = "room", required = false) String room,
                               @RequestParam(name = "speakerEmail", required = false) String speakerEmail,
                               Model model, HttpSession session) {
        Report report = (Report) session.getAttribute("report");
        updateReportService.setReport(report);
        updateReportService.setTheme(theme);
        updateReportService.setDate(date);
        updateReportService.setTime(time);
        updateReportService.setCity(city);
        updateReportService.setStreet(street);
        updateReportService.setBuilding(building);
        updateReportService.setRoom(room);
        updateReportService.setEmail(speakerEmail);
        String result = updateReportService.handle();
        session.setAttribute("report", updateReportService.getReport());
        model.addAttribute("message", result);
        return "forward:updateReportPage";
    }

    @PostMapping("/editReport")
    public String editReport(@RequestParam(name = "date") String date,
                             @RequestParam(name = "time") String time,
                             @RequestParam(name = "city") String city,
                             @RequestParam(name = "street") String street,
                             @RequestParam(name = "building") String building,
                             @RequestParam(name = "room") String room,
                             Model model, HttpSession session) {
        Report report = (Report) session.getAttribute("offeredReport");
        editReportService.setReport(report);
        editReportService.setDate(date);
        editReportService.setTime(time);
        editReportService.setCity(city);
        editReportService.setStreet(street);
        editReportService.setBuilding(building);
        editReportService.setRoom(room);
        String result = editReportService.handle();
        model.addAttribute("message", result);
        return "forward:editReportPage";
    }


    @GetMapping("/offeredReports")
    public String offeredReports(HttpSession session, Pageable pageable,
                                 @RequestParam(name = "size", required = false) String size,
                                 @RequestParam(name = "page", required = false) String page) {
        Integer sessionPage = (Integer) session.getAttribute("offeredPage");
        Integer sessionSize = (Integer) session.getAttribute("offeredSize");
        paginationManager.setSessionPage(sessionPage);
        paginationManager.setSessionSize(sessionSize);
        paginationManager.setPageable(pageable);
        paginationManager.setRequestSize(size);
        paginationManager.setRequestPage(page);
        paginationManager.pagination("offered");
        session.setAttribute("offeredPage", paginationManager.getResultPage());
        session.setAttribute("offeredSize", paginationManager.getResultSize());
        session.setAttribute("offeredButtons", paginationManager.getButtons());
        session.setAttribute("offeredReports", paginationManager.getPage());
        return "offeredReports";
    }

    @PostMapping("/deleteOfferedReport")
    public String deletePastReport(@RequestParam(name = "reportId") String reportId) {
        iReport.deleteReportById(Long.parseLong(reportId));
        return "redirect:offeredReports";
    }

    @GetMapping("/offeredReport")
    public String offeredReport(@RequestParam(name = "index") String index,
                                HttpSession session) {
        Page<Report> page = (Page<Report>) session.getAttribute("offeredReports");
        Report report = page.get().collect(Collectors.toList()).get(Integer.parseInt(index));
        session.setAttribute("offeredReport", report);
        return "forward:editReportPage";
    }

    @RequestMapping("/updateReportPage")
    public String updateReportPage() {
        return "updateReport";
    }

    @RequestMapping("/editReportPage")
    public String editReportPage() {
        return "editReport";
    }

}
