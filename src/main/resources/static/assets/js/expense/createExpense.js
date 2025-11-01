$('#comfirmCreateExpense').click(async () => {
    const expenseDate = $('#expenseDate').val();
    const expenseName = $('#expenseName').val();
    const amount = $('#amount').val();

    const data = {
        expenseDate: expenseDate,
        expenseName: expenseName,
        amount: amount
    };

    console.log("Check data: " + JSON.stringify(data));


    try {
        const response = await $.ajax({
            url: '/admin/budget/create-expense',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data)
        });

        showToast('Thêm mới thành công!', 'success');
        $('#createExpense').modal('hide');
        setTimeout(() => location.reload(), 1000);
    } catch (xhr) {
        if (xhr.responseText) {
            showToast(xhr.responseText, 'danger');
        } else {
            showToast('Lỗi không xác định khi thêm mới!', 'danger');
        }
    }
})