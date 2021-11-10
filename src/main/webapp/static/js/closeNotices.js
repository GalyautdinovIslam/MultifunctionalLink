document.addEventListener('DOMContentLoaded', () => {
    const notices = document.getElementById("notices");

    function closeNotice(){
        notices.style.display = "none";
    }

    setTimeout(closeNotice, 10000);
});
