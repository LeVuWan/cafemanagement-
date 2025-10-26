const modalCancel = $('#cancelTableModal');

$('#btn-cancel-table').click(async () => {
    const table = JSON.parse(sessionStorage.getItem('selectedTable'));
    $('#cancelTableModalLabel').text('Bạn muốn xóa ' + table.name);
})

$('#btn-confirm-cancel').click(async () => {
    const btn = $('#btn-confirm-cancel');
    const table = JSON.parse(sessionStorage.getItem('selectedTable'));
    btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Đang xử lý...');
    try {
        const response = await $.ajax({
            url: '/admin/table/cancel-table/' + table.id,
            method: 'POST',
            contentType: 'application/json',
        });

        showToast(response.message || 'Hủy bàn thành công', 'success');
        modalCancel.modal('hide');
        sessionStorage.removeItem('selectedTable');

        setTimeout(() => location.reload(), 1000);
    } catch (error) {
        const msg = error.responseJSON?.message || 'Thêm món thất bại!';
        showToast(msg, 'danger');
    } finally {
        btn.prop('disabled', false).text('Có');
    }
})