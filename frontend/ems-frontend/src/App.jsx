import React from "react";
import { Routes, Route, Navigate, Link, useNavigate } from "react-router-dom";
import Login from "./pages/Login";
import StudentHome from "./pages/StudentHome";
import StudentNewReport from "./pages/StudentNewReport";
import StudentMyReports from "./pages/StudentMyReports";
import StudentReportDetail from "./pages/StudentReportDetail";
import InstructorHome from "./pages/InstructorHome";
import InstructorReportDetail from "./pages/InstructorReportDetail";
import { clearAuth, isAuthed } from "./auth/auth";
import Reports from "./pages/Reports";

function Private({ children }) {
    if (!isAuthed()) return <Navigate to="/login" replace />;
    return children;
}

export default function App() {
    const nav = useNavigate();

    return (
        <div style={{ maxWidth: 1000, margin: "0 auto", padding: 16, fontFamily: "system-ui, Arial" }}>
            <header style={{ display: "flex", gap: 12, alignItems: "center", marginBottom: 16 }}>
                <h2 style={{ margin: 0, flex: 1 }}>EMS Documentation Training</h2>
                <Link to="/student">Student</Link>
                <Link to="/instructor">Instructor</Link>
                <button
                    onClick={() => { clearAuth(); nav("/login"); }}
                    style={{ padding: "6px 10px" }}
                >
                    Log out
                </button>
            </header>

            <Routes>
                <Route path="/login" element={<Login />} />

                <Route path="/student" element={<Private><StudentHome /></Private>} />
                <Route path="/student/new" element={<Private><StudentNewReport /></Private>} />
                <Route path="/student/reports" element={<Private><StudentMyReports /></Private>} />
                <Route path="/student/reports/:id" element={<Private><StudentReportDetail /></Private>} />

                <Route path="/instructor" element={<Private><InstructorHome /></Private>} />
                <Route path="/instructor/reports/:id" element={<Private><InstructorReportDetail /></Private>} />

                <Route path="/reports" element={<Private><Reports /></Private>} />

                <Route path="/" element={<Navigate to="/login" replace />} />
            </Routes>
        </div>
    );
}
