package edu.wgu.d.emsbackend.report;

import java.time.LocalDateTime;
import java.util.List;

public class ReportResponse {

    private String title;
    private LocalDateTime generatedAt;
    private List<String> columns;
    private List<List<String>> rows;

    public ReportResponse(String title, LocalDateTime generatedAt, List<String> columns, List<List<String>> rows) {
        this.title = title;
        this.generatedAt = generatedAt;
        this.columns = columns;
        this.rows = rows;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<List<String>> getRows() {
        return rows;
    }
}
