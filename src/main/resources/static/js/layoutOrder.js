function handleStatusClick(el) {
  document.querySelectorAll(".order__status").forEach(e => e.classList.remove("active"));
  el.classList.add("active");
}
// Modal JS
function openModal() {
  const modal = document.getElementById("modal");
  modal.style.display = "flex";
}

function closeModal() {
  const modal = document.getElementById("modal");
  modal.style.display = "none";
}
