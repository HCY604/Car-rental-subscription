function goRegister() {
    window.location.href = "OOO.html"; // 之後再改成真實頁面
}

function goSubscribe() {
    window.location.href = "subscribe.html";
}

//下拉式選單
document.addEventListener("DOMContentLoaded", () => {
    const button = document.querySelector(".dropbtn");
    const menu = document.querySelector(".dropdown-content");

    // 點擊開啟/關閉
    button.addEventListener("click", (e) => {
        e.stopPropagation();
        menu.style.display = menu.style.display === "block" ? "none" : "block";
    });

    // 點擊其他地方關閉
    document.addEventListener("click", () => {
        menu.style.display = "none";
    });
});
