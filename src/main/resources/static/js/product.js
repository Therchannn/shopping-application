//Product
const form_add = document.getElementById("modal_add");
const form_edit = document.getElementById("modal_edit")
const modal_delete = document.getElementById("modal_delete");

document.getElementById("add_product").addEventListener("click", () => {
    form_add.classList.add("show")
})

document.getElementById("close_form_add_btn").addEventListener('click', () => {
    form_add.classList.remove("show")
})

document.getElementById("close_edit_form_btn").addEventListener("click", () => {
    form_edit.classList.remove("show")
})


document.querySelectorAll('.list_btn').forEach(btn => {
    btn.addEventListener('click', function () {
        const parent = btn.closest('.product');

        const variantContainer = parent.querySelector('.product_variant_container');

        variantContainer.classList.toggle('active');
    });
});

document.querySelectorAll('.import_btn').forEach((btn) => {
    btn.addEventListener('change', (e) => {
        const file = e.target.files[0];
        const image = btn.closest('.input_heading').querySelector('.newImage');

        if (file) {
            image.src = URL.createObjectURL(file);
            image.dataset.filename = file.name;
        }
    });
});

//Modify
document.querySelectorAll(".edit_btn").forEach(btn => {
    btn.addEventListener("click", () => {
        const product = btn.closest(".product"); // lấy phần tử .product gần nhất

        const id = product.dataset.id;
        const name = product.dataset.name;
        const desc = product.dataset.description;
        const category = product.dataset.category;
        const status = product.dataset.status;

        document.getElementById("edit_id").value = id;
        document.getElementById("edit_name").value = name;
        document.getElementById("edit_description").value = desc;

        document.getElementById("edit_category_ao").checked = category === "Áo";
        document.getElementById("edit_category_quan").checked = category === "Quần";

        document.getElementById("edit_status").value = status;

        form_edit.classList.add("show")
    });
});

//Add variant
document.querySelectorAll('.form_variant').forEach(form => {
    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const variant =form.closest('.product_variant_container');
        const colors = Array.from(variant.querySelectorAll('.product_variant')).map(color => color.dataset.color)
        const color = form.querySelector('#color').value.trim();

        for(let c of colors){
            if(c.toLowerCase() === color.toLowerCase()){
                showError("Màu sắc đang bị trùng, vui lòng nhập màu khác")
                return;
            }
        }

        const product = form.closest('.product');
        const id = product.querySelector('.product_content').dataset.productId;

        const imageElement = form.querySelector('.newImage');
        const imageUrl = imageElement?.dataset.filename || '';

        const sizes = Array.from(form.querySelectorAll('.size_input span')).map(span => span.textContent.trim());
        const quantities = Array.from(form.querySelectorAll('.quantity_input input')).map(input => input.value);
        const prices = Array.from(form.querySelectorAll('.price_input input')).map(input => input.value);

        const variants = sizes.map((size, i) => ({
            id_product: id,
            id_product_variant: id + Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15),
            code_product_variant: `${id}-${color}-${size}`,
            color,
            size,
            quantity: parseInt(quantities[i] || 0, 10),
            price: parseFloat(prices[i] || 0),
            imageUrl
        }));

        let hiddenInput = form.querySelector('input[name="variantsJson"]');
        if (!hiddenInput) {
            hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = 'variantsJson';
            form.appendChild(hiddenInput);
        }
        hiddenInput.value = JSON.stringify(variants);

        form.submit();
    });
});

