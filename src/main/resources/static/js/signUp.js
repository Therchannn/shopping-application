// === Toggle hiện/ẩn mật khẩu ===
const showPassword = document.querySelector(".icon-password");

if (showPassword) {
  showPassword.addEventListener("click", function () {
    const input = this.previousElementSibling; // input ngay trước icon
    const isPassword = input.type === "password";

    input.type = isPassword ? "text" : "password";

    this.name = isPassword ? "eye-outline" : "eye-off-outline";
  });
}

// Submit form
document.getElementById("signUpForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const username = document.getElementById("username").value.trim();
  const name = document.getElementById("name").value.trim();
  const password = document.getElementById("password").value.trim();
  const phone = document.getElementById("phone").value.trim();
  const message = document.getElementById("message");

  if (password.length < 6) {
    message.innerText = "Mật khẩu phải ít nhất 6 ký tự!";
    message.style.color = "red";
    return document.querySelector(".signUpForm").reset();

  }

  if (!/^[0-9]{10,11}$/.test(phone)) {
    message.innerText = "Số điện thoại không hợp lệ (10-11 chữ số)!";
    message.style.color = "red";
    document.querySelector(".signUpForm").reset();

    return;
  }


  const data = { username, name, password, phone };

  // FETCH API
  try {
    const res = await fetch("/api/user/signUp", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    const result = await res.text();

    // --- Xử lý phản hồi từ BE ---
    if (res.ok) {
      message.innerText = result;
      message.style.color = result.includes("successfully")
        ? "green"
        : "orange";
      if (result.includes("successfully")) {
        document.getElementById("signUpForm").reset();
      }
    } else {
      message.innerText = "Lỗi: " + result;
      message.style.color = "red";
    }
  } catch (error) {
    message.innerText = "Lỗi kết nối đến server!";
    message.style.color = "red";
  }
});
