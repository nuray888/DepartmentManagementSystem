// API Service - Handles communication with backend
class ApiService {
    constructor(baseUrl = "http://localhost:8080") {
        this.baseUrl = baseUrl
        this.token = localStorage.getItem("accessToken")
    }

    // ========== AUTHENTICATION ==========
    async signup(data) {
        return this.post("/api/v1/auth/signUp", data)
    }

    async login(credentials) {
        const response = await this.post("/api/v1/auth/login", credentials)
        if (response.accessToken) {
            this.setToken(response.accessToken)
        }
        return response
    }

    async verifyEmail(token) {
        return this.get(`/api/v1/auth/verify-profile?token=${token}`)
    }

    async forgotPassword(email) {
        return this.post("/api/v1/auth/forgotPassword", { email })
    }

    async resetPassword(data) {
        return this.post("/api/v1/auth/resetPassword", data)
    }

    async changePassword(data) {
        return this.post("/api/v1/auth/changePassword", data)
    }

    async logout(refreshToken) {
        try {
            await this.post(`/api/v1/auth/log-out?refreshToken=${refreshToken}`, {})
        } catch (error) {
            console.log("[v0] Logout API error:", error)
        } finally {
            this.removeToken()
        }
    }

    async refreshToken(refreshTokenValue) {
        const response = await this.post("/api/v1/auth/refreshToken", { token: refreshTokenValue })
        if (response.accessToken) {
            this.setToken(response.accessToken)
        }
        return response
    }

    // ========== USERS ==========
    async getUsers() {
        return this.get("/api/v1/user/all")
    }

    async getUser(id) {
        return this.get(`/api/v1/user/get/${id}`)
    }

    async updateUser(id, data) {
        return this.put(`/api/v1/user/update/${id}`, data)
    }

    async deleteUser(id) {
        return this.delete(`/api/v1/user/delete/${id}`)
    }

    async changeUserRole(userId, role) {
        return this.patch(`/api/v1/user/change-role/${userId}?role=${role}`, {})
    }

    async activateUser(id) {
        return this.patch(`/api/v1/user/activate/${id}`, {})
    }

    async deactivateUser(id) {
        return this.patch(`/api/v1/user/deactivate/${id}`, {})
    }

    async getCurrentUser() {
        return this.get("/api/v1/user/current")
    }

    // ========== DEPARTMENTS ==========
    async getDepartments() {
        return this.get("/v1/departments/all")
    }

    async getDepartment(id) {
        return this.get(`/v1/departments/get/${id}`)
    }

    async createDepartment(data) {
        return this.post("/v1/departments/create", data)
    }

    async updateDepartment(id, data) {
        return this.put(`/v1/departments/update/${id}`, data)
    }

    async deleteDepartment(id) {
        return this.delete(`/v1/departments/delete/${id}`)
    }

    async getDepartmentEmployees(departmentId) {
        return this.get(`/v1/departments/users/${departmentId}`)
    }

    // ========== HTTP METHODS ==========
    async get(endpoint) {
        return this.request(endpoint, {
            method: "GET",
        })
    }

    async post(endpoint, data) {
        return this.request(endpoint, {
            method: "POST",
            body: JSON.stringify(data),
        })
    }

    async put(endpoint, data) {
        return this.request(endpoint, {
            method: "PUT",
            body: JSON.stringify(data),
        })
    }

    async patch(endpoint, data) {
        return this.request(endpoint, {
            method: "PATCH",
            body: JSON.stringify(data),
        })
    }

    async delete(endpoint) {
        return this.request(endpoint, {
            method: "DELETE",
        })
    }

    async request(endpoint, options = {}) {
        const url = `${this.baseUrl}${endpoint}`
        const headers = {
            "Content-Type": "application/json",
            ...options.headers,
        }

        if (this.token) {
            headers["Authorization"] = `Bearer ${this.token}`
        }

        try {
            console.log("[v0] API Request:", { method: options.method, url })
            const response = await fetch(url, {
                ...options,
                headers,
            })

            const data = await response.json()
            console.log("[v0] API Response:", { status: response.status, data })

            if (!response.ok) {
                throw new Error(data.message || `API Error: ${response.status}`)
            }

            return data
        } catch (error) {
            console.error("[v0] API Error:", error)
            throw error
        }
    }

    setToken(token) {
        this.token = token
        localStorage.setItem("accessToken", token)
    }

    removeToken() {
        this.token = null
        localStorage.removeItem("accessToken")
    }

    getToken() {
        return this.token
    }
}

// Initialize API Service
const api = new ApiService()
