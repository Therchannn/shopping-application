const btn = document.querySelector('.logout__btn');
const logout = document.querySelector('.logout')
const logoutBtn = document.querySelector('.logout__icon');
const logoutBack = document.querySelector('.btn__back');
        
btn.addEventListener("click", () => {
    logout.classList.add("show")
})

logoutBtn.addEventListener("click", () => {
    logout.classList.remove("show")
})

logoutBack.addEventListener("click", () => {
    logout.classList.remove("show")
})
