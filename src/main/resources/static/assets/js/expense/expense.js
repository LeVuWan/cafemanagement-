$('#comfirmAddNewExpense').on('click', async () => {
    const btn = $('#comfirmAddNewExpense');
    btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Đang xử lý...');
    try {
        const data = {
            expenseDate: $('#expenseDate').val(),
            expenseName: $('#expenseName').val(),
            amount: $('#amount').val(),
        }

        const response = await $.ajax({
            url: '/admin/expense/create',
            method: 'POST',
            data: JSON.stringify(data),
            contentType: 'application/json',
        });

        console.log("Check response: " + JSON.stringify(response.data));

        modalAddNewExpense.modal('hide');

        showToast(response.message, 'success');

        await fetchExpenses();
    } catch (error) {
        console.log("Check error:", JSON.stringify(error));
        const msg = error.responseJSON?.message || 'Thêm mới chi tiêu thất bại!';
        showToast(msg, 'danger');
    } finally {
        btn.prop('disabled', false).html('Save');
    }
});

const fetchExpenses = async () => {
    const response = await $.ajax({
        url: '/admin/expense/list-expense',
        method: 'GET',
        contentType: 'application/json',
    });

    const table = $('table');

    const data = response.data || [];

    console.log("Check data: " + JSON.stringify(data));

    table.empty();

    // Tạo header
    const thead = `
            <thead class="thead-light">
                <tr>
                    <th>Ngày</th>
                    <th>Khoản chi</th>
                    <th>Số tiền</th>
                </tr>
            </thead>
        `;

    // Render body
    let tbody = '<tbody>';
    data.forEach((item) => {
        tbody += `
                <tr>
                    <td>${item.amount}</td>
                    <td>${item.expenseName}</td>
                    <td>${item.expenseDate}</td>
                </tr>
            `;
    });
    tbody += '</tbody>';

    table.append(thead + tbody);
}