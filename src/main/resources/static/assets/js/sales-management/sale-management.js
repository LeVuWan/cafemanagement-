document.addEventListener("DOMContentLoaded", function () {
    const management = document.getElementById("management-functions");

    document.querySelector("#list-table").addEventListener("click", function (e) {
        const table = e.target.closest(".table-item");
        if (!table) return;

        // xử lý sự kiện click
        document.querySelectorAll(".table-item").forEach(item => {
            item.classList.remove("table-selected");
        });

        table.classList.add("table-selected");

        const tableId = table.dataset.id;
        const tableName = table.dataset.name;
        const tableStatus = table.dataset.status;

        const selectedTable = {
            id: tableId,
            name: tableName,
            status: tableStatus
        };

        sessionStorage.setItem("selectedTable", JSON.stringify(selectedTable));

        management.style.display = "block";

        toggleButtonsByStatus(selectedTable.status);
    });

    const toggleButtonsByStatus = (status) => {
        const buttons = {
            xemBan: document.getElementById("btn-infor-table"),
            chuyenBan: document.getElementById("btn-move-table"),
            tachBan: document.getElementById("btn-cup-table"),
            gopBan: document.getElementById("btn-merge-table"),
            huyBan: document.getElementById("btn-cancel-table"),
            datBan: document.getElementById("btn-dat-ban"),
            chonThucDon: document.getElementById("btn-choose-menu"),
            thanhToan: document.getElementById("btn-payment"),
        };

        Object.values(buttons).forEach(btn => btn.style.display = "none");

        switch (status) {
            case "AVAILABLE":
                buttons.datBan.style.display = "inline-block";
                buttons.chonThucDon.style.display = "inline-block";
                break;
            case "RESERVED":
                buttons.huyBan.style.display = "inline-block";
                buttons.chonThucDon.style.display = "inline-block";
                break;
            case "OCCUPIED":
                buttons.xemBan.style.display = "inline-block";
                buttons.chuyenBan.style.display = "inline-block";
                buttons.tachBan.style.display = "inline-block";
                buttons.gopBan.style.display = "inline-block";
                buttons.huyBan.style.display = "inline-block";
                buttons.thanhToan.style.display = "inline-block";
                buttons.chonThucDon.style.display = "inline-block";
                break;
        }
    };
});
