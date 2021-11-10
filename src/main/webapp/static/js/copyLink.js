document.addEventListener('DOMContentLoaded', () => {
    const copyButton = document.getElementById("copyButton");
    const link = document.getElementById("link")
    copyButton.onclick = () => {
        navigator.clipboard.writeText(link.value);
    }
});
