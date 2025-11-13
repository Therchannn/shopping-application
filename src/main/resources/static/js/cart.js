document.querySelectorAll(".amount").forEach(input =>
    input.addEventListener("change", async () => {
      const solid = input.closest(".cart_solid");
      const quantity = Number(solid.querySelector(".limit").value);
      const id = solid.querySelector(".item_id").value;
      let amount = Number(input.value);

      if (isNaN(amount) || amount <= 0) {
        amount = 1;
      }

      if (amount > quantity) {
        amount = quantity;
      }

      input.value = amount;

      const body = {
        productVariantId: id,
        quantity: amount
      };

      await handleUpdate(body);
    })
);

let render = (input, amount) => {
  input.value = amount;
};

const debounce = (func, delay) => {
  let timeout;
  return function (...args) {
    clearTimeout(timeout);
    timeout = setTimeout(() => func.apply(this, args), delay);
  };
}

const handleUpdate = async (data) => {
  await fetch("/outerity/api/cart/update", {
    headers: {
      "Content-Type": "application/json"
    },
    method: "post",
    body: JSON.stringify(data)
  })
}

const handlePlus = async (btn) => {
  const solid = btn.closest(".cart_solid");
  const quantity = Number(solid.querySelector(".limit").value);
  const amountElement = solid.querySelector(".amount");
  const id = solid.querySelector(".item_id").value;

  let amount = Number(amountElement.value);

  if (isNaN(amount) || amount <= 0) amount = 1;
  amount += 1;
  if (amount > quantity) amount = quantity;

  render(amountElement, amount);

  const body = {
    productVariantId: id,
    quantity: amount
  };

  await handleUpdate(body);
};

const handleMinus = async (btn) => {
  const solid = btn.closest(".cart_solid");
  const quantity = Number(solid.querySelector(".limit").value);
  const amountElement = solid.querySelector(".amount");
  const id = solid.querySelector(".item_id").value;

  let amount = Number(amountElement.value);

  if (isNaN(amount) || amount <= 0) {
    amount = 1;
  }

  amount -= 1;

  if (amount < 1) {
    amount = 1;
  }

  if (amount > quantity) {
    amount = quantity;
  }

  render(amountElement, amount);

  const body = {
    productVariantId: id,
    quantity: amount,
  };

  await handleUpdate(body);
};


debounce(handleMinus, 500)
debounce(handlePlus, 500)

const handleDelete = async (btn) => {
  const parent = btn.closest(".cart_content")
  const value = parent.querySelector(".item_id").value
  const message = await deleteCart(value)

  const toaster = document.createElement("div");
  toaster.className = 'toaster'
  toaster.innerText = message
  document.body.appendChild(toaster)

  setTimeout(() => {
    toaster.remove()
  }, 1000)
}

async function deleteCart(data){
  const res = await fetch("/outerity/api/cart/delete", {
    headers: {
      "Content-Type": "application/json",
    },
    method: "delete",
    body: JSON.stringify({id: data})
  })

  return await res.text()
}

// Xóa
document.querySelector(".cart_list").addEventListener("click", function (e) {
  if (e.target.closest(".cart_del")) {
    e.target.closest(".cart_solid").remove();
  }
});
