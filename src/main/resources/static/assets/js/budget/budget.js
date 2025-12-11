const modalAddNewExpense = $('#addNewExpense');

const fetchBudgetData = async () => {
    const btn = $('#fetchBudget');
    const table = $('#dataTable'); // ✅ dùng id cụ thể
    const from = $('#fromDate').val();
    const to = $('#toDate').val();

    if (checkDateRange(from, to) === false) return;

    btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Đang xử lý...');

    try {
        const response = await $.ajax({
            url: `/admin/report/general?from=${from}&to=${to}`,
            method: 'GET',
        });

        console.log("Raw response:", response);

        const data = Array.isArray(response.data) ? response.data : response;

        table.find('tbody').empty();

        let tbody = '';

        data.forEach(r => {
            tbody += `
                <tr>
                    <td>${formatDate(r.date)} </td>
                    <td class="text-right">${(r.income || 0).toLocaleString('vi-VN')} đ</td>
                    <td class="text-right">${(r.expense || 0).toLocaleString('vi-VN')} đ</td>
                </tr>
            `;
        });

        table.find('tbody').append(tbody);

        showToast(response.message || 'Tải dữ liệu thành công!', 'success');
    } catch (error) {
        console.log("Check error:", error);
        const msg = error.responseJSON?.message || 'Load report thất bại!';
        showToast(msg, 'danger');
    } finally {
        btn.prop('disabled', false).text('Xem');
    }
};

$('#fetchBudget').on('click', async () => {
    await fetchBudgetData();
});


