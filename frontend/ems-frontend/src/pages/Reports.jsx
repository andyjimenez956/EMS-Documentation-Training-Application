import React, { useEffect, useMemo, useState } from "react";
import { api } from "../api/http";

function fmt(v) {
    if (!v) return "";
    try {
        return new Date(v).toLocaleString();
    } catch {
        return String(v);
    }
}

function normalizeReport(r) {
    if (!r) return null;

    const title = r.title || "Report";
    const generatedAt = r.generatedAt || r.generated_at || r.generated || r.timestamp || null;

    const columns = Array.isArray(r.columns) ? r.columns : [];
    const rows = Array.isArray(r.rows) ? r.rows : [];

    return { title, generatedAt, columns, rows };
}

export default function Reports() {
    const [reportKey, setReportKey] = useState("users");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [report, setReport] = useState(null);

    const endpoint = useMemo(() => {
        if (reportKey === "users") return "/api/reports/users";
        if (reportKey === "attemptsByScenario") return "/api/reports/attempts-by-scenario";
        if (reportKey === "attemptsByStatus") return "/api/reports/attempts-by-status";
        return "/api/reports/users";
    }, [reportKey]);

    async function run() {
        setLoading(true);
        setError("");
        try {
            const r = await api(endpoint);
            setReport(normalizeReport(r));
        } catch (e) {
            setReport(null);
            setError(e.message);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        run();
    }, [endpoint]);

    return (
        <div style={{ display: "grid", gap: 12 }}>
            <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
                <h3 style={{ margin: 0, flex: 1 }}>Reports</h3>
                <button onClick={run} style={{ padding: 8 }}>
                    Refresh
                </button>
            </div>

            <div style={{ display: "grid", gap: 10, padding: 12, border: "1px solid #ddd", borderRadius: 10 }}>
                <div style={{ display: "grid", gridTemplateColumns: "160px 1fr", gap: 10, alignItems: "center" }}>
                    <div>Report</div>
                    <select value={reportKey} onChange={(e) => setReportKey(e.target.value)} style={{ padding: 8 }}>
                        <option value="users">Users Report</option>
                        <option value="attemptsByScenario">Attempts by Scenario</option>
                        <option value="attemptsByStatus">Attempts by Status</option>
                    </select>
                </div>
                <div style={{ opacity: 0.85 }}>Endpoint: {endpoint}</div>
            </div>

            {loading && <div>Loading...</div>}
            {error && <div style={{ color: "crimson" }}>{error}</div>}

            {report && (
                <div style={{ display: "grid", gap: 10 }}>
                    <div style={{ padding: 12, border: "1px solid #ddd", borderRadius: 10 }}>
                        <div style={{ fontWeight: 800, fontSize: 18 }}>{report.title}</div>
                        <div style={{ opacity: 0.85 }}>Generated: {fmt(report.generatedAt)}</div>
                        <div style={{ opacity: 0.85 }}>Rows: {report.rows.length}</div>
                    </div>

                    <div style={{ border: "1px solid #ddd", borderRadius: 10, overflow: "hidden" }}>
                        <div
                            style={{
                                display: "grid",
                                gridTemplateColumns: `repeat(${Math.max(report.columns.length, 1)}, minmax(120px, 1fr))`,
                                background: "#f6f6f6",
                                padding: 10,
                                fontWeight: 700
                            }}
                        >
                            {report.columns.length ? report.columns.map((c) => <div key={c}>{c}</div>) : <div>Columns</div>}
                        </div>

                        {report.rows.map((row, idx) => (
                            <div
                                key={idx}
                                style={{
                                    display: "grid",
                                    gridTemplateColumns: `repeat(${Math.max(report.columns.length, 1)}, minmax(120px, 1fr))`,
                                    padding: 10,
                                    borderTop: "1px solid #eee"
                                }}
                            >
                                {(Array.isArray(row) ? row : []).map((cell, cidx) => (
                                    <div key={cidx}>{cell === null || cell === undefined ? "" : String(cell)}</div>
                                ))}
                            </div>
                        ))}

                        {report.rows.length === 0 && <div style={{ padding: 12 }}>No rows returned.</div>}
                    </div>
                </div>
            )}
        </div>
    );
}
