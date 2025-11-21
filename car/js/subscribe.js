/* ============================================================
   從後端載入車款資料
============================================================ */
let cars = [];
let filteredCars = [];
let currentIndex = 0;
const CARDS_PER_PAGE = 3;

function loadFromBackend() {
    fetch("http://localhost:8080/api/cars")
        .then(res => res.json())
        .then(data => {
            cars = data;
            filteredCars = data;
            initFilters();
            loadCarousel(filteredCars);
        })
        .catch(err => console.error("讀取後端資料失敗：", err));
}

loadFromBackend();


/* ============================================================
   Carousel：渲染車卡
============================================================ */
function loadCarousel(list) {
    const track = document.getElementById("carouselTrack");
    track.innerHTML = "";

    list.forEach(c => {
        track.innerHTML += `
            <div class="car-card">
                <img src="${c.img}">
                <h3>${c.name}</h3>
                <p>品牌：${c.brand}</p>
                <p>月費：NT$ ${c.price}</p>
                <button class="select-btn" onclick="selectCar(${c.id})">確認選擇</button>
            </div>
        `;
    });

    // ⭐ track 寬度固定計算
    track.style.width = `${list.length * 340}px`;

    currentIndex = 0;
    updateCarousel();
}


/* ============================================================
   Carousel 左右導航按鈕
============================================================ */
document.getElementById("carNext").addEventListener("click", () => {
    const maxIndex = Math.ceil(filteredCars.length / CARDS_PER_PAGE) - 1;
    if (currentIndex <= maxIndex+1) currentIndex++;
    updateCarousel();
});

document.getElementById("carPrev").addEventListener("click", () => {
    if (currentIndex >= 1) currentIndex--;
    updateCarousel();
});


/* ============================================================
   Carousel 移動動畫
============================================================ */
function updateCarousel() {
    const track = document.getElementById("carouselTrack");
    const shift = currentIndex * -(340);
    track.style.transform = `translateX(${shift}px)`;
}



/* ============================================================
   初始化篩選選單
============================================================ */
function initFilters() {
    const brandSelect = document.getElementById("filterBrand");
    const priceSelect = document.getElementById("filterPrice");
    const areaSelect = document.getElementById("filterArea");
    const storeSelect = document.getElementById("filterStore");

    brandSelect.innerHTML = `<option value="">品牌</option>`;
    priceSelect.innerHTML = `<option value="">月費</option>`;
    areaSelect.innerHTML = `<option value="">取車區域</option>`;
    storeSelect.innerHTML = `<option value="">取車據點</option>`;

    /* 動態產生品牌 */
    [...new Set(cars.map(c => c.brand))].forEach(b => {
        brandSelect.innerHTML += `<option value="${b}">${b}</option>`;
    });

    /* 動態產生月費 */
    [...new Set(cars.map(c => c.price))].sort().forEach(p => {
        priceSelect.innerHTML += `<option value="${p}">${p}</option>`;
    });

    /* 固定區域 */
    ["北區", "中區", "南區"].forEach(a => {
        areaSelect.innerHTML += `<option value="${a}">${a}</option>`;
    });

    /* 固定據點 */
    ["台北車站", "羅東車站", "台中車站", "高雄車站"].forEach(s => {
        storeSelect.innerHTML += `<option value="${s}">${s}</option>`;
    });
}



/* ============================================================
   搜尋
============================================================ */
function applyFilters() {

    const brand = document.getElementById("filterBrand").value;
    const price = document.getElementById("filterPrice").value;
    const area = document.getElementById("filterArea").value;
    const store = document.getElementById("filterStore").value;

    filteredCars = cars.filter(c => {
        return (!brand || c.brand === brand) &&
               (!price || c.price == price) &&
               (!area  || c.area === area) &&
               (!store || c.store === store);
    });

    currentIndex = 0;       // 
    loadCarousel(filteredCars);
}

/*  直接觸發搜尋 */
document.getElementById("filterBrand").addEventListener("change", applyFilters);
document.getElementById("filterPrice").addEventListener("change", applyFilters);
document.getElementById("filterArea").addEventListener("change", applyFilters);
document.getElementById("filterStore").addEventListener("change", applyFilters);

/* ============================================================
   STEP 1 → STEP 2
============================================================ */
let selectedCar = null;

function selectCar(id) {
    selectedCar = filteredCars.find(c => c.id === id);

    step1.classList.remove("active");
    step2.classList.add("active");
}



/* ============================================================
   STEP 2 → STEP 3（Tabs）
============================================================ */
// STEP2 初始顯示 tab1
tabContent.innerHTML = `
    <h3>取車應備文件</h3>
    <p>需攜帶：身分證、駕照、押金 NT$10,000</p>
`;

document.querySelectorAll(".tab").forEach(btn => {
    btn.addEventListener("click", () => {

        document.querySelectorAll(".tab").forEach(t => t.classList.remove("active"));
        btn.classList.add("active");

        const tab = btn.dataset.tab;

        const content = {
            doc: `<h3>取車應備文件</h3><p>需攜帶：身分證、駕照、押金 NT$10,000</p>`,
            note: `<h3>注意事項</h3><p>不得酒駕 · 必須持有效駕照</p>`,
            insurance: `<h3>保險內容</h3><p>含強制險、第三責任險</p>`,
            cancel: `<h3>取消政策</h3><p>3天前退30%，當日退0%</p>`
        };

        tabContent.innerHTML = content[tab];
    });
});



/* ============================================================
   STEP 2 → STEP 3
============================================================ */
function goStep3() {
    step2.classList.remove("active");
    step3.classList.add("active");

    s3_carImg.src = selectedCar.img;
    s3_carName.textContent = selectedCar.name;

    updateCalculation();
}



/* ============================================================
   試算功能
============================================================ */
function updateCalculation() {
    const m = parseInt(s3_month.value);

    let bonus = (m === 6 || m === 9) ? 200 : (m === 12 ? 400 : 0);
    s3_mileageBonus.textContent = bonus;

    const totalFee = selectedCar.price * m;
    s3_totalFee.textContent = "NT$ " + totalFee;

    const finalTotal = totalFee + 10000;
    s3_finalTotal.textContent = "NT$ " + finalTotal;
}

s3_month.addEventListener("change", updateCalculation);



/* ============================================================
   完成訂閱
============================================================ */
function confirmOrder() {

    const payload = {
        carId: selectedCar.id,
        memberId: 1,  // 之後換 JWT
        store: s3_store.value,
        startDate: s3_date.value,
        startTime: s3_time.value,
        months: parseInt(s3_month.value),
        mileageBonus: parseInt(s3_mileageBonus.textContent),
        totalPrice: selectedCar.price * parseInt(s3_month.value),
        finalPrice: selectedCar.price * parseInt(s3_month.value) + 10000
    };

    fetch("http://localhost:8080/api/subscribe", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
        .then(res => res.json())
        .then(data => {
            alert("訂閱成功！訂單編號：" + data.id);
        })
        .catch(err => alert("訂閱失敗：" + err));
}



/* ============================================================
   返回按鈕
============================================================ */
function backToStep1() {
    step2.classList.remove("active");
    step1.classList.add("active");
}

function backToStep2() {
    step3.classList.remove("active");
    step2.classList.add("active");
}
