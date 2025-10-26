const modalMoveToTable = $('#moveToTableModal');

$('#btn-move-table').click(async () => {
    const table = JSON.parse(sessionStorage.getItem('selectedTable'));
    modalMoveToTable.modal('show');

    try {
        const response = await $.ajax({
            url: '/admin/table/get-table-move/' + table.id,
            method: 'GET',
            contentType: 'application/json'
        });

        const tables = response.data;

        const selectTable = $('#listTable');

        selectTable.empty();

        tables.forEach(tableItem => {
            if (tableItem.tableId !== table.id) {
                const option = $('<option></option>')
                    .val(tableItem.tableId)
                    .text(tableItem.tableName);
                selectTable.append(option);
            }
        });

        if (selectTable.children().length === 0) {
            selectTable.append('<option value="">Không có bàn khả dụng</option>');
        }
    } catch (xhr) {
        console.log("Error: " + xhr.responseJSON.message);
        const msg = xhr.responseJSON?.message || 'Không thể tải danh sách bàn!';
        showToast(msg, 'danger');
    }
});

$('#comfirmMoveTabe').click(async () => {
    const btn = $(this);
    const selectedTableId = $('#listTable').val();
    btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Đang xử lý...');

    try {
        if (!selectedTableId) {
            throw new Error('Vui lòng chọn một bàn để chuyển!');
        }

        const selectedTable = sessionStorage.getItem('selectedTable');

        const table = JSON.parse(selectedTable);

        const response = await $.ajax({
            url: '/admin/table/move-table',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                fromTableId: table.id,
                toTableId: selectedTableId
            })
        });

        showToast(response.message, 'success');
        modalMoveToTable.modal('hide');
        sessionStorage.removeItem('selectedTable');
        setTimeout(() => location.reload(), 1000);
    } catch (error) {
        const msg = error.responseJSON?.message || 'Chuyển bàn thất bại!';
        showToast(msg, 'danger');
    } finally {
        btn.prop('disabled', false).text('Chuyển');
    }
})