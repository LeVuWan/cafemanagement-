document.addEventListener("DOMContentLoaded", function () {
    const tables = document.querySelectorAll(".table-item");
    const managementFunctions = document.getElementById("management-functions");

    // Lấy tất cả các nút để dễ điều khiển
    const btnDatBan = document.getElementById("btn-dat-ban");
    const btnChonThucDon = document.getElementById("btn-chon-thuc-don");
    const otherButtons = [
        "btn-xem-ban",
        "btn-chuyen-ban",
        "btn-tach-ban",
        "btn-gop-ban",
        "btn-huy-ban",
        "btn-thanh-toan",
        "btn-in-an"
    ].map(id => document.getElementById(id));

    tables.forEach(table => {
        table.addEventListener("click", () => {
            // Xóa class selected khỏi tất cả bàn
            tables.forEach(t => t.classList.remove("selected"));
            table.classList.add("selected");

            // Hiện phần nút
            managementFunctions.style.display = "block";

            // Lấy status
            const status = table.dataset.status;

            if (status === "IS_EMPTY") {
                // Ẩn tất cả các nút khác
                otherButtons.forEach(btn => btn.style.display = "none");
                btnChonThucDon.style.display = "none";
                btnDatBan.style.display = "inline-block";
            } else {
                // Hiện tất cả các nút trừ "Chọn thực đơn"
                btnDatBan.style.display = "none";
                btnChonThucDon.style.display = "none"; // ẩn chọn thực đơn
                otherButtons.forEach(btn => btn.style.display = "inline-block");
            }
        });
    });
});
