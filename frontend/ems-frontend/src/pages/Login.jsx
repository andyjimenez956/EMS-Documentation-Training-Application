import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { setAuth } from "../auth/auth";
import { api } from "../api/http";

export default function Login() {
    const nav = useNavigate();
    const [username, setUsername] = useState("user");
    const [password, setPassword] = useState("Password123!");
    const [error, setError] = useState("");

    async function onLogin(e) {
        e.preventDefault();
        setError("");
        setAuth(username, password);
        try {
            await api("/api/scenarios");
            nav("/student");
        } catch (err) {
            setError(err.message || "Login failed");
        }
    }

    return (
        <div style={{ maxWidth: 420, margin: "60px auto", padding: 16, border: "1px solid #ddd", borderRadius: 10 }}>
            <h3 style={{ marginTop: 0 }}>Log in</h3>
            <p style={{ marginTop: 0 }}>Enter your credentials to access the EMS training system.</p>

            <form onSubmit={onLogin} style={{ display: "grid", gap: 10 }}>
                <label>
                    Username
                    <input value={username} onChange={(e) => setUsername(e.target.value)} style={{ width: "100%", padding: 8 }} />
                </label>

                <label>
                    Password
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} style={{ width: "100%", padding: 8 }} />
                </label>

                {error && <div style={{ color: "crimson" }}>{error}</div>}

                <button type="submit" style={{ padding: 10 }}>Log in</button>
            </form>
        </div>
    );
}
