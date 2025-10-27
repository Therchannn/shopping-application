// === Toggle hiện/ẩn mật khẩu ===
const showPassword = document.querySelector(".icon-password");

if (showPassword) {
  showPassword.addEventListener("click", function () {
    const input = this.previousElementSibling;
    const isPassword = input.type === "password";
    input.type = isPassword ? "text" : "password";
    this.name = isPassword ? "eye-outline" : "eye-off-outline";
  });
}

// === Submit form ===
document.querySelector(".login_form").addEventListener("submit", async (e) => {
  e.preventDefault();

  const username = document.getElementById("username").value.trim();
  const password = document.getElementById("password").value.trim();
  const message = document.getElementById("message");

  const data = { username, password };

  
  try {
    const res = await fetch("/api/user/signIn", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    if (res.ok) {
      const result = await res.json();
      if (result && result.username) {
        message.innerText = "Đăng nhập thành công!";
        message.style.color = "green";
        setTimeout(() => (window.location.href = "/home"), 1000);
      }
    } else if (res.status === 401) {
      message.innerText = "Sai tài khoản hoặc mật khẩu!";
      message.style.color = "red";
      document.querySelector(".login_form").reset();
    } else {
      message.innerText = "Đã xảy ra lỗi máy chủ!";
      message.style.color = "red";
      document.querySelector(".login_form").reset();
    }
  } catch (error) {
    message.innerText = "Không thể kết nối đến server!";
    message.style.color = "red";
    document.querySelector(".login_form").reset();
  }
});
