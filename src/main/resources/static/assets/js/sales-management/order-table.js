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
    const data = {
        tableId: table.id,
        customerName: form.customerName.value.trim(),
        customerPhone: form.customerPhone.value.trim(),
        dateOrder: form.dateOrder.value,
        timeOrder: form.timeOrder.value
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
        setTimeout(() => location.reload(), 1000);
    } catch (xhr) {
        const msg = xhr.responseJSON?.message || 'Đặt bàn thất bại!';
        showToast(msg, 'danger');
    } finally {
        btn.prop('disabled', false).text('Xác nhận đặt bàn');
    }
});


const showToast = (message, type = 'success') => {
    const toastEl = document.getElementById('liveToast');
    const toastBody = document.getElementById('toast-message');

    toastEl.classList.remove('bg-success', 'bg-danger', 'bg-warning');
    if (type === 'success') toastEl.classList.add('bg-success');
    else if (type === 'danger') toastEl.classList.add('bg-danger');
    else toastEl.classList.add('bg-warning');

    toastBody.textContent = message;

    const toast = new bootstrap.Toast(toastEl, { delay: 3000 });
    toast.show();
}

