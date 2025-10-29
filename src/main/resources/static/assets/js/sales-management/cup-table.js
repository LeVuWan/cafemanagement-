const modalCutTable = $('#cupTableModal');


const populateTableSelect = async (tableId) => {
    const response = await $.ajax({
        url: '/admin/table/get-table-move/' + tableId,
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
                    <td>${item.dishName}</td>
                    <td>
                        <div class="input-group input-group-sm">
                            <input type="number" class="form-control text-center cut-quantity" value="0" min="0" max="${item.quantity}">
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
    const tableId = table.id;

    try {
        await Promise.all([populateTableSelect(tableId), populateMenuTable(tableId, table.name)]);
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
                    <td colspan="3" class="text-center text-muted">Chưa có món nào 1.</td>
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

    if (!tableToId || tableToId === '') {
        showToast('Vui lòng chọn bàn đích', 'warning');
        button.prop('disabled', false).text('Xác nhận');
        return;
    }

    let allChecked = true;
    let allQuantitiesEqual = true;
    const totalRows = $('#menuTableForm tr').length;
    let checkLimit = false;
    $('#menuTableForm tr').each((index, element) => {
        const checkbox = $(element).find('.checkBox');
        const menuId = checkbox.val();
        const cutQuantity = parseInt($(element).find('.cut-quantity').val()) || 0;
        const currentQuantity = parseInt($(element).find('.currentQuantity strong').text());


        // Kiểm tra nếu checkbox được tích
        if (checkbox.is(':checked')) {
            // Kiểm tra số lượng tách có lớn hơn số lượng hiện tại không
            if (cutQuantity > currentQuantity) {
                // Xóa danh sách để ngăn gửi dữ liệu
                checkLimit = true;
                return; // Thoát vòng lặp
            }
            if (cutQuantity > 0) {
                listMenuCut.push({ menuId, quantity: cutQuantity }); // Chỉ gửi menuId và cutQuantity
            }
            // Kiểm tra nếu số lượng tách bằng số lượng hiện tại
            if (cutQuantity !== currentQuantity) {
                allQuantitiesEqual = false;
            }
        } else {
            allChecked = false;
        }
    });

    if (checkLimit) {
        showToast(`Số lượng tách của món vượt quá số lượng hiện tại`, 'warning');
        listMenuCut.length = 0;
        button.prop('disabled', false).text('Xác nhận');
        return;
    }

    // Kiểm tra nếu không có checkbox nào được tích hoặc không có món hợp lệ
    if (listMenuCut.length === 0) {
        showToast('Vui lòng chọn ít nhất một món để tách', 'warning');
        button.prop('disabled', false).text('Xác nhận');
        return;
    }

    // Kiểm tra nếu tất cả checkbox được tích và số lượng tách bằng số lượng hiện tại
    if (allChecked && listMenuCut.length === totalRows && allQuantitiesEqual) {
        console.log('Tất cả checkbox được tích và số lượng tách bằng số lượng hiện tại cho tất cả món.');
    }

    // Gửi yêu cầu AJAX để tách bàn
    try {
        const data = {
            fromTableId: tableFromId,
            toTableId: tableToId,
            menus: listMenuCut,
            isCheckAll: allChecked && listMenuCut.length === totalRows && allQuantitiesEqual
        };

        const response = await $.ajax({
            url: '/admin/table/cut-table',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data)
        });

        showToast(response.message || 'Tách bàn thành công!', 'success');
        $('#cupTableModal').modal('hide');
        sessionStorage.removeItem('selectedTable');
        setTimeout(() => location.reload(), 1000);
    } catch (error) {
        const msg = error.responseJSON?.message || 'Tách bàn thất bại!';
        console.log("Check msg: " + msg);

        showToast(msg, 'danger');
    } finally {
        button.prop('disabled', false).text('Xác nhận');
    }
});

