const cart = document.querySelector(".cart");
const cartBtn = document.querySelector(".another__cart");
// if (!cart || !cartBtn) return;
cartBtn.addEventListener("click", () => {
  cart.classList.toggle("is-hidden");
});

cart.addEventListener("click", (e) => {
  if (e.target.classList.contains("cart")) {
    cart.classList.add("is-hidden");
  }
});
