const modalPayment = $('#paymentModal');

const getPaymentInfo = async (tableId) => {
    const response = await $.ajax({
        url: '/admin/table/get-information-table/' + tableId,
        method: 'GET',
        contentType: 'application/json'
    });

    const tbody = $('#paymentModal tbody');
    tbody.empty();
    const inforTable = response.data;

    let toltalPrice = 0;

    inforTable.infoMenuRes.forEach(item => {
        const row = `
                <tr>
                    <td class="text-center">${item.dishName}</td>
                    <td class="text-center">${item.quantity}</td>
                    <td class="text-right">${item.totalPrice}</td>
                </tr>
            `;
        toltalPrice += item.totalPrice;
        tbody.append(row);
    })
    $('#paymentModal').modal('show');
    $('#totalPrice').text(toltalPrice);
}

const getVoucher = async () => {
    const response = await $.ajax({
        url: '/admin/voucher/get-voucher-by-date',
        method: 'GET',
        contentType: 'application/json'
    });

    const vouchers = response.data;

    const $select = $('#chooseVoucher');

    $select.empty();
    $select.append('<option selected value="">Chọn voucher</option>');

    vouchers.forEach(voucher => {
        const optionText = `${voucher.voucherName} - Giảm ${voucher.discountValue}%`;
        const option = `<option value="${voucher.voucherId}">${optionText}</option>`;
        $select.append(option);
    });
};

$('#btn-payment').click(async () => {
    const table = JSON.parse(sessionStorage.getItem('selectedTable'));
    $('#paymentModalLabel').text('Thanh toán bàn ' + table.name);
    try {
        await Promise.all([getPaymentInfo(table.id), getVoucher()]);
    } catch (error) {
        console.log(JSON.stringify(error.responseJSON?.message));

        const msg = error.responseJSON?.message || 'Không thể mở modal payment';
        showToast(msg, 'danger');
    }
})

$('#customer-paid').on('input', function (event) {
    const totalPriceText = $('#totalPrice').text().replace(/[^\d]/g, '');
    const totalPrice = parseInt(totalPriceText) || 0;

    const customerPaid = parseInt($(this).val()) || 0;

    let change = customerPaid - totalPrice;
    if (change < 0) change = 0;

    const formattedChange = change.toLocaleString('vi-VN') + ' đ';
    $('#change-amount').text(formattedChange);
});

$('#chooseVoucher').on('change', async () => {
    const discountId = $('#chooseVoucher').find(':selected').val();
    const $totalPrice = $('#totalPrice');
    const $changeAmount = $('#change-amount');
    const $customerPaid = $('#customer-paid');

    try {
        const response = await $.ajax({
            url: '/admin/voucher/get-voucher-by-id/' + discountId,
            method: 'GET',
            contentType: 'application/json'
        });

        const voucher = response.data;

        const discountPercent = voucher.discountValue || 0;

        let originalPrice = $totalPrice.data('original');

        if (!originalPrice) {
            originalPrice = parseInt($totalPrice.text().replace(/[^\d]/g, '')) || 0;
            $totalPrice.data('original', originalPrice);
        }

        const discountedPrice = Math.round(originalPrice * (1 - discountPercent / 100));

        $totalPrice.text(discountedPrice.toLocaleString('vi-VN') + ' đ');

        const customerPaid = parseInt($customerPaid.val()) || 0;
        let change = customerPaid - discountedPrice;
        if (change < 0) change = 0;
        $changeAmount.text(change.toLocaleString('vi-VN') + ' đ');
    } catch (error) {
        console.error('Lỗi khi load voucher:', error.responseJSON?.message || error);

        const msg = error.responseJSON?.message || 'Lỗi khi load voucher';
        showToast(msg, 'danger');
    }
});

$('#comfirmPayment').click(async () => {
    const voucherId = $('#chooseVoucher').find(':selected').val();
    console.log("Check voucherId: " + voucherId);

    const button = $('#comfirmPayment');
    const tableId = JSON.parse(sessionStorage.getItem('selectedTable')).id;

    const data = {
        voucherId, tableId
    };

    try {
        const response = await $.ajax({
            url: '/admin/table/payment',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data)
        });
        modalPayment.modal('hide');
        showToast(response.message || 'Thanh toán thành công!', 'success');
        sessionStorage.removeItem('selectedTable');
        setTimeout(() => location.reload(), 1000);
    } catch (error) {
        const msg = error.responseJSON?.message || 'Thanh toán thất bại!';

        showToast(msg, 'danger');
    } finally {
        button.prop('disabled', false).text('Thanh toán');
    }
})


