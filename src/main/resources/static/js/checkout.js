function handleStatusClick(el) {
  document.querySelectorAll(".order__status").forEach(e => e.classList.remove("active"));
  el.classList.add("active");
}
// Modal JS
document.getElementById("submit_order").addEventListener("click", async () => {
    const value = document.querySelector('input[name="payment"]:checked').value;
    const res = await fetch("/api/cart/order",{
      headers: {
        "Content-Type": "application/json",
      },
      method: "POST",
        body: JSON.stringify({payment: value})
    })

    if(res){
        openModal()
    }
})

function openModal() {
  const modal = document.getElementById("modal");
  modal.style.display = "flex";
}

function closeModal() {
  const modal = document.getElementById("modal");
  modal.style.display = "none";
}
