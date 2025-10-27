
const banners = document.querySelectorAll('.slide__img');
const length = banners.length;
let current = 0;
banners[0].style.display = "block"


setInterval( () => {
    if(current === length - 1){
    current = 0; 
    } else{
    current++;
}

for(let i = 0; i < length; i++){
    if(i === current){
        banners[current].style.display = 'block';
    }
    else{
        banners[i].style.display = 'none';
    }
}
}, 2000)