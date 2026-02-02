import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { api } from "../api/http";
import { setAuth, setMe, isAuthed, getRole, clearAuth } from "../auth/auth";

export default function Login() {
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    // ✅ Auto-route ONLY if already authed
    useEffect(() => {
        if (!isAuthed()) return;

        const role = getRole();
        if (role === "ADMIN") navigate("/admin", { replace: true });
        else if (role === "INSTRUCTOR") navigate("/instructor", { replace: true });
        else if (role === "STUDENT") navigate("/student", { replace: true });
    }, [navigate]);

    async function onSubmit(e) {
        e.preventDefault();
        setError("");

        try {
            const token = btoa(`${email}:${password}`);
            setAuth(token);

            const me = await api("/api/auth/me");
            setMe(me);

            if (me.role === "ADMIN") navigate("/admin");
            else if (me.role === "INSTRUCTOR") navigate("/instructor");
            else navigate("/student");
        } catch (err) {
            // if auth fails, clear bad token so you don't get stuck "logged in"
            clearAuth();
            setError(err.message || "Login failed");
        }
    }

    return (
        <div>
            <h3>Login</h3>
            <form onSubmit={onSubmit}>
                <input value={email} onChange={(e)=>setEmail(e.target.value)} placeholder="email" />
                <input type="password" value={password} onChange={(e)=>setPassword(e.target.value)} placeholder="password" />
                <button type="submit">Login</button>
            </form>
            {error && <div style={{color:"crimson"}}>{error}</div>}
        </div>
    );
}
