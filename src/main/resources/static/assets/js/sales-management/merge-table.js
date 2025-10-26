const modalMergeTableModal = $('#mergeTableModal');

$('#btn-merge-table').click(async () => {
    modalMergeTableModal.modal('show');
    try {
        const response = await $.ajax({
            url: '/admin/table/get-table-merge',
            method: 'GET',
            contentType: 'application/json'
        });

        const listTableFrom = $('#listTableForm');
        const listTableFromRes = response.data.tablesTo;

        listTableFromRes.forEach(tableItem => {
            const formCheck = `
            <div class="form-check">
                <input class="form-check-input" type="checkbox" value=${tableItem.tableId}>
                <label class="form-check-label" for="sourceCheck1">${tableItem.tableName}</label>
            </div>
            `
            listTableFrom.append(formCheck);
        });

        const listTableTo = $('#listTableTo');
        const listTableToRes = response.data.tablesFrom;

        listTableToRes.forEach(tableItem => {
            const formRadio = `
            <div class="form-check">
                <input class="form-check-input" type="radio" value=${tableItem.tableId}>
                <label class="form-check-label" for="destRadio1">${tableItem.tableName}</label>
            </div>
            `
            listTableTo.append(formRadio);
        });


    } catch (error) {
        const msg = error.responseJSON?.message || 'Không thể tải danh sách bàn!';
        showToast(msg, 'danger');
    }
})

$(`#comfirmMergeTabel`).click(async () => {
    let listIdTableFrom = [];

    const btn = $(`#comfirmMergeTabel`);

    btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Đang xử lý...');

    $('#listTableForm input[type="checkbox"]:checked').each(function () {
        listIdTableFrom.push($(this).val());
    });

    const tableToId = $('#listTableTo input[type="radio"]:checked').val();

    try {
        if (listIdTableFrom.length < 2) {
            throw new Error('Bạn cần gộp ít nhất 2 bàn!');
        }

        if (!tableToId) {
            throw new Error('Vui lòng chọn bàn gộp đến!');
        }

        const data = {
            listIdTableFrom, tableToId
        };

        const response = await $.ajax({
            url: '/admin/table/merge-table',
            method: 'POST',
            data: JSON.stringify(data),
            contentType: 'application/json'
        });

        showToast(response.message, 'success');
        modalMergeTableModal.modal('hide');
        setTimeout(() => location.reload(), 1000);
    } catch (error) {
        console.log("Check error: " + error.responseJSON?.message);

        const msg = error.responseJSON?.message || error.message;
        showToast(msg, 'danger');
    } finally {
        btn.prop('disabled', false).text('Gộp');
    }
})