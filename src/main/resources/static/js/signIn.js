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
