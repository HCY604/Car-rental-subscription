document.addEventListener("DOMContentLoaded", async () => {

    console.log(" 訂單查詢頁載入");

    // 1 取得 token & member
    const token = localStorage.getItem("token");
    const member = JSON.parse(localStorage.getItem("member"));

    console.log("目前登入會員 =", member);

    // 2 未登入 → 返回登入頁（subscribe.html）
    if (!token || !member) {
        alert("請先登入會員才能查詢訂單！");
        window.location.href = "subscribe.html";
        return;
    }

    // 3 顯示當前登入會員名稱
    const nameBadge = document.createElement("div");
    nameBadge.style = "padding:10px;font-weight:600;font-size:18px;";
    nameBadge.textContent = `👤 目前登入：${member.name}`;
    document.querySelector("main").prepend(nameBadge);

    // 4 開始抓取訂單
    const tbody = document.querySelector("#orderTable tbody");

    try {
        const response = await fetch(
            `http://localhost:8080/api/orders/member/${member.id}/status`,
            {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + token,
                    "Content-Type": "application/json"
                }
            }
        );

        if (!response.ok) {
            if (response.status === 401) {
                alert("登入逾時，請重新登入");
                window.location.href = "subscribe.html";
                return;
            }
            throw new Error("後端回傳錯誤：" + response.status);
        }

        const orders = await response.json();
        console.log("會員訂單資料 =", orders);

        if (orders.length === 0) {
            tbody.innerHTML = `<tr><td colspan="6" style="text-align:center;color:gray;">目前沒有任何訂閱紀錄</td></tr>`;
            return;
        }

        // 計算結束日期（依訂閱月數）
        function getEndDate(startDateStr, months) {
            if (!startDateStr) return "—";

            const startDate = new Date(startDateStr);
            if (isNaN(startDate)) return "—";

            const endDate = new Date(startDate.getTime() + months * 30 * 24 * 60 * 60 * 1000);
            return endDate.toISOString().split("T")[0];
        }

        // 5 渲染訂單內容
        orders.forEach(order => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${order.orderNo}</td>
                <td>${order.carId}</td>
                <td>${order.startDate}</td>
                <td>${getEndDate(order.startDate, order.months)}</td>
                <td>${order.finalPrice?.toLocaleString() ?? ""} 元</td>
                <td class="${order.status === '進行中' ? 'status-in-progress' : 'status-completed'}">
                    ${order.status}
                </td>
            `;
            tbody.appendChild(tr);
        });

    } catch (err) {
        console.error("查詢錯誤：", err);
        alert("查詢訂單發生錯誤：" + err.message);
    }
});


// 下拉式選單控制
document.addEventListener("DOMContentLoaded", () => {
    const button = document.querySelector(".dropbtn");
    const menu = document.querySelector(".dropdown-content");

    button.addEventListener("click", (e) => {
        e.stopPropagation();
        menu.style.display = menu.style.display === "block" ? "none" : "block";
    });

    document.addEventListener("click", () => {
        menu.style.display = "none";
    });
});


// ============================
// 正式登出功能
// ============================
document.getElementById("logoutBtn").addEventListener("click", () => {
    if (confirm("確定要登出嗎？")) {
        localStorage.removeItem("token");
        localStorage.removeItem("member");

        alert("已登出");
        window.location.href = "subscribe.html";
    }
});
