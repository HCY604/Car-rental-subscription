/* ----------------- 假資料（6 台車） ----------------- */
const cars = [
    { id: 1, name: "Mitsubishi Veryca", price: 9999, brand: "Mitsubishi", area:"北區", store:"台北車站", img: "img/car1.jpg" },
    { id: 2, name: "Mitsubishi New Colt Plus", price: 9999, brand: "Mitsubishi", area:"北區", store:"羅東車站", img: "img/car2.jpg" },
    { id: 3, name: "Toyota Yaris", price: 10999, brand: "Toyota", area:"中區", store:"台中車站", img: "img/car3.jpg" },
    { id: 4, name: "Toyota Altis", price: 11999, brand: "Toyota", area:"中區", store:"台中車站", img: "img/car4.jpg" },
    { id: 5, name: "Nissan Kicks", price: 12999, brand: "Nissan", area:"南區", store:"高雄車站", img: "img/car5.jpg" },
    { id: 6, name: "Honda Fit", price: 13999, brand: "Honda", area:"南區", store:"高雄車站", img: "img/car6.jpg" }
];

/* ----------------- 初始化：產生篩選選單 ----------------- */
function initFilters() {

    // 品牌
    const brandSelect = document.getElementById("filterBrand");
    let brands = [...new Set(cars.map(c => c.brand))];
    brands.forEach(b => {
        brandSelect.innerHTML += `<option value="${b}">${b}</option>`;
    });

    // 月費
    const priceSelect = document.getElementById("filterPrice");
    let prices = [...new Set(cars.map(c => c.price))];
    prices.sort((a,b)=>a-b);
    prices.forEach(p => {
        priceSelect.innerHTML += `<option value="${p}">${p}</option>`;
    });

    // 取車區域（固定）
    const areaSelect = document.getElementById("filterArea");
    ["北區","中區","南區"].forEach(a => {
        areaSelect.innerHTML += `<option value="${a}">${a}</option>`;
    });

    // 取車據點（固定）
    const storeSelect = document.getElementById("filterStore");
    ["台北車站","羅東車站","台中車站","高雄車站"].forEach(s => {
        storeSelect.innerHTML += `<option value="${s}">${s}</option>`;
    });
}

initFilters();

/* ----------------- 產生車輛列表 ----------------- */
function loadCars(listData) {
    const list = document.getElementById("carList");
    list.innerHTML = "";

    listData.forEach(c => {
        list.innerHTML += `
            <div class="car-card">
                <img src="${c.img}">
                <h4>${c.name}</h4>
                <p>品牌：${c.brand}</p>
                <p>月費：NT$ ${c.price}</p>
                <button onclick="selectCar(${c.id})">確認選擇</button>
            </div>
        `;
    });
}

loadCars(cars);  // 預設全部顯示

/* ----------------- 搜尋功能（依照條件） ----------------- */
document.getElementById("searchBtn").addEventListener("click", function(){

    const brand = document.getElementById("filterBrand").value;
    const price = document.getElementById("filterPrice").value;
    const area  = document.getElementById("filterArea").value;
    const store = document.getElementById("filterStore").value;

    let result = cars.filter(c => {
        return (!brand || c.brand === brand) &&
               (!price || c.price == price) &&
               (!area  || c.area === area) &&
               (!store || c.store === store);
    });

    loadCars(result);
});

/* ----------------- STEP 1 → STEP 2 ----------------- */
let selectedCar = null;

function selectCar(id) {
    selectedCar = cars.find(c => c.id === id);
    document.getElementById("step1").classList.remove("active");
    document.getElementById("step2").classList.add("active");
}

/* ----------------- STEP 2 Tabs ----------------- */
document.querySelectorAll(".tab").forEach(btn => {
    btn.addEventListener("click", () => {
        document.querySelectorAll(".tab").forEach(t => t.classList.remove("active"));
        btn.classList.add("active");

        const tab = btn.dataset.tab;

        const content = {
            doc: "<h3>取車應備文件</h3><p>身分證、駕照、押金 NT$10,000</p>",
            note: "<h3>租車注意事項</h3><p>需持有效駕照 · 不得酒駕</p>",
            insurance: "<h3>基礎保險</h3><p>含強制險、第三責任險</p>",
            cancel: "<h3>取消政策</h3><p>3 天前取消收 30%，當日取消收 100%</p>"
        };
        document.getElementById("tabContent").innerHTML = content[tab];
    });
});

/* ----------------- STEP 2 → STEP 3 ----------------- */
function goStep3() {
    document.getElementById("step2").classList.remove("active");
    document.getElementById("step3").classList.add("active");

    document.getElementById("s3_carImg").src = selectedCar.img;
    document.getElementById("s3_carName").innerText = selectedCar.name;

    updateCalculation();
}

/* ----------------- 試算 ----------------- */
function updateCalculation() {
    const month = parseInt(document.getElementById("s3_month").value);

    let mileageBonus = 0;
    if (month === 6 || month === 9) mileageBonus = 200;
    if (month === 12) mileageBonus = 400;

    document.getElementById("s3_mileageBonus").innerText = mileageBonus;

    const price = selectedCar.price;
    const totalFee = price * month;
    document.getElementById("s3_totalFee").innerText = "NT$ " + totalFee;

    const finalTotal = totalFee + 10000;
    document.getElementById("s3_finalTotal").innerText = "NT$ " + finalTotal;
}

document.getElementById("s3_month").addEventListener("change", updateCalculation);

/* ----------------- 返回 ----------------- */
function backToStep1() {
    document.getElementById("step2").classList.remove("active");
    document.getElementById("step1").classList.add("active");
}
function backToStep2() {
    document.getElementById("step3").classList.remove("active");
    document.getElementById("step2").classList.add("active");
}

/* ----------------- 完成訂閱 ----------------- */
function confirmOrder() {
    alert("訂閱完成！（等一下可串後端）");
}
