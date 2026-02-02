import React, { useEffect, useState } from "react";
import { api } from "../api/http";

export default function AdminHome() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [msg, setMsg] = useState("");
    const [error, setError] = useState("");

    // search inputs
    const [email, setEmail] = useState("");
    const [lastName, setLastName] = useState("");

    // create form
    const [form, setForm] = useState({
        email: "",
        password: "",
        firstName: "",
        lastName: "",
        role: "STUDENT",
    });

    function setField(k, v) {
        setForm((p) => ({ ...p, [k]: v }));
    }

    async function loadAll() {
        setLoading(true);
        setError("");
        setMsg("");
        try {
            const data = await api("/api/users");
            setUsers(Array.isArray(data) ? data : []);
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    }

    async function search() {
        setLoading(true);
        setError("");
        setMsg("");
        try {
            const params = new URLSearchParams();
            if (email.trim()) params.set("email", email.trim());
            if (lastName.trim()) params.set("lastName", lastName.trim());

            const path = params.toString()
                ? `/api/users/search?${params.toString()}`
                : "/api/users";

            const data = await api(path);
            setUsers(Array.isArray(data) ? data : []);
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    }

    async function createUser() {
        setError("");
        setMsg("");

        if (!form.email.trim()) return setError("Email is required.");
        if (!form.password.trim()) return setError("Password is required.");
        if (!form.firstName.trim()) return setError("First name is required.");
        if (!form.lastName.trim()) return setError("Last name is required.");

        try {
            await api("/api/users", {
                method: "POST",
                body: {
                    email: form.email,
                    password: form.password,
                    firstName: form.firstName,
                    lastName: form.lastName,
                    role: form.role,
                },
            });

            setMsg("User created.");
            setForm({ email: "", password: "", firstName: "", lastName: "", role: "STUDENT" });
            await loadAll();
        } catch (e) {
            setError(e.message);
        }
    }

    async function deleteUser(id) {
        setError("");
        setMsg("");

        const ok = window.confirm("Delete this user?");
        if (!ok) return;

        try {
            await api(`/api/users/${id}`, { method: "DELETE" });
            setMsg("User deleted.");
            await loadAll();
        } catch (e) {
            setError(e.message);
        }
    }

    useEffect(() => {
        loadAll();
    }, []);

    return (
        <div style={{ display: "grid", gap: 12 }}>
            <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
                <h3 style={{ margin: 0, flex: 1 }}>Admin — User Management</h3>
                <button onClick={loadAll} style={{ padding: 8 }}>Refresh</button>
            </div>

            {msg && <div style={{ color: "green" }}>{msg}</div>}
            {error && <div style={{ color: "crimson" }}>{error}</div>}

            <div style={{ padding: 12, border: "1px solid #ddd", borderRadius: 10, display: "grid", gap: 10 }}>
                <div style={{ fontWeight: 700 }}>Search Users</div>
                <div style={{ display: "grid", gridTemplateColumns: "160px 1fr", gap: 10, alignItems: "center" }}>
                    <div>Email</div>
                    <input value={email} onChange={(e) => setEmail(e.target.value)} style={{ padding: 8 }} />

                    <div>Last Name</div>
                    <input value={lastName} onChange={(e) => setLastName(e.target.value)} style={{ padding: 8 }} />
                </div>

                <div style={{ display: "flex", gap: 10 }}>
                    <button onClick={search} style={{ padding: 10, width: 160 }}>Search</button>
                    <button onClick={() => { setEmail(""); setLastName(""); loadAll(); }} style={{ padding: 10, width: 160 }}>
                        Clear
                    </button>
                </div>
            </div>

            <div style={{ padding: 12, border: "1px solid #ddd", borderRadius: 10, display: "grid", gap: 10 }}>
                <div style={{ fontWeight: 700 }}>Create User</div>

                <div style={{ display: "grid", gridTemplateColumns: "160px 1fr", gap: 10, alignItems: "center" }}>
                    <div>Email</div>
                    <input value={form.email} onChange={(e) => setField("email", e.target.value)} style={{ padding: 8 }} />

                    <div>Password</div>
                    <input type="password" value={form.password} onChange={(e) => setField("password", e.target.value)} style={{ padding: 8 }} />

                    <div>First Name</div>
                    <input value={form.firstName} onChange={(e) => setField("firstName", e.target.value)} style={{ padding: 8 }} />

                    <div>Last Name</div>
                    <input value={form.lastName} onChange={(e) => setField("lastName", e.target.value)} style={{ padding: 8 }} />

                    <div>Role</div>
                    <select value={form.role} onChange={(e) => setField("role", e.target.value)} style={{ padding: 8 }}>
                        <option value="STUDENT">STUDENT</option>
                        <option value="INSTRUCTOR">INSTRUCTOR</option>
                        <option value="ADMIN">ADMIN</option>
                    </select>
                </div>

                <button onClick={createUser} style={{ padding: 10, width: 180 }}>Create</button>
            </div>

            <div style={{ border: "1px solid #ddd", borderRadius: 10, overflow: "hidden" }}>
                <div style={{ background: "#f6f6f6", padding: 10, fontWeight: 700 }}>
                    Users ({users.length})
                </div>

                {loading && <div style={{ padding: 12 }}>Loading...</div>}

                {!loading && users.map((u) => (
                    <div
                        key={u.id}
                        style={{
                            display: "grid",
                            gridTemplateColumns: "260px 1fr 140px 120px",
                            gap: 10,
                            padding: 10,
                            borderTop: "1px solid #eee",
                            alignItems: "center"
                        }}
                    >
                        <div style={{ fontFamily: "monospace", fontSize: 12 }}>{u.id}</div>
                        <div>
                            <div style={{ fontWeight: 600 }}>{u.firstName} {u.lastName}</div>
                            <div style={{ opacity: 0.8 }}>{u.email}</div>
                        </div>
                        <div>{u.role}</div>
                        <div>
                            <button onClick={() => deleteUser(u.id)} style={{ padding: "6px 10px" }}>
                                Delete
                            </button>
                        </div>
                    </div>
                ))}

                {!loading && users.length === 0 && (
                    <div style={{ padding: 12 }}>No users found.</div>
                )}
            </div>
        </div>
    );
}
