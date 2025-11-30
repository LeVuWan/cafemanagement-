const modalPayment = $('#paymentModal');
const form = $('#paymentForm')[0];

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
        toltalPrice += item.totalPrice * item.quantity;
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
    resetPaymentModal();
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

    if (!discountId) {
        $totalPrice.text($totalPrice.data('original') || '0');

        const customerPaid = parseInt($customerPaid.val()) || 0;
        const total = parseInt($totalPrice.data('original')) || 0;
        let change = customerPaid - total;
        if (change < 0) change = 0;
        $changeAmount.text(change.toLocaleString('vi-VN') + ' đ');

        return;
    }

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


    if (!form.validateForm()) {
        btn.prop('disabled', false).text('Xác nhận đặt bàn');
        return;
    }

    const voucherId = $('#chooseVoucher').find(':selected').val();

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
        await getListTable();
    } catch (error) {
        const msg = error.responseJSON?.message || 'Thanh toán thất bại!';

        showToast(msg, 'danger');
    } finally {
        button.prop('disabled', false).text('Thanh toán');
    }
})

$("#printBillBtn").click(async () => {
    if (!form.validateForm()) {
        return;
    }

    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    const rows = [];
    $("#paymentModal tbody tr").each(function () {
        const name = removeVietnameseTones($(this).find("td:eq(0)").text().trim());
        const quantity = $(this).find("td:eq(1)").text().trim();
        const price = $(this).find("td:eq(2)").text().trim();
        rows.push([name, quantity, price]);
    });

    const total = removeVietnameseTones($("#totalPrice").text().trim());
    const customerPaid = $("#customer-paid").val() || "0";
    const changeAmount = removeVietnameseTones($("#change-amount").text().trim());

    doc.setFontSize(16);
    doc.text(removeVietnameseTones("PHIẾU THANH TOÁN"), 14, 15);

    doc.autoTable({
        head: [[
            "Ten mon",
            "So luong",
            "Thanh tien"
        ]],
        body: rows,
        startY: 25,
        theme: "grid",
    });

    // TỌA ĐỘ SAU BẢNG
    const finalY = doc.lastAutoTable.finalY + 10;

    doc.setFontSize(12);
    doc.text(removeVietnameseTones(`Tổng tiền: ${total} vnd`), 14, finalY);
    doc.text(removeVietnameseTones(`Khách đưa: ${parseInt(customerPaid).toLocaleString()} vnd`), 14, finalY + 7);
    doc.text(removeVietnameseTones(`Thối lại: ${changeAmount} vnd`), 14, finalY + 14);

    doc.save("phieu-thanh-toan.pdf");
});

const resetPaymentModal = () => {
    const modal = $('#paymentModal');

    modal.find('form')[0].reset();

    modal.find('.form-message').text('');

    modal.find('.invalid').removeClass('invalid');

    modal.find('#change-amount').text('0 đ');

    modal.find('#totalPrice').text('');

    modal.find('#chooseVoucher').prop('selectedIndex', 0);
}
