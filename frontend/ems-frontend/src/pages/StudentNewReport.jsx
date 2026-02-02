import React, { useEffect, useState } from "react";
import { api } from "../api/http";

const STUDENT_ID = "0993ccb9-6ae6-4bce-80f2-5a1ffb1aabe1";

export default function StudentNewReport() {
    const [scenarios, setScenarios] = useState([]);
    const [scenarioId, setScenarioId] = useState("");
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
        disposition: ""
    });
    const [result, setResult] = useState(null);
    const [error, setError] = useState("");

    useEffect(() => {
        (async () => {
            const list = await api("/api/scenarios");
            setScenarios(list);
            if (list.length) setScenarioId(list[0].id);
        })();
    }, []);

    function setField(k, v) {
        setForm((p) => ({ ...p, [k]: v }));
    }

    async function createDraft() {
        setError("");
        setResult(null);

        if (!scenarioId) return setError("Please select a scenario.");

        const payload = {
            scenarioId,
            studentId: STUDENT_ID,
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
            disposition: form.disposition
        };

        const created = await api("/api/attempts", { method: "POST", body: payload });
        setResult(created);
    }

    return (
        <div style={{ display: "grid", gap: 12 }}>
            <h3 style={{ margin: 0 }}>Create Report Draft</h3>

            <label>
                Scenario
                <select value={scenarioId} onChange={(e) => setScenarioId(e.target.value)} style={{ width: "100%", padding: 8 }}>
                    {scenarios.map(s => <option key={s.id} value={s.id}>{s.title}</option>)}
                </select>
            </label>

            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10 }}>
                <label>First Name<input value={form.patientFirstName} onChange={(e) => setField("patientFirstName", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
                <label>Last Name<input value={form.patientLastName} onChange={(e) => setField("patientLastName", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
                <label>Age<input value={form.patientAge} onChange={(e) => setField("patientAge", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
                <label>Sex<input value={form.patientSex} onChange={(e) => setField("patientSex", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
            </div>

            <label>Chief Complaint<input value={form.chiefComplaint} onChange={(e) => setField("chiefComplaint", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>

            <div style={{ display: "grid", gridTemplateColumns: "repeat(5, 1fr)", gap: 10 }}>
                <label>Sys BP<input value={form.systolicBp} onChange={(e) => setField("systolicBp", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
                <label>Dia BP<input value={form.diastolicBp} onChange={(e) => setField("diastolicBp", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
                <label>HR<input value={form.heartRate} onChange={(e) => setField("heartRate", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
                <label>RR<input value={form.respiratoryRate} onChange={(e) => setField("respiratoryRate", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
                <label>SpO2<input value={form.spo2} onChange={(e) => setField("spo2", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>
            </div>

            <label>Assessment Notes<textarea value={form.assessmentNotes} onChange={(e) => setField("assessmentNotes", e.target.value)} style={{ width: "100%", padding: 8, minHeight: 80 }} /></label>
            <label>Interventions<textarea value={form.interventions} onChange={(e) => setField("interventions", e.target.value)} style={{ width: "100%", padding: 8, minHeight: 80 }} /></label>
            <label>Disposition<input value={form.disposition} onChange={(e) => setField("disposition", e.target.value)} style={{ width: "100%", padding: 8 }} /></label>

            {error && <div style={{ color: "crimson" }}>{error}</div>}

            <button onClick={createDraft} style={{ padding: 10, width: 220 }}>Save Draft</button>

            {result && (
                <div style={{ padding: 12, border: "1px solid #ddd", borderRadius: 10 }}>
                    <div><b>Draft saved.</b></div>
                    <div>Attempt ID: {result.id}</div>
                    <div>Status: {result.status}</div>
                </div>
            )}
        </div>
    );
}
