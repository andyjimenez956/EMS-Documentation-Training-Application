import { getAuth } from "../auth/auth";

const BASE = "http://localhost:8080";

export async function api(path, { method = "GET", body } = {}) {
    if (body !== undefined && method === "GET") {
        throw new Error(`api() called with body but no method for ${path}`);
    }

    const token = getAuth();

    const headers = { Accept: "application/json" };
    if (token) headers.Authorization = `Basic ${token}`;
    if (body !== undefined) headers["Content-Type"] = "application/json";

    const res = await fetch(`${BASE}${path}`, {
        method,
        headers,
        body: body === undefined ? undefined : JSON.stringify(body),
    });

    const text = await res.text();
    let data = null;
    try {
        data = text ? JSON.parse(text) : null;
    } catch {
        data = text;
    }

    if (!res.ok) {
        const msg =
            (data && data.message) ||
            (data && data.error) ||
            (typeof data === "string" ? data : "Request failed");
        throw new Error(msg);
    }

    return data;
}
