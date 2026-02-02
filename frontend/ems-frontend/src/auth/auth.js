const KEY = "ems_basic_auth";

export function setAuth(username, password) {
    const token = btoa(`${username}:${password}`);
    localStorage.setItem(KEY, token);
}

export function clearAuth() {
    localStorage.removeItem(KEY);
}

export function getAuth() {
    return localStorage.getItem(KEY);
}

export function isAuthed() {
    return !!getAuth();
}
