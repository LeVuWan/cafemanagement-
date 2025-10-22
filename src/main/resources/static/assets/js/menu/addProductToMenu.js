document.addEventListener("DOMContentLoaded", function () {
    const addButton = document.querySelector("#exampleModal .btn-primary"); // nút "Thêm" trong modal
    const tableBody = document.querySelector("table.table-sm tbody"); // tbody bảng ngoài
    const toastElement = document.getElementById('liveToast');
    const toast = new bootstrap.Toast(toastElement);

    addButton.addEventListener("click", function () {
        const rows = document.querySelectorAll("#exampleModal tbody tr");
        let hasEmptyQuantity = false;
        let hasDuplicate = false;

        rows.forEach(row => {
            const checkbox = row.querySelector('input[type="checkbox"]');
            const quantityInput = row.querySelector('input[type="number"]');

            if (checkbox.checked) {
                const productName = row.querySelector('td:nth-child(1)').textContent.trim();
                const unitName = row.querySelector('td:nth-child(2)').textContent.trim();
                const quantity = quantityInput.value.trim();
                const productId = row.querySelector('td:nth-child(4)').textContent.trim();
                const unitId = row.querySelector('td:nth-child(5)').textContent.trim();

                if (quantity === "") {
                    hasEmptyQuantity = true;
                    return;
                }

                const existingRows = tableBody.querySelectorAll("tr");
                let isDuplicate = false;

                existingRows.forEach(existingRow => {
                    const existingProductId = existingRow.querySelector("td:nth-child(4)")?.textContent.trim();
                    if (existingProductId === productId) {
                        isDuplicate = true;
                    }
                });

                if (isDuplicate) {
                    hasDuplicate = true;
                    return;
                }

                const newRow = document.createElement("tr");
                newRow.innerHTML = `
                    <td>${productName}</td>
                    <td>${quantity}</td>
                    <td>${unitName}</td>
                    <td style="display:none;">${productId}</td>
                    <td style="display:none;">${unitId}</td>
                    <td class="text-center"><button type="button" class="btn btn-danger delete-btn">Delete</button></td>
                `;

                tableBody.appendChild(newRow);

                checkbox.checked = false;
                quantityInput.value = "";
            }
        });


        if (hasEmptyQuantity) {
            toastElement.querySelector('.toast-body').textContent = "Vui lòng nhập khối lượng cho tất cả thành phần đã chọn.";
            toast.show();
            return;
        }

        if (hasDuplicate) {
            toastElement.querySelector('.toast-body').textContent = "Một số thành phần đã tồn tại trong danh sách.";
            toast.show();
            return;
        }
    });

    tableBody.addEventListener("click", function (event) {
        if (event.target.classList.contains("delete-btn")) {
            const row = event.target.closest("tr");
            if (row) row.remove();
        }
    });


});
