const getListTable = async () => {
    const res = await $.ajax({
        url: '/admin/table/get-list-table',
        method: 'GET',
        contentType: 'application/json'
    });
    renderTables(res.data);
}

const renderTables = (tables) => {
    const container = document.querySelector("#list-table");
    container.innerHTML = "";

    tables.forEach(table => {
        const isAvailable = table.status === "AVAILABLE";

        const item = `
            <div class="col-6 col-md-4 col-lg-3 col-xl-2 mb-3">
                <div class="
                    table-item p-3 rounded shadow-sm border border-dark text-center fw-bold
                    d-flex flex-column justify-content-center align-items-center hover-effect
                    ${isAvailable ? "bg-success text-white" : "bg-warning text-dark"}
                "
                data-id="${table.tableId}"
                data-name="${table.tableName}"
                data-status="${table.status}">
                    <span class="fs-5 mb-2">${table.tableName}</span>
                    <span class="badge bg-secondary">
                        ${table.status === "AVAILABLE"
                ? "CÒN TRỐNG"
                : table.status === "RESERVED"
                    ? "ĐÃ ĐẶT TRƯỚC"
                    : "ĐANG SỬ DỤNG"
            }
                    </span>
                </div>
            </div>
        `;
        container.insertAdjacentHTML("beforeend", item);
    });
};
