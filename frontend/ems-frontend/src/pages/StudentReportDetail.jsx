import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { api } from "../api/http";

export default function StudentReportDetail() {
    const { id } = useParams();
    const nav = useNavigate();
    const [a, setA] = useState(null);
    const [saving, setSaving] = useState(false);
    const [msg, setMsg] = useState("");
    const [error, setError] = useState("");

    const [form, setForm] = useState({
        patientFirstName: "",
        patientLastName: "",
        patientAge: "",
        patientSex: "",
        chiefComplaint: "",
        systolicBp: "",
        diastolicBp: "",
        heartRate: "",
        respiratoryRate: "",
        spo2: "",
        assessmentNotes: "",
        interventions: "",
        disposition: "",
        narrativeText: ""
    });

    useEffect(() => {
        (async () => {
            try {
                const data = await api(`/api/attempts/${id}`);
                setA(data);
                setForm({
                    patientFirstName: data.patientFirstName || "",
                    patientLastName: data.patientLastName || "",
                    patientAge: data.patientAge ?? "",
                    patientSex: data.patientSex || "",
                    chiefComplaint: data.chiefComplaint || "",
                    systolicBp: data.systolicBp ?? "",
                    diastolicBp: data.diastolicBp ?? "",
                    heartRate: data.heartRate ?? "",
                    respiratoryRate: data.respiratoryRate ?? "",
                    spo2: data.spo2 ?? "",
                    assessmentNotes: data.assessmentNotes || "",
                    interventions: data.interventions || "",
                    disposition: data.disposition || "",
                    narrativeText: data.narrativeText || ""
                });
            } catch (e) {
                setError(e.message);
            }
        })();
    }, [id]);

    function setField(k, v) {
        setForm((p) => ({ ...p, [k]: v }));
    }

    async function refresh() {
        const data = await api(`/api/attempts/${id}`);
        setA(data);
    }

    async function saveDraft() {
        setSaving(true);
        setError("");
        setMsg("");
        try {
            const payload = {
                patientFirstName: form.patientFirstName,
                patientLastName: form.patientLastName,
                patientAge: form.patientAge === "" ? null : Number(form.patientAge),
                patientSex: form.patientSex,
                chiefComplaint: form.chiefComplaint,
                systolicBp: form.systolicBp === "" ? null : Number(form.systolicBp),
                diastolicBp: form.diastolicBp === "" ? null : Number(form.diastolicBp),
                heartRate: form.heartRate === "" ? null : Number(form.heartRate),
                respiratoryRate: form.respiratoryRate === "" ? null : Number(form.respiratoryRate),
                spo2: form.spo2 === "" ? null : Number(form.spo2),
                assessmentNotes: form.assessmentNotes,
                interventions: form.interventions,
                disposition: form.disposition,
                narrativeText: form.narrativeText
            };
            await api(`/api/attempts/${id}`, { method: "PUT", body: payload });
            await refresh();
            setMsg("Draft saved.");
        } catch (e) {
            setError(e.message);
        } finally {
            setSaving(false);
        }
    }

    async function submit() {
        setSaving(true);
        setError("");
        setMsg("");
        try {
            await api(`/api/attempts/${id}/submit`, { method: "POST" });
            await refresh();
            setMsg("Report submitted.");
        } catch (e) {
            setError(e.message);
        } finally {
            setSaving(false);
        }
    }

    async function deleteDraft() {
        if (!confirm("Delete this draft?")) return;
        setSaving(true);
        setError("");
        setMsg("");
        try {
            await api(`/api/attempts/${id}`, { method: "DELETE" });
            nav("/student/reports");
        } catch (e) {
            setError(e.message);
        } finally {
            setSaving(false);
        }
    }

    if (error && !a) {
        return <div style={{ color: "crimson" }}>{error}</div>;
    }

    if (!a) return <div>Loading...</div>;

    const locked = a.status === "SUBMITTED";

    return (
        <div style={{ display: "grid", gap: 12 }}>
            <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
                <h3 style={{ margin: 0, flex: 1 }}>Report</h3>
                <button onClick={() => nav("/student/reports")} style={{ padding: 8 }}>Back</button>
            </div>

            <div style={{ display: "flex", gap: 12, flexWrap: "wrap" }}>
                <div><b>Status:</b> {a.status}</div>
                <div><b>Submitted:</b> {a.submittedAt || ""}</div>
                <div><b>Score:</b> {a.score ?? ""}</div>
            </div>

            {msg && <div style={{ color: "green" }}>{msg}</div>}
            {error && <div style={{ color: "crimson" }}>{error}</div>}

            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10 }}>
                <label>First Name<input disabled={locked} value={form.patientFirstName} onChange={(e) => setField("patientFirstName", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
                <label>Last Name<input disabled={locked} value={form.patientLastName} onChange={(e) => setField("patientLastName", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
                <label>Age<input disabled={locked} value={form.patientAge} onChange={(e) => setField("patientAge", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
                <label>Sex<input disabled={locked} value={form.patientSex} onChange={(e) => setField("patientSex", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
            </div>

            <label>Chief Complaint<input disabled={locked} value={form.chiefComplaint} onChange={(e) => setField("chiefComplaint", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>

            <div style={{ display: "grid", gridTemplateColumns: "repeat(5, 1fr)", gap: 10 }}>
                <label>Sys BP<input disabled={locked} value={form.systolicBp} onChange={(e) => setField("systolicBp", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
                <label>Dia BP<input disabled={locked} value={form.diastolicBp} onChange={(e) => setField("diastolicBp", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
                <label>HR<input disabled={locked} value={form.heartRate} onChange={(e) => setField("heartRate", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
                <label>RR<input disabled={locked} value={form.respiratoryRate} onChange={(e) => setField("respiratoryRate", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
                <label>SpO2<input disabled={locked} value={form.spo2} onChange={(e) => setField("spo2", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
            </div>

            <label>Assessment Notes<textarea disabled={locked} value={form.assessmentNotes} onChange={(e) => setField("assessmentNotes", e.target.value)} style={{ width: "100%", padding: 8, minHeight: 80 }} /></label>
            <label>Interventions<textarea disabled={locked} value={form.interventions} onChange={(e) => setField("interventions", e.target.value)} style={{ width: "100%", padding: 8, minHeight: 80 }} /></label>
            <label>Disposition<input disabled={locked} value={form.disposition} onChange={(e) => setField("disposition", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>

            <label>Narrative<textarea disabled={locked} value={form.narrativeText} onChange={(e) => setField("narrativeText", e.target.value)} style={{ width: "100%", padding: 8, minHeight: 120 }} /></label>

            <div style={{ display: "flex", gap: 10, flexWrap: "wrap" }}>
                {!locked && <button disabled={saving} onClick={saveDraft} style={{ padding: 10, width: 160 }}>Save Draft</button>}
                {!locked && <button disabled={saving} onClick={submit} style={{ padding: 10, width: 160 }}>Submit</button>}
                {!locked && <button disabled={saving} onClick={deleteDraft} style={{ padding: 10, width: 160 }}>Delete Draft</button>}
            </div>

            {a.feedback && (
                <div style={{ padding: 12, border: "1px solid #ddd", borderRadius: 10 }}>
                    <div style={{ marginBottom: 6 }}><b>Instructor Feedback</b></div>
                    <div>{a.feedback}</div>
                </div>
            )}
        </div>
    );
}
