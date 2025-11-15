document.addEventListener("DOMContentLoaded", () => {
    const toaster = document.querySelector(".toaster")
    setTimeout(() => {
        toaster.remove();
    }, 2000)
})

// Modal JS
const orderId_model = document.getElementById("orderId");
const payment_model = document.getElementById("payment");

const submitBtn = document.getElementById("submit_order");
if(submitBtn){
    submitBtn.addEventListener("click", async () => {
        const orderId = document.getElementById("orderId")?.value;
        const payment = document.querySelector('input[name="payment"]:checked').value;

        let res;

        if(orderId){
            res = await fetch("/outerity/api/order/update", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    status: "Pending",
                    orderId: orderId,
                    payment: payment
                }),
            });
        }
        else{
             res = await fetch("/outerity/api/cart/order",{
              headers: {
                "Content-Type": "application/json",
              },
              method: "POST",
                body: JSON.stringify({
                    payment: payment
                })
            })
        }

        if(res){
            openModal()
        }
    })
}

const modal = document.getElementById("modal");

function openModal(btn) {
  modal.style.display = "flex";

  if(!btn) return;

  const parent = btn.closest('.order__new') ?? null;
  if(parent){
      const orderId = parent.querySelector('.orderId').value;
      const payment = parent.querySelector('.payment').value;
      orderId_model.value = orderId;
      payment_model.value = payment;
  }
}

function closeModal() {
  modal.style.display = "none";
}


