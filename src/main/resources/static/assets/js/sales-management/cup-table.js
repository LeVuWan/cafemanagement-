const modalCutTable = $('#cupTableModal');


const populateTableSelect = async (tableId) => {
    const response = await $.ajax({
        url: '/admin/table/get-table-cut/' + tableId,
        method: 'GET',
        contentType: 'application/json'
    });

    const tables = response.data;
    const selectTable = $('#destinationTableSelect');
    selectTable.empty();
    selectTable.append('<option value="" selected>-- Chọn bàn đích --</option>');

    tables.forEach(tableItem => {
        if (tableItem.tableId !== tableId) {
            const option = $('<option></option>')
                .val(tableItem.tableId)
                .text(tableItem.tableName);
            selectTable.append(option);
        }
    });

    if (selectTable.children().length === 0) {
        selectTable.append('<option value="">Không có bàn khả dụng</option>');
    }
};

const populateMenuTable = async (tableId) => {
    const response = await $.ajax({
        url: '/admin/table/get-information-table/' + tableId,
        method: 'GET',
        contentType: 'application/json'
    });

    const tbody = $('#menuTableForm');
    tbody.empty();

    const inforTable = response.data;

    inforTable.infoMenuRes.forEach(item => {
        const row = `
                <tr>
                    <td class="text-center">
                        <div class="custom-control custom-checkbox">
                            <input class="form-check-input checkBox" type="checkbox" value="${item.menuId}" />
                        </div>
                    </td>
                    <td class="dishName">${item.dishName}</td>
                    <td>
                        <div class="input-group input-group-sm">
                            <input id="${item.menuId}" type="text" class="form-control text-center cut-quantity integer" value="0" min="0" pattern="\d*" maxlength="3"/>
                        </div>
                    </td>
                    <td class="text-center currentQuantity"><strong>${item.quantity}</strong></td>
                </tr>
  `;
        tbody.append(row);
    });

};

$('#btn-cup-table').click(async () => {
    const table = JSON.parse(sessionStorage.getItem('selectedTable'));
    $('#cupTableModalLabel').text(`Tách ${table.name}`);
    const tableId = table.id;

    try {
        await Promise.all([populateTableSelect(tableId), populateMenuTable(tableId, table.name)]);
        resetManagementButtons();
    } catch (error) {
        const msg = error.responseJSON?.message || 'Đã xảy ra lỗi';
        showToast(msg, 'danger');
    }
});

$('#destinationTableSelect').on('change', async function () {
    const tableToId = $(this).val();
    const tbody = $('#menuTableTo');
    tbody.empty();

    if (!tableToId || tableToId === '') {
        tbody.append(`
            <tr>
                <td colspan="3" class="text-center text-muted">Vui lòng chọn bàn đích.</td>
            </tr>
        `);
        return;
    }

    try {
        const response = await $.ajax({
            url: '/admin/table/get-information-table/' + tableToId,
            method: 'GET',
            contentType: 'application/json'
        });

        const inforTable = response.data;

        if (inforTable.infoMenuRes && inforTable.infoMenuRes.length > 0) {
            inforTable.infoMenuRes.forEach(item => {
                const row = `
                    <tr>
                        <td>${item.dishName}</td>
                        <td class="text-center"><strong>${item.quantity}</strong></td>
                    </tr>
                `;
                tbody.append(row);
            });
        } else {
            tbody.append(`
                <tr>
                    <td colspan="3" class="text-center text-muted">Chưa có món nào.</td>
                </tr>
            `);
        }
    } catch (error) {
        if (error.responseJSON?.data == null) {
            tbody.append(`
                <tr>
                    <td colspan="3" class="text-center text-muted">Chưa có món nào.</td>
                </tr>
            `);
            return;
        }

        const msg = error.responseJSON?.message || 'Lỗi khi tải menu bàn đích';
        showToast(msg, 'danger');
    }
});

$('#confirmMergeTable').click(async () => {
    const tableToId = $('#destinationTableSelect').val();
    const button = $('#confirmMergeTable');
    const tableFromId = JSON.parse(sessionStorage.getItem('selectedTable')).id;
    const listMenuCut = [];

    button.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Đang xử lý...');

    if (!tableToId) {
        showToast('Vui lòng chọn bàn đích', 'warning');
        resetButton();
        return;
    }

    let allChecked = true;
    let allQuantitiesEqual = true;
    let checkLimit = false;          // số lượng vượt quá
    let missingQuantity = false;     // checkbox đã chọn nhưng không nhập số lượng

    const rows = $('#menuTableForm tr');
    const totalRows = rows.length;

    rows.each((index, row) => {
        const checkbox = $(row).find('.checkBox');
        const menuId = checkbox.val();
        const cutQuantity = parseInt($(row).find('.cut-quantity').val()) || 0;
        const currentQuantity = parseInt($(row).find('.currentQuantity strong').text());
        const menuName = $(row).find('.dishName').text().trim();

        if (checkbox.is(':checked')) {

            // ❌ Checkbox đã tích nhưng không nhập số lượng hợp lệ
            if (cutQuantity <= 0) {
                missingQuantity = true;
                showToast(`Vui lòng nhập số lượng cho ${menuName}`, 'warning');
                return false; // DỪNG VÒNG LẶP
            }

            // ❌ Số lượng tách lớn hơn số lượng hiện tại
            if (cutQuantity > currentQuantity) {
                checkLimit = true;
                return false;
            }

            // Thêm vào danh sách gửi đi
            listMenuCut.push({ menuId, quantity: cutQuantity });

            // Kiểm tra tách toàn bộ
            if (cutQuantity !== currentQuantity) {
                allQuantitiesEqual = false;
            }

        } else {
            allChecked = false;
        }
    });

    // 👉 Sau vòng lặp, kiểm tra lỗi
    if (missingQuantity) {
        resetButton();
        return;
    }

    if (checkLimit) {
        showToast('Số lượng tách vượt quá số lượng hiện tại', 'warning');
        resetButton();
        return;
    }

    if (listMenuCut.length === 0) {
        showToast('Vui lòng chọn ít nhất một món để tách', 'warning');
        resetButton();
        return;
    }

    // Dữ liệu gửi lên server
    const data = {
        fromTableId: tableFromId,
        toTableId: tableToId,
        menu: listMenuCut,
        isCheckAll: allChecked && allQuantitiesEqual && listMenuCut.length === totalRows
    };

    try {
        const response = await $.ajax({
            url: '/admin/table/cut-table',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data)
        });

        showToast(response.message || 'Tách bàn thành công!', 'success');
        $('#cupTableModal').modal('hide');
        sessionStorage.removeItem('selectedTable');
        await getListTable();

    } catch (error) {
        const msg = error.responseJSON?.message || 'Tách bàn thất bại!';
        console.log("Error: " + msg);
        showToast(msg, 'danger');
    } finally {
        resetButton();
    }

    // Đặt lại nút
    function resetButton() {
        button.prop('disabled', false).text('Xác nhận');
    }
});

$('#cupTableModal').on('hidden.bs.modal', () => {
    sessionStorage.removeItem('selectedTable');
    $('#menuTableTo').empty();
    $('#destinationTableSelect').val('');
});