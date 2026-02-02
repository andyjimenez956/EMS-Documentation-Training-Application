const KEY_TOKEN = "ems_token";
const KEY_ME = "ems_me";

export function setAuth(token) {
    localStorage.setItem(KEY_TOKEN, token);
}

export function getAuth() {
    return localStorage.getItem(KEY_TOKEN);
}

export function clearAuth() {
    localStorage.removeItem(KEY_TOKEN);
    localStorage.removeItem(KEY_ME);
}

export function isAuthed() {
    return !!getAuth();
}

export function setMe(me) {
    localStorage.setItem(KEY_ME, JSON.stringify(me));
}

export function getMe() {
    const raw = localStorage.getItem(KEY_ME);
    if (!raw) return null;
    try {
        return JSON.parse(raw);
    } catch {
        return null;
    }
}

export function getRole() {
    const me = getMe();
    return me?.role || null;
}

export function getUserId() {
    const me = getMe();
    return me?.id || null;
}
