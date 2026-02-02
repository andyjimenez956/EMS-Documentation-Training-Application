package edu.wgu.d.emsbackend.report;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/users")
    public ReportResponse usersReport() {
        return reportService.usersReport();
    }

    @GetMapping("/attempts-by-scenario")
    public ReportResponse attemptsByScenario() {
        return reportService.attemptsByScenarioReport();
    }

    @GetMapping("/attempts-by-status")
    public ReportResponse attemptsByStatus() {
        return reportService.attemptsByStatusReport();
    }
}
