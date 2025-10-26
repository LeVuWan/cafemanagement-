document.addEventListener("DOMContentLoaded", function () {
    const tables = document.querySelectorAll(".table-item");
    const management = document.getElementById("management-functions");
    tables.forEach(table => {
        table.addEventListener("click", () => {
            tables.forEach(item => {
                item.classList.remove('table-selected');
            });

            table.classList.add('table-selected');

            const tableId = table.getAttribute("data-id");
            const tableName = table.getAttribute("data-name");
            const tableStatus = table.getAttribute("data-status");

            const selectedTable = {
                id: tableId, name: tableName, status: tableStatus
            };

            sessionStorage.setItem("selectedTable", JSON.stringify(selectedTable));

            management.style.display = "block"

            toggleButtonsByStatus(selectedTable.status);
        })
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
            thanhToan: document.getElementById("btn-thanh-toan"),
            inAn: document.getElementById("btn-in-an"),
        }

        Object.values(buttons).forEach(btn => btn.style.display = "none");

        switch (status) {
            case "AVAILABLE":
                buttons.datBan.style.display = "inline-block";
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
                buttons.inAn.style.display = "inline-block";
                buttons.chonThucDon.style.display = "inline-block";
                break;
        }
    }
});
