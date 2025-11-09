$('#send-report').click(async () => {
    const category = $('input[name="category"]:checked').val();
    const from = $('#date-from').val();
    const to = $('#date-to').val();

    console.log("Check from: " + from);
    console.log("Check to: " + to);

    chooseReport(category, from, to);
});

const chooseReport = async (category, from, to) => {
    switch (category) {
        case "general":
            if (checkDateRange(from, to) === false) return;
            await reportAll(from, to);
            break;
        case "input-output":
            if (checkDateRange(from, to) === false) return;
            await reportInputOutput(from, to);
            break;
        case "input":
            if (checkDateRange(from, to) === false) return;
            await reportInput(from, to);
            break;
        case "export":
            if (checkDateRange(from, to) === false) return;
            await reportExport(from, to);
            break;
        case "sell":
            if (checkDateRange(from, to) === false) return;
            await reportSell(from, to);
            break;
        case "employee-info":
            await reportEmployeeInformation(from, to);
            break;
        case "expense":
            if (checkDateRange(from, to) === false) return;
            await expenseReport(from, to);
            break;
        default:
            showToast(category + " Không hợp lệ", 'danger');
            break;
    }
}

const reportAll = async (from, to) => {
    const btn = $('#send-report');
    const table = $('table');

    btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Đang xử lý...');

    try {
        const response = await $.ajax({
            url: `/admin/report/general?from=${from}&to=${to}`,
            method: 'GET',
        });

        const data = response.data || [];

        table.empty();

        let thead = `
            <thead>
                <tr>
                    <th>Ngày</th>
                    <th>Thu</th>
                    <th>Chi</th>
                </tr>
            </thead>
        `;

        // Tạo body
        let tbody = '<tbody>';
        let totalIncomeAll = 0;
        let totalExpenseAll = 0;

        data.forEach(r => {
            totalIncomeAll += r.income;
            totalExpenseAll += r.expense;
            tbody += `
                <tr>
                    <td>${r.date}</td>
                    <td>${r.income.toLocaleString('vi-VN')}</td>
                    <td>${r.expense.toLocaleString('vi-VN')}</td>
                </tr>
            `;
        });

        tbody += `
            <tr class="fw-bold text-end">
                <td>Tổng cộng</td>
                <td>${totalIncomeAll.toLocaleString('vi-VN')}</td>
                <td>${totalExpenseAll.toLocaleString('vi-VN')}</td>
            </tr>
        `;
        tbody += '</tbody>';

        table.append(thead + tbody);

        showToast(response.message, 'success');
    } catch (error) {
        console.log("Check error:", error);
        const msg = error.responseJSON?.message || 'Load report thất bại!';
        showToast(msg, 'danger');
    } finally {
        btn.prop('disabled', false).text('Xem');
    }
};

const reportInputOutput = async (from, to) => {
    const btn = $('#send-report');
    const table = $('table');

    btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Đang xử lý...');

    try {
        const response = await $.ajax({
            url: `/admin/report/input-output?from=${from}&to=${to}`,
            method: 'GET',
        });

        const data = response.data || [];

        console.log("Check data:", data);

        table.empty();

        const thead = `
            <thead>
                <tr>
                    <th>Ngày</th>
                    <th>Tổng tiền nhập</th>
                    <th>Tổng tiền xuất</th>
                </tr>
            </thead>
        `;

        let tbody = '<tbody>';
        let totalImportAmount = 0;
        let totalExportAmount = 0;

        data.forEach(r => {
            totalImportAmount += r.importAmount || 0;
            totalExportAmount += r.exportAmount || 0;

            tbody += `
                <tr>
                    <td>${r.date}</td>
                    <td>${r.importAmount.toLocaleString('vi-VN')}</td>
                    <td>${r.exportAmount.toLocaleString('vi-VN')}</td>
                </tr>
            `;
        });

        tbody += `
            <tr class="fw-bold text-end">
                <td>Tổng cộng</td>
                <td>${totalImportAmount.toLocaleString('vi-VN')}</td>
                <td>${totalExportAmount.toLocaleString('vi-VN')}</td>
            </tr>
        `;
        tbody += '</tbody>';

        table.append(thead + tbody);

        showToast(response.message || 'Tải báo cáo thành công!', 'success');
    } catch (error) {
        console.log("Check error:", error);
        const msg = error.responseJSON?.message || 'Load report thất bại!';
        showToast(msg, 'danger');
    } finally {
        btn.prop('disabled', false).text('Xem');
    }
};

const reportInput = async (from, to) => {
    const btn = $('#send-report');
    const table = $('table');

    btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Đang xử lý...');

    try {
        const response = await $.ajax({
            url: `/admin/report/input?from=${from}&to=${to}`,
            method: 'GET',
        });

        const data = response.data || [];

        console.log("Check data:", data);

        table.empty();

        const thead = `
            <thead>
                <tr>
                    <th>Ngày</th>
                    <th>Tổng tiền nhập</th>
                </tr>
            </thead>
        `;

        let tbody = '<tbody>';
        let totalImport = 0;

        data.forEach(r => {
            const amount = r.totalAmount || 0;
            totalImport += amount;

            tbody += `
                <tr>
                    <td>${r.importDate}</td>
                    <td>${amount.toLocaleString('vi-VN')}</td>
                </tr>
            `;
        });

        tbody += `
            <tr class="fw-bold text-end">
                <td>Tổng cộng</td>
                <td>${totalImport.toLocaleString('vi-VN')}</td>
            </tr>
        `;

        tbody += '</tbody>';

        table.append(thead + tbody);
        showToast(response.message || 'Tải báo cáo thành công!', 'success');
    } catch (error) {
        console.log("Check error:", error);
        const msg = error.responseJSON?.message || 'Load report thất bại!';
        showToast(msg, 'danger');
    } finally {
        btn.prop('disabled', false).text('Xem');
    }
};

const reportExport = async (from, to) => {
    const btn = $('#send-report');
    const table = $('table');

    btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Đang xử lý...');

    try {
        const response = await $.ajax({
            url: `/admin/report/export?from=${from}&to=${to}`,
            method: 'GET',
        });

        const data = response.data || [];

        console.log("Check data: ", JSON.stringify(data));

        table.empty();

        const thead = `
            <thead>
                <tr>
                    <th>Ngày</th>
                    <th>Tổng tiền xuất</th>
                </tr>
            </thead>
        `;

        let tbody = '<tbody>';
        let totalExport = 0;

        data.forEach(r => {
            const amount = r.totalAmount || r.exportAmount || 0;
            totalExport += amount;

            tbody += `
                <tr>
                    <td>${r.importDate}</td>
                    <td>${amount.toLocaleString('vi-VN')}</td>
                </tr>
            `;
        });

        tbody += `
            <tr class="fw-bold text-end">
                <td>Tổng cộng</td>
                <td>${totalExport.toLocaleString('vi-VN')}</td>
            </tr>
        `;

        tbody += '</tbody>';

        table.append(thead + tbody);
        showToast(response.message || 'Tải báo cáo thành công!', 'success');
    } catch (error) {
        console.log("Check error:", error);
        const msg = error.responseJSON?.message || 'Load report thất bại!';
        showToast(msg, 'danger');
    } finally {
        btn.prop('disabled', false).text('Xem');
    }
};

const reportSell = async (from, to) => {
    const btn = $('#send-report');
    const table = $('table');

    btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Đang xử lý...');

    try {
        const response = await $.ajax({
            url: `/admin/report/sell?from=${from}&to=${to}`,
            method: 'GET',
        });

        const data = response.data || [];

        // Xóa nội dung cũ
        table.empty();

        console.log("Check data: " + JSON.stringify(data));


        // Render header
        let thead = `
            <thead class="thead-light">
                <tr>
                    <th>Ngày</th>
                    <th>Tổng thu</th>
                </tr>
            </thead>
        `;

        // Render body
        let tbody = '<tbody>';
        data.forEach(item => {
            tbody += `
                <tr>
                    <td>${item.date}</td>
                    <td>${item.totalIncome.toLocaleString('vi-VN')}₫</td>
                </tr>
            `;
        });

        // Tính tổng thu
        const totalIncome = data.reduce((sum, item) => sum + (item.totalIncome || 0), 0);
        tbody += `
            <tr class="font-weight-bold bg-light">
                <td>Tổng</td>
                <td>${totalIncome.toLocaleString('vi-VN')}₫</td>
            </tr>
        `;
        tbody += '</tbody>';

        // Gắn lại vào bảng
        table.append(thead + tbody);

    } catch (error) {
        console.log("Check error:", error);
        const msg = error.responseJSON?.message || 'Load report thất bại!';
        showToast(msg, 'danger');
    } finally {
        btn.prop('disabled', false).text('Xem');
    }
};

const reportEmployeeInformation = async () => {
    const btn = $('#send-report');
    const table = $('table');
    btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Đang xử lý...');

    try {
        const response = await $.ajax({
            url: '/admin/report/information-employy',
            method: 'GET',
        });

        const data = response.data || [];
        console.log("Check data:", data);

        // Xóa nội dung cũ
        table.empty();

        // Tạo header
        const thead = `
            <thead class="thead-light">
                <tr>
                    <th>STT</th>
                    <th>Họ tên</th>
                    <th>Số điện thoại</th>
                    <th>Địa chỉ</th>
                    <th>Lương</th>
                    <th>Quyền</th>
                </tr>
            </thead>
        `;

        // Render body
        let tbody = '<tbody>';
        data.forEach((emp, index) => {
            tbody += `
                <tr>
                    <td>${index + 1}</td>
                    <td>${emp.fullname}</td>
                    <td>${emp.phoneNumber}</td>
                    <td>${emp.address}</td>
                    <td>${emp.salary.toLocaleString('vi-VN')}₫</td>
                    <td>${emp.permissionName || 'Không có'}</td>
                </tr>
            `;
        });
        tbody += '</tbody>';

        table.append(thead + tbody);

    } catch (error) {
        console.log("Check error:", error);
        const msg = error.responseJSON?.message || 'Load report thất bại!';
        showToast(msg, 'danger');
    } finally {
        btn.prop('disabled', false).text('Xem');
    }
};

const expenseReport = async (from, to) => {
    const btn = $('#send-report');
    const table = $('table');
    btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Đang xử lý...');
    try {
        const response = await $.ajax({
            url: `/admin/report/expense?from=${from}&to=${to}`,
            method: 'GET',
        });

        const data = response.data || [];

        console.log("Check data: " + JSON.stringify(data));        

        // Xóa nội dung cũ
        table.empty();

        // Render header
        let thead = `
            <thead class="thead-light">
                <tr>
                    <th>Ngày</th>
                    <th>Tổng chi</th>
                </tr>
            </thead>
        `;

        // Render body
        let tbody = '<tbody>';
        data.forEach(item => {
            tbody += `
                <tr>
                    <td>${item.importDate}</td>
                    <td>${item.totalAmount.toLocaleString('vi-VN')}₫</td>
                </tr>
            `;
        });

        // Tính tổng thu
        const totalIncome = data.reduce((sum, item) => sum + (item.totalAmount), 0);
        tbody += `
            <tr class="font-weight-bold bg-light">
                <td>Tổng</td>
                <td>${totalIncome.toLocaleString('vi-VN')}₫</td>
            </tr>
        `;
        tbody += '</tbody>';

        // Gắn lại vào bảng
        table.append(thead + tbody);
    } catch (error) {
        console.log("Check error:", error);
        const msg = error.responseJSON?.message || 'Load report thất bại!';
        showToast(msg, 'danger');
    } finally {
        btn.prop('disabled', false).text('Xem');
    }
}

const checkDateRange = (from, to) => {
    if (!from || !to) {
        showToast('Vui lòng chọn đầy đủ khoảng thời gian', 'warning');
        return false;
    }

    if (new Date(from) > new Date(to)) {
        showToast('Ngày bắt đầu phải trước ngày kết thúc', 'warning');
        return false;
    }
}