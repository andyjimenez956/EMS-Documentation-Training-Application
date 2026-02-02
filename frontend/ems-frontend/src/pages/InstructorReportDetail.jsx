import React, { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { api } from "../api/http";

const INSTRUCTOR_ID = "ba4ee098-fd48-4a1a-a287-c6f35bd5def5";

function fmt(v) {
    if (!v) return "";
    try {
        return new Date(v).toLocaleString();
    } catch {
        return String(v);
    }
}

export default function InstructorReportDetail() {
    const { id } = useParams();
    const nav = useNavigate();

    const [a, setA] = useState(null);
    const [score, setScore] = useState("");
    const [feedback, setFeedback] = useState("");

    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [msg, setMsg] = useState("");
    const [error, setError] = useState("");

    const canReview = useMemo(() => a && a.status === "SUBMITTED", [a]);

    async function load() {
        setLoading(true);
        setError("");
        setMsg("");
        try {
            const data = await api(`/api/attempts/${id}`);
            setA(data);
            setScore(data.score ?? "");
            setFeedback(data.feedback ?? "");
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        load();
    }, [id]);

    async function saveReview() {
        setSaving(true);
        setError("");
        setMsg("");
        try {
            const payload = {
                reviewedBy: INSTRUCTOR_ID,
                score: score === "" ? null : Number(score),
                feedback: feedback
            };

            await api(`/api/attempts/${id}/review`, { method: "POST", body: payload });
            setMsg("Review saved.");
            await load();
        } catch (e) {
            setError(e.message);
        } finally {
            setSaving(false);
        }
    }

    if (loading) return <div>Loading...</div>;
    if (error && !a) return <div style={{ color: "crimson" }}>{error}</div>;
    if (!a) return <div>Not found.</div>;

    return (
        <div style={{ display: "grid", gap: 12 }}>
            <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
                <h3 style={{ margin: 0, flex: 1 }}>Instructor Review</h3>
                <button onClick={() => nav("/instructor")} style={{ padding: 8 }}>
                    Back
                </button>
                <button onClick={load} style={{ padding: 8 }}>
                    Refresh
                </button>
            </div>

            <div style={{ display: "grid", gap: 6, padding: 12, border: "1px solid #ddd", borderRadius: 10 }}>
                <div><b>Attempt ID:</b> {a.id}</div>
                <div><b>Status:</b> {a.status}</div>
                <div><b>Submitted At:</b> {fmt(a.submittedAt)}</div>
                <div><b>Reviewed By:</b> {a.reviewedBy || ""}</div>
                <div><b>Reviewed At:</b> {fmt(a.reviewedAt)}</div>
                <div><b>Student ID:</b> {a.studentId}</div>
                <div><b>Scenario ID:</b> {a.scenarioId}</div>
            </div>

            <div style={{ display: "grid", gap: 10, padding: 12, border: "1px solid #ddd", borderRadius: 10 }}>
                <div style={{ fontWeight: 700 }}>Patient / Call Summary</div>
                <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10 }}>
                    <div><b>Name:</b> {(a.patientFirstName || "") + " " + (a.patientLastName || "")}</div>
                    <div><b>Age/Sex:</b> {(a.patientAge ?? "") + " / " + (a.patientSex || "")}</div>
                    <div style={{ gridColumn: "1 / -1" }}><b>Chief Complaint:</b> {a.chiefComplaint || ""}</div>
                </div>
            </div>

            <div style={{ display: "grid", gap: 10, padding: 12, border: "1px solid #ddd", borderRadius: 10 }}>
                <div style={{ fontWeight: 700 }}>Vitals</div>
                <div style={{ display: "grid", gridTemplateColumns: "repeat(5, 1fr)", gap: 10 }}>
                    <div><b>BP:</b> {(a.systolicBp ?? "") + "/" + (a.diastolicBp ?? "")}</div>
                    <div><b>HR:</b> {a.heartRate ?? ""}</div>
                    <div><b>RR:</b> {a.respiratoryRate ?? ""}</div>
                    <div><b>SpO2:</b> {a.spo2 ?? ""}</div>
                </div>
            </div>

            <div style={{ display: "grid", gap: 10, padding: 12, border: "1px solid #ddd", borderRadius: 10 }}>
                <div style={{ fontWeight: 700 }}>Assessment / Interventions / Disposition</div>
                <div><b>Assessment Notes:</b> {a.assessmentNotes || ""}</div>
                <div><b>Interventions:</b> {a.interventions || ""}</div>
                <div><b>Disposition:</b> {a.disposition || ""}</div>
            </div>

            <div style={{ display: "grid", gap: 10, padding: 12, border: "1px solid #ddd", borderRadius: 10 }}>
                <div style={{ fontWeight: 700 }}>Narrative</div>
                <div>{a.narrativeText || ""}</div>
            </div>

            {msg && <div style={{ color: "green" }}>{msg}</div>}
            {error && <div style={{ color: "crimson" }}>{error}</div>}

            <div style={{ display: "grid", gap: 10, padding: 12, border: "1px solid #ddd", borderRadius: 10 }}>
                <div style={{ fontWeight: 700 }}>Instructor Review</div>

                <div style={{ display: "grid", gridTemplateColumns: "160px 1fr", gap: 10, alignItems: "center" }}>
                    <div>Score (0–100)</div>
                    <input
                        value={score}
                        onChange={(e) => setScore(e.target.value)}
                        inputMode="numeric"
                        style={{ width: "100%", padding: 8 }}
                        disabled={!canReview || saving}
                    />

                    <div>Feedback</div>
                    <textarea
                        value={feedback}
                        onChange={(e) => setFeedback(e.target.value)}
                        style={{ width: "100%", padding: 8, minHeight: 140 }}
                        disabled={!canReview || saving}
                    />
                </div>

                {!canReview && (
                    <div style={{ padding: 10, borderRadius: 10, border: "1px solid #eee" }}>
                        This report is not SUBMITTED yet, so it cannot be reviewed.
                    </div>
                )}

                <button
                    onClick={saveReview}
                    disabled={!canReview || saving}
                    style={{ padding: 10, width: 180 }}
                >
                    {saving ? "Saving..." : "Save Review"}
                </button>
            </div>
        </div>
    );
}
