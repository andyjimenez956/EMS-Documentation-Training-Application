import React from "react";
import { Routes, Route, Navigate, Link, useNavigate } from "react-router-dom";

import Login from "./pages/Login";
import AdminHome from "./pages/AdminHome";
import StudentHome from "./pages/StudentHome";
import StudentNewReport from "./pages/StudentNewReport";
import StudentMyReports from "./pages/StudentMyReports";
import StudentReportDetail from "./pages/StudentReportDetail";
import InstructorHome from "./pages/InstructorHome";
import InstructorReportDetail from "./pages/InstructorReportDetail";

import { clearAuth, isAuthed, getRole } from "./auth/auth";

// ---- Guards ----
function RequireAuth({ children }) {
    if (!isAuthed()) return <Navigate to="/login" replace />;
    return children;
}

function RequireRole({ allow, children }) {
    if (!isAuthed()) return <Navigate to="/login" replace />;
    const role = getRole();

    if (!role || !allow.includes(role)) {
        // fall back to the role's "home"
        if (role === "INSTRUCTOR") return <Navigate to="/instructor" replace />;
        if (role === "ADMIN") return <Navigate to="/admin" replace />;
        return <Navigate to="/student" replace />;
    }

    return children;
}

export default function App() {
    const nav = useNavigate();
    const role = getRole(); // "STUDENT" | "INSTRUCTOR" | "ADMIN" | null

    const homePath =
        role === "INSTRUCTOR" ? "/instructor" : role === "ADMIN" ? "/admin" : "/student";

    return (
        <div style={{ maxWidth: 1000, margin: "0 auto", padding: 16, fontFamily: "system-ui, Arial" }}>
            <header style={{ display: "flex", gap: 12, alignItems: "center", marginBottom: 16 }}>
                <h2 style={{ margin: 0, flex: 1 }}>EMS Documentation Training</h2>

                {/* Only show links relevant to the logged-in role */}
                {role === "STUDENT" && (
                    <>
                        <Link to="/student">Home</Link>
                        <Link to="/student/new">New Report</Link>
                        <Link to="/student/reports">My Reports</Link>
                    </>
                )}

                {role === "INSTRUCTOR" && (
                    <>
                        <Link to="/instructor">Instructor</Link>
                    </>
                )}

                {role === "ADMIN" && (
                    <>
                        <Link to="/admin">Admin</Link>
                    </>
                )}

                <button
                    onClick={() => {
                        clearAuth();
                        nav("/login");
                    }}
                    style={{ padding: "6px 10px" }}
                >
                    Log out
                </button>
            </header>

            <Routes>
                {/* Public */}
                <Route path="/login" element={<Login />} />

                {/* Student */}
                <Route
                    path="/student"
                    element={
                        <RequireRole allow={["STUDENT"]}>
                            <StudentHome />
                        </RequireRole>
                    }
                />
                <Route
                    path="/student/new"
                    element={
                        <RequireRole allow={["STUDENT"]}>
                            <StudentNewReport />
                        </RequireRole>
                    }
                />
                <Route
                    path="/student/reports"
                    element={
                        <RequireRole allow={["STUDENT"]}>
                            <StudentMyReports />
                        </RequireRole>
                    }
                />
                <Route
                    path="/student/reports/:id"
                    element={
                        <RequireRole allow={["STUDENT"]}>
                            <StudentReportDetail />
                        </RequireRole>
                    }
                />

                {/* Instructor */}
                <Route
                    path="/instructor"
                    element={
                        <RequireRole allow={["INSTRUCTOR"]}>
                            <InstructorHome />
                        </RequireRole>
                    }
                />
                <Route
                    path="/instructor/reports/:id"
                    element={
                        <RequireRole allow={["INSTRUCTOR"]}>
                            <InstructorReportDetail />
                        </RequireRole>
                    }
                />

                {/* Admin */}
                <Route
                    path="/admin"
                    element={
                        <RequireRole allow={["ADMIN"]}>
                            <AdminHome />
                        </RequireRole>
                    }
                />

                {/* Default + fallback */}
                <Route
                    path="/"
                    element={
                        <RequireAuth>
                            <Navigate to={homePath} replace />
                        </RequireAuth>
                    }
                />
                <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
        </div>
    );
}
