import React from "react";
import { Link } from "react-router-dom";

export default function StudentHome() {
    return (
        <div style={{ display: "grid", gap: 10 }}>
            <h3 style={{ margin: 0 }}>Student</h3>
            <p style={{ marginTop: 0 }}>
                Create a new report draft, save it, and submit it when complete.
            </p>

            <div style={{ display: "flex", gap: 10 }}>
                <Link to="/student/new"><button style={{ padding: 10 }}>Create New Report</button></Link>
                <Link to="/student/reports"><button style={{ padding: 10 }}>View My Reports</button></Link>
            </div>
        </div>
    );
}
