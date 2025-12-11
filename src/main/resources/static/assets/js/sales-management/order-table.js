$('#orderTableModal').on('show.bs.modal', function () {
    const table = JSON.parse(sessionStorage.getItem('selectedTable'));
    document.getElementById('tableName').value = table.name;
    document.getElementById('tableId').value = table.id;
});

$('#btn-confirm-order').on('click', async () => {
    const btn = $('#btn-confirm-order');

    btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Đang xử lý...');

    const table = JSON.parse(sessionStorage.getItem('selectedTable') || '{}');

    const form = $('#orderTableForm')[0];

    if (!form.validateForm()) {
        btn.prop('disabled', false).text('Xác nhận đặt bàn');
        return;
    }

    const durationMinutes = parseInt(form.durationMinutes.value, 10);

    const data = {
        tableId: table.id,
        customerName: document.getElementById('customerName').value.trim(),
        customerPhone: document.getElementById('phoneNumber').value.trim(),
        durationMinutes: durationMinutes
    };

    try {
        const response = await $.ajax({
            url: '/admin/table/order',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data)
        });

        showToast(response.message, 'success');
        form.reset();
        $('#orderTableModal').modal('hide');
        sessionStorage.removeItem('selectedTable');

        // Có thể chỉ reload phần danh sách bàn, không cần full page
        await getListTable();
    } catch (xhr) {
        const msg = xhr.responseJSON?.message || 'Đặt bàn thất bại!';
        showToast(msg, 'danger');
    } finally {
        btn.prop('disabled', false).text('Xác nhận đặt bàn');
    }
});

$('#orderTableModal').on('hide.bs.modal', function () {
    const form = $('#orderTableForm');

    form.trigger('reset');

    form.find('.form-message').text('');

    form.find('.form-control').removeClass('invalid');
});

const showToast = (message, type = 'success') => {
    const toastEl = document.getElementById('liveToast');
    const toastBody = document.getElementById('toast-message');

    toastEl.classList.remove('bg-success', 'bg-danger', 'bg-warning');
    if (type === 'success') toastEl.classList.add('bg-success');
    else if (type === 'danger') toastEl.classList.add('bg-danger');
    else toastEl.classList.add('bg-warning');

    toastBody.textContent = message;

    const toast = new bootstrap.Toast(toastEl, { delay: 5000 });
    toast.show();
}

$('#orderTableModal').on('show.bs.modal', function () {
    resetManagementButtons();
});
