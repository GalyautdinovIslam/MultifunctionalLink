document.addEventListener('DOMContentLoaded', () => {
    const span = document.getElementById("span");
    const cutLink = document.getElementById("cutLink");
    const cutLinkHref = document.getElementById("cutLinkHref");
    i = 9;

    function showTime(){
        if (i >= 0) {
            span.innerText = i;
            i -= 1;
        } else {
            let regex = /\/\//;
            if(regex.test(cutLink.innerText)){
                window.location.replace(cutLink.innerText);
            } else {
                window.location.replace(cutLinkHref.getAttribute("href"));
            }
        }
    }
    setInterval(showTime, 1000);
});
