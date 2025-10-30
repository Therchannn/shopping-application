let render = (input, amount) => {
  input.value = amount;
};

// Tăng
let handlePlus = (btn) => {
  const solid = btn.closest(".cart_solid");
  const amountElement = solid.querySelector(".amount");
  const priceElement = solid.querySelector(".cart_price");

  let amount = parseInt(amountElement.value) || 1;
  amount++;
// Thay đổi giá khi tăng số lượng
  let basePrice = parseInt(priceElement.getAttribute("data-base-price"));
  priceElement.textContent = (basePrice * amount).toLocaleString("vi-VN") + " VND";

  render(amountElement, amount);
};

// Giảm
let handleMinus = (btn) => {
  const solid = btn.closest(".cart_solid");
  const amountElement = solid.querySelector(".amount");
  const priceElement = solid.querySelector(".cart_price");

  let amount = parseInt(amountElement.value) || 1;
  amount--;
  if (amount < 1) amount = 1;
// Thay đổi giá khi giảm số lượng

  let basePrice = parseInt(priceElement.getAttribute("data-base-price"));
  priceElement.textContent = (basePrice * amount).toLocaleString("vi-VN") + " VND";

  render(amountElement, amount);
};

// Xóa
document.querySelector(".cart_list").addEventListener("click", function (e) {
  if (e.target.closest(".cart_del")) {
    e.target.closest(".cart_solid").remove();
  }
});
