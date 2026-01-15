// Application Logic
let currentUser = null
let allUsers = []
let allDepartments = []

// Declare the api variable
const api = window.api // Assuming ApiService instance is created as 'api' in api.js

// ========== PAGE NAVIGATION ==========
function showPage(pageId) {
    const pages = document.querySelectorAll("#authPages .page")
    pages.forEach((page) => page.classList.remove("active"))
    document.getElementById(pageId).classList.add("active")
}

function showDashboardPage(pageId) {
    const pages = document.querySelectorAll(".page-content")
    const navItems = document.querySelectorAll(".nav-item")

    pages.forEach((page) => page.classList.remove("active"))
    navItems.forEach((item) => item.classList.remove("active"))

    document.getElementById(pageId).classList.add("active")

    // Update nav item active state
    if (event && event.target) {
        const navItem = event.target.closest(".nav-item")
        if (navItem) navItem.classList.add("active")
    }

    // Update page title
    const titleMap = {
        dashboardHome: "Dashboard",
        usersPage: "User Management",
        departmentsPage: "Department Management",
        profilePage: "Settings",
    }
    document.getElementById("pageTitle").textContent = titleMap[pageId] || "Dashboard"
}

// ========== AUTHENTICATION HANDLERS ==========

// ========== PROFILE HANDLERS ==========
document.getElementById("profileForm")?.addEventListener("submit", async (e) => {
    e.preventDefault()
    const formData = {
        name: document.getElementById("profileFirstName").value,
        surname: document.getElementById("profileLastName").value,
    }

    try {
        await api.updateUser(currentUser.id, formData)
        currentUser = { ...currentUser, ...formData }
        showToast("Profile updated successfully!", "success")
    } catch (error) {
        console.log("[v0] Profile update error:", error)
        showToast(error.message || "Failed to update profile", "error")
    }
})

document.getElementById("changePasswordForm")?.addEventListener("submit", async (e) => {
    e.preventDefault()
    const oldPassword = document.getElementById("currentPassword").value
    const newPassword = document.getElementById("newPassword").value
    const confirmPassword = document.getElementById("confirmPassword").value

    if (newPassword !== confirmPassword) {
        showToast("Passwords do not match", "error")
        return
    }

    try {
        await api.changePassword({
            oldPassword: oldPassword,
            newPassword: newPassword,
        })
        showToast("Password changed successfully!", "success")
        document.getElementById("changePasswordForm").reset()
    } catch (error) {
        console.log("[v0] Change password error:", error)
        showToast(error.message || "Failed to change password", "error")
    }
})

// ========== USER MANAGEMENT ==========
function openUserModal() {
    document.getElementById("userModal").classList.add("active")
    document.getElementById("userForm").reset()
}

function closeUserModal() {
    document.getElementById("userModal").classList.remove("active")
    document.getElementById("userForm").reset()
}

document.getElementById("userForm")?.addEventListener("submit", async (e) => {
    e.preventDefault()
    const fullName = document.getElementById("userName").value
    const nameParts = fullName.split(" ")
    const formData = {
        name: nameParts[0],
        surname: nameParts.slice(1).join(" ") || "User",
        email: document.getElementById("userEmail").value,
        password: "DefaultPassword123", // Users should change this
        departmentId: Number.parseInt(document.getElementById("userDepartment").value) || null,
    }

    try {
        await api.signup(formData)
        showToast("User created successfully!", "success")
        closeUserModal()
        loadUsers()
    } catch (error) {
        console.log("[v0] Create user error:", error)
        showToast(error.message || "Failed to create user", "error")
    }
})

async function loadUsers() {
    try {
        allUsers = await api.getUsers()
        console.log("[v0] Users loaded:", allUsers)
        renderUsersTable()
        updateStats()
    } catch (error) {
        console.error("[v0] Failed to load users:", error)
        showToast("Failed to load users", "error")
    }
}

function renderUsersTable() {
    const tbody = document.querySelector("#usersTable tbody")
    if (!tbody) return

    tbody.innerHTML = ""

    allUsers.forEach((user) => {
        const row = document.createElement("tr")
        const status = user.isActive ? "Active" : "Inactive"
        const statusClass = user.isActive ? "success" : "warning"
        row.innerHTML = `
      <td>${user.name} ${user.surname}</td>
      <td>${user.email}</td>
      <td>${user.role}</td>
      <td><span style="color: var(--${statusClass})">${status}</span></td>
      <td>
        <div class="table-actions">
          <button class="btn-edit" onclick="editUser(${user.id})">Edit</button>
          <button class="btn-delete" onclick="deleteUserAction(${user.id})">Delete</button>
        </div>
      </td>
    `
        tbody.appendChild(row)
    })
}

function editUser(userId) {
    const user = allUsers.find((u) => u.id === userId)
    if (user) {
        document.getElementById("userName").value = `${user.name} ${user.surname}`
        document.getElementById("userEmail").value = user.email
        document.getElementById("userRole").value = user.role
        openUserModal()
    }
}

async function deleteUserAction(userId) {
    if (confirm("Are you sure you want to delete this user?")) {
        try {
            await api.deleteUser(userId)
            showToast("User deleted successfully!", "success")
            loadUsers()
        } catch (error) {
            console.log("[v0] Delete user error:", error)
            showToast(error.message || "Failed to delete user", "error")
        }
    }
}

// ========== DEPARTMENT MANAGEMENT ==========
function openDepartmentModal() {
    document.getElementById("departmentModal").classList.add("active")
    document.getElementById("departmentForm").reset()
}

function closeDepartmentModal() {
    document.getElementById("departmentModal").classList.remove("active")
    document.getElementById("departmentForm").reset()
}

document.getElementById("departmentForm")?.addEventListener("submit", async (e) => {
    e.preventDefault()
    const formData = {
        name: document.getElementById("deptName").value,
        description: document.getElementById("deptDescription").value,
        managerId: null,
    }

    try {
        await api.createDepartment(formData)
        showToast("Department created successfully!", "success")
        closeDepartmentModal()
        loadDepartments()
    } catch (error) {
        console.log("[v0] Create department error:", error)
        showToast(error.message || "Failed to create department", "error")
    }
})

async function loadDepartments() {
    try {
        allDepartments = await api.getDepartments()
        console.log("[v0] Departments loaded:", allDepartments)
        renderDepartmentsTable()
        populateDepartmentSelect()
        updateStats()
    } catch (error) {
        console.error("[v0] Failed to load departments:", error)
        showToast("Failed to load departments", "error")
    }
}

function renderDepartmentsTable() {
    const tbody = document.querySelector("#departmentsTable tbody")
    if (!tbody) return

    tbody.innerHTML = ""

    allDepartments.forEach((dept) => {
        const row = document.createElement("tr")
        const employeeCount = allUsers.filter((u) => u.departmentId === dept.id).length
        row.innerHTML = `
      <td>${dept.name}</td>
      <td>${employeeCount}</td>
      <td>Active</td>
      <td>
        <div class="table-actions">
          <button class="btn-edit" onclick="editDepartment(${dept.id})">Edit</button>
          <button class="btn-delete" onclick="deleteDepartmentAction(${dept.id})">Delete</button>
        </div>
      </td>
    `
        tbody.appendChild(row)
    })
}

function populateDepartmentSelect() {
    const select = document.getElementById("userDepartment")
    if (!select) return

    select.innerHTML = '<option value="">Select Department</option>'
    allDepartments.forEach((dept) => {
        const option = document.createElement("option")
        option.value = dept.id
        option.textContent = dept.name
        select.appendChild(option)
    })
}

function editDepartment(deptId) {
    const dept = allDepartments.find((d) => d.id === deptId)
    if (dept) {
        document.getElementById("deptName").value = dept.name
        document.getElementById("deptDescription").value = dept.description || ""
        openDepartmentModal()
    }
}

async function deleteDepartmentAction(deptId) {
    if (confirm("Are you sure you want to delete this department?")) {
        try {
            await api.deleteDepartment(deptId)
            showToast("Department deleted successfully!", "success")
            loadDepartments()
        } catch (error) {
            console.log("[v0] Delete department error:", error)
            showToast(error.message || "Failed to delete department", "error")
        }
    }
}

// ========== DASHBOARD DISPLAY ==========
function showDashboard() {
    document.getElementById("authPages").style.display = "none"
    document.getElementById("dashboardPages").style.display = "flex"
    if (currentUser) {
        document.getElementById("currentUser").textContent = `${currentUser.name || ""} ${currentUser.surname || ""}`
    }
    loadUsers()
    loadDepartments()
}

function updateStats() {
    document.getElementById("totalUsers").textContent = allUsers.length
    document.getElementById("totalDepartments").textContent = allDepartments.length
    document.getElementById("activeUsers").textContent = allUsers.filter((u) => u.isActive).length
}

// ========== LOGOUT ==========
async function logout() {
    try {
        const refreshToken = localStorage.getItem("refreshToken")
        if (refreshToken) {
            await api.logout(refreshToken)
        }
    } catch (error) {
        console.error("[v0] Logout error:", error)
    } finally {
        currentUser = null
        localStorage.removeItem("accessToken")
        localStorage.removeItem("refreshToken")
        api.removeToken()
        document.getElementById("authPages").style.display = "block"
        document.getElementById("dashboardPages").style.display = "none"
        showPage("loginPage")
        showToast("Logged out successfully!", "success")
    }
}

// ========== TOAST NOTIFICATIONS ==========
function showToast(message, type = "info") {
    const toast = document.getElementById("toast")
    toast.textContent = message
    toast.className = `toast ${type} show`

    setTimeout(() => {
        toast.classList.remove("show")
    }, 3000)
}

// ========== INITIALIZATION ==========
window.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("loginForm")
    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault()
            const email = document.getElementById("loginEmail").value
            const password = document.getElementById("loginPassword").value

            try {
                console.log("[v0] Attempting login with:", email)
                const response = await window.api.login({ email, password })
                console.log("[v0] Login response:", response)

                if (response.accessToken) {
                    localStorage.setItem("accessToken", response.accessToken)
                }
                if (response.token) {
                    localStorage.setItem("refreshToken", response.token)
                }

                const user = await window.api.getCurrentUser()
                currentUser = user

                showDashboard()
                showToast("Login successful!", "success")
            } catch (error) {
                console.error("[v0] Login error:", error.message)
                showToast(error.message || "Login failed", "error")
            }
        })
    }

    const signupForm = document.getElementById("signupForm")
    if (signupForm) {
        signupForm.addEventListener("submit", async (e) => {
            e.preventDefault()
            console.log("[v0] Signup form submitted")
            const formData = {
                name: document.getElementById("signupFirstName").value,
                surname: document.getElementById("signupLastName").value,
                email: document.getElementById("signupEmail").value,
                password: document.getElementById("signupPassword").value,
                departmentId: null,
            }

            console.log("[v0] Signup data:", formData)

            try {
                await window.api.signup(formData)
                console.log("[v0] Signup successful")
                showToast("Signup successful! Please check your email to verify your account.", "success")
                setTimeout(() => showPage("loginPage"), 2000)
            } catch (error) {
                console.error("[v0] Signup error:", error.message)
                showToast(error.message || "Signup failed", "error")
            }
        })
    }

    const forgotPasswordForm = document.getElementById("forgotPasswordForm")
    if (forgotPasswordForm) {
        forgotPasswordForm.addEventListener("submit", async (e) => {
            e.preventDefault()
            console.log("[v0] Forgot password form submitted")
            const email = document.getElementById("forgotEmail").value

            console.log("[v0] Forgot password email:", email)

            try {
                await window.api.forgotPassword(email)
                console.log("[v0] Forgot password successful")
                showToast("Password reset link sent to your email!", "success")
                setTimeout(() => showPage("loginPage"), 2000)
            } catch (error) {
                console.error("[v0] Forgot password error:", error.message)
                showToast(error.message || "Failed to send reset link", "error")
            }
        })
    }

    const accessToken = localStorage.getItem("accessToken")
    if (accessToken) {
        window.api.setToken(accessToken)
        window.api
            .getCurrentUser()
            .then((user) => {
                currentUser = user
                showDashboard()
            })
            .catch(() => {
                localStorage.removeItem("accessToken")
                localStorage.removeItem("refreshToken")
                window.api.removeToken()
                showPage("loginPage")
            })
    } else {
        showPage("loginPage")
    }
})
