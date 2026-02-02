import React, { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import { api } from "../api/http";

const STUDENT_ID = "0993ccb9-6ae6-4bce-80f2-5a1ffb1aabe1";

function fmt(v) {
    if (!v) return "";
    try {
        return new Date(v).toLocaleString();
    } catch {
        return String(v);
    }
}

export default function StudentMyReports() {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [status, setStatus] = useState("");
    const [scenarioId, setScenarioId] = useState("");
    const [q, setQ] = useState("");

    async function load() {
        setLoading(true);
        setError("");
        try {
            const data = await api("/api/attempts");
            const all = Array.isArray(data) ? data : [];
            setItems(all.filter((a) => a.studentId === STUDENT_ID));
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        load();
    }, []);

    const filtered = useMemo(() => {
        const qq = q.trim().toLowerCase();
        return items
            .filter((a) => (status ? a.status === status : true))
            .filter((a) => (scenarioId.trim() ? a.scenarioId === scenarioId.trim() : true))
            .filter((a) => {
                if (!qq) return true;
                const hay = [
                    a.patientFirstName,
                    a.patientLastName,
                    a.chiefComplaint,
                    a.disposition,
                    a.narrativeText,
                    a.assessmentNotes,
                    a.interventions
                ]
                    .filter(Boolean)
                    .join(" ")
                    .toLowerCase();
                return hay.includes(qq);
            })
            .sort((x, y) => {
                const ax = x.created ? new Date(x.created).getTime() : 0;
                const ay = y.created ? new Date(y.created).getTime() : 0;
                const bx = x.submittedAt ? new Date(x.submittedAt).getTime() : 0;
                const by = y.submittedAt ? new Date(y.submittedAt).getTime() : 0;
                return (by || ay) - (bx || ax);
            });
    }, [items, status, scenarioId, q]);

    return (
        <div style={{ display: "grid", gap: 12 }}>
            <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
                <h3 style={{ margin: 0, flex: 1 }}>My Reports</h3>
                <Link to="/student/new">New Report</Link>
                <button onClick={load} style={{ padding: 8 }}>
                    Refresh
                </button>
            </div>

            <div style={{ display: "grid", gap: 10, padding: 12, border: "1px solid #ddd", borderRadius: 10 }}>
                <div style={{ fontWeight: 700 }}>Filters</div>

                <div style={{ display: "grid", gridTemplateColumns: "160px 1fr", gap: 10, alignItems: "center" }}>
                    <div>Status</div>
                    <select value={status} onChange={(e) => setStatus(e.target.value)} style={{ padding: 8 }}>
                        <option value="">All</option>
                        <option value="DRAFT">DRAFT</option>
                        <option value="SUBMITTED">SUBMITTED</option>
                    </select>

                    <div>Scenario ID</div>
                    <input value={scenarioId} onChange={(e) => setScenarioId(e.target.value)} style={{ padding: 8 }} />

                    <div>Search text</div>
                    <input value={q} onChange={(e) => setQ(e.target.value)} style={{ padding: 8 }} />
                </div>

                <div>Results: {filtered.length}</div>
            </div>

            {loading && <div>Loading...</div>}
            {error && <div style={{ color: "crimson" }}>{error}</div>}

            <div style={{ border: "1px solid #ddd", borderRadius: 10, overflow: "hidden" }}>
                <div style={{ display: "grid", gridTemplateColumns: "140px 1fr 260px 160px", gap: 0, background: "#f6f6f6", padding: 10, fontWeight: 700 }}>
                    <div>Status</div>
                    <div>Patient / Complaint</div>
                    <div>Submitted</div>
                    <div>Open</div>
                </div>

                {filtered.map((a) => (
                    <div
                        key={a.id}
                        style={{
                            display: "grid",
                            gridTemplateColumns: "140px 1fr 260px 160px",
                            gap: 0,
                            padding: 10,
                            borderTop: "1px solid #eee",
                            alignItems: "center"
                        }}
                    >
                        <div>{a.status}</div>
                        <div>
                            <div style={{ fontWeight: 600 }}>
                                {(a.patientFirstName || "") + " " + (a.patientLastName || "")}
                            </div>
                            <div style={{ opacity: 0.8 }}>{a.chiefComplaint || ""}</div>
                        </div>
                        <div>{fmt(a.submittedAt)}</div>
                        <div>
                            <Link to={`/student/reports/${a.id}`}>Open</Link>
                        </div>
                    </div>
                ))}

                {!loading && filtered.length === 0 && <div style={{ padding: 12 }}>No results.</div>}
            </div>
        </div>
    );
}
