const modalPayment = $('#paymentModal');
const form = $('#paymentForm')[0];

// Hàm loại bỏ dấu tiếng Việt
const removeVietnameseTones = (str) => {
    if (!str) return '';
    return str
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '')
        .replace(/đ/g, 'd')
        .replace(/Đ/g, 'D');
};

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
                    <td class="text-right">${item.quantity}</td>
                    <td class="text-right">${item.unitPrice.toLocaleString('vi-VN') + ' đ'}</td>
                    <td class="text-right">${item.totalPrice.toLocaleString('vi-VN') + ' đ'}</td>
                </tr>
            `;
        toltalPrice += item.totalPrice;
        tbody.append(row);
    })
    $('#paymentModal').modal('show');
    $('#totalPrice').text(toltalPrice.toLocaleString('vi-VN') + ' đ');
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
        resetManagementButtons();
    } catch (error) {
        console.log(JSON.stringify(error.responseJSON?.message));

        const msg = error.responseJSON?.message || 'Không thể mở modal payment';
        showToast(msg, 'danger');
    }
})

$('#customer-paid').on('input', function (event) {
    const totalPriceText = $('#totalPrice').text().replace(/[^\d]/g, '');
    const totalPrice = parseInt(totalPriceText) || 0;

    // Giá trị người dùng nhập
    const rawValue = $(this).val().replace(/\D/g, '');  // bỏ mọi ký tự không phải số

    const customerPaid = parseInt(rawValue) || 0;

    let change = customerPaid - totalPrice;
    if (change < 0) change = 0;

    const formattedChange = change.toLocaleString('vi-VN') + ' đ';
    $('#change-amount').text(formattedChange);
});

$('#chooseVoucher').on('change', async () => {
    const discountId = $('#chooseVoucher').val();

    const $totalPrice = $('#totalPrice');
    const $changeAmount = $('#change-amount');
    const $customerPaid = $('#customer-paid');

    const getNumber = (text) => parseInt(String(text).replace(/\D/g, '')) || 0;

    if (!discountId) {
        const original = $totalPrice.data('original') || 0;

        $totalPrice.text(original.toLocaleString('vi-VN') + ' đ');

        const customerPaid = getNumber($customerPaid.val());
        let change = customerPaid - original;
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
            originalPrice = getNumber($totalPrice.text());
            $totalPrice.data('original', originalPrice);
        }

        const discountedPrice = Math.round(originalPrice * (1 - discountPercent / 100));
        $totalPrice.text(discountedPrice.toLocaleString('vi-VN') + ' đ');

        const customerPaid = getNumber($customerPaid.val());
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

    // Lấy dữ liệu từ modal
    const rows = [];
    $("#paymentModal tbody tr").each(function () {
        const name = removeVietnameseTones($(this).find("td:eq(0)").text().trim());
        const quantity = $(this).find("td:eq(1)").text().trim();
        const price = $(this).find("td:eq(2)").text().trim();
        const totalPrice = $(this).find("td:eq(3)").text().trim();

        rows.push([name, quantity, price, totalPrice]);
    });

    // Lấy thông tin giá
    const totalPriceText = $("#totalPrice").text().replace(/[^\d]/g, '');
    const originalTotal = parseInt($('#totalPrice').data('original')) || parseInt(totalPriceText);
    const currentTotal = parseInt(totalPriceText);
    const customerPaid = parseInt($("#customer-paid").val()) || 0;
    const changeAmount = parseInt($("#change-amount").text().replace(/[^\d]/g, '')) || 0;

    // Lấy thông tin voucher
    const voucherSelect = $('#chooseVoucher');
    const selectedVoucherOption = voucherSelect.find(':selected');
    const voucherText = selectedVoucherOption.val() ? removeVietnameseTones(selectedVoucherOption.text()) : 'Khong co voucher';

    // Tính phần trăm giảm giá
    const discountPercent = originalTotal > 0 ? Math.round(((originalTotal - currentTotal) / originalTotal) * 100) : 0;

    // Tiêu đề
    doc.setFontSize(16);
    doc.text(removeVietnameseTones("PHIEU THANH TOAN"), 14, 15);

    // Table — fix alignment and padding for numeric columns
    doc.autoTable({
        head: [[
            "Ten mon",
            { content: "So luong", halign: "right", cellPadding: { left: 1, right: 4 } },
            { content: "Don gia", halign: "right", cellPadding: { left: 1, right: 4 } },
            { content: "Thanh tien", halign: "right", cellPadding: { left: 1, right: 4 }}
        ]],
        body: rows.map(row => [
            row[0],
            { content: row[1], halign: "right" },
            { content: row[2], halign: "right" },
            { content: row[3], halign: "right" }
        ]),
        startY: 25,
        theme: "grid",
        columnStyles: {
            1: { halign: "right", cellPadding: { left: 1, right: 4 } },
            2: { halign: "right", cellPadding: { left: 1, right: 4 } },
            3: { halign: "right", cellPadding: { left: 1, right: 4 } }
        },
        headStyles: {
            halign: "center"
        }
    });

    // Tọa độ sau bảng
    const finalY = doc.lastAutoTable.finalY + 10;

    // Thông tin thanh toán
    doc.setFontSize(11);
    let currentY = finalY;

    // Tổng tiền gốc
    doc.text(removeVietnameseTones(`Tong tien goc: ${originalTotal.toLocaleString('vi-VN')} vnd`), 14, currentY);
    currentY += 7;

    // Voucher
    if (discountPercent > 0) {
        doc.text(removeVietnameseTones(`Voucher: ${voucherText}`), 14, currentY);
        currentY += 7;
    }

    // Tổng tiền sau giảm
    doc.setFont(undefined, "bold");
    doc.text(removeVietnameseTones(`Tong tien: ${currentTotal.toLocaleString('vi-VN')} vnd`), 14, currentY);
    doc.setFont(undefined, "normal");
    currentY += 7;

    // Khách đưa
    doc.text(removeVietnameseTones(`Khach dua: ${customerPaid.toLocaleString('vi-VN')} vnd`), 14, currentY);
    currentY += 7;

    // Thối lại
    doc.text(removeVietnameseTones(`Thoi lai: ${changeAmount.toLocaleString('vi-VN')} vnd`), 14, currentY);

    doc.save("phieu-thanh-toan.pdf");
});

const resetPaymentModal = () => {
    const modal = $('#paymentModal');

    modal.find('form')[0].reset();

    modal.find('.form-message').text('');

    modal.find('.invalid').removeClass('invalid');

    modal.find('#change-amount').text('0 đ');

    modal.find('#totalPrice').text('').removeData('original');

    modal.find('#chooseVoucher').prop('selectedIndex', 0);

    $('#paymentModal tbody').empty();
}
