// Helper function to format currency
const formatCurrency = (amount) => {
    return amount.toLocaleString('vi-VN', {
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    }) + ' ₫';
};

// Helper function to format date
const formatDate = (dateString) => {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
};

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
            showHideDateDiv("show");
            if (checkDateRange(from, to) === false) return;
            await reportAll(from, to);
            break;
        case "input-output":
            showHideDateDiv("show");
            if (checkDateRange(from, to) === false) return;
            await reportInputOutput(from, to);
            break;
        case "input":
            showHideDateDiv("show");
            if (checkDateRange(from, to) === false) return;
            await reportInput(from, to);
            break;
        case "export":
            showHideDateDiv("show");
            if (checkDateRange(from, to) === false) return;
            await reportExport(from, to);
            break;
        case "sell":
            showHideDateDiv("show");
            if (checkDateRange(from, to) === false) return;
            await reportSell(from, to);
            break;
        case "employee-info":
            showHideDateDiv("hide");
            await reportEmployeeInformation(from, to);
            break;
        case "expense":
            showHideDateDiv("hide");
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
                    <td>${formatDate(r.date)}</td>
                    <td class="text-right">${formatCurrency(r.income)}</td>
                    <td class="text-right">${formatCurrency(r.expense)}</td>
                </tr>
            `;
        });

        tbody += `
            <tr class="fw-bold text-end">
                <td>Tổng cộng</td>
                <td class="text-right">${formatCurrency(totalIncomeAll)}</td>
                <td class="text-right">${formatCurrency(totalExpenseAll)}</td>
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
                    <td>${formatDate(r.date)}</td>
                    <td class="text-right">${formatCurrency(r.importAmount)}</td>
                    <td class="text-right">${formatCurrency(r.exportAmount)}</td>
                </tr>
            `;
        });

        tbody += `
            <tr class="fw-bold text-end">
                <td>Tổng cộng</td>
                <td class="text-right">${formatCurrency(totalImportAmount)}</td>
                <td class="text-right">${formatCurrency(totalExportAmount)}</td>
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
                    <td>${formatDate(r.importDate)}</td>
                    <td class="text-right">${formatCurrency(amount)}</td>
                </tr>
            `;
        });

        tbody += `
            <tr class="fw-bold text-end">
                <td>Tổng cộng</td>
                <td class="text-right">${formatCurrency(totalImport)}</td>
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
                    <td>${formatDate(r.importDate)}</td>
                    <td class="text-right">${formatCurrency(amount)}</td>
                </tr>
            `;
        });

        tbody += `
            <tr class="fw-bold text-end">
                <td>Tổng cộng</td>
                <td class="text-right">${formatCurrency(totalExport)}</td>
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

        console.log("Check data: ", data);


        // Xóa nội dung cũ
        table.empty();

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
                    <td>${formatDate(item.importDate)}</td>
                    <td class="text-right">${formatCurrency(item.totalAmount)}</td>
                </tr>
            `;
        });

        // Tính tổng thu
        const totalIncome = data.reduce((sum, item) => sum + (item.totalIncome || 0), 0);
        tbody += `
            <tr class="font-weight-bold bg-light">
                <td>Tổng</td>
                <td class="text-right">${formatCurrency(totalIncome)}</td>
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
                    <td class="text-right">${formatCurrency(emp.salary)}</td>
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
                    <td>${formatDate(item.importDate)}</td>
                    <td class="text-right">${formatCurrency(item.totalAmount)}</td>
                </tr>
            `;
        });

        // Tính tổng thu
        const totalIncome = data.reduce((sum, item) => sum + (item.totalAmount), 0);
        tbody += `
            <tr class="font-weight-bold bg-light">
                <td>Tổng</td>
                <td class="text-right">${formatCurrency(totalIncome)}</td>
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


const showHideDateDiv = (action) => {
    if (action === "show") {
        $("#date").show();
    } else if (action === "hide") {
        $("#date").hide();
    }
}

$(document).ready(function () {
    $("input[name='category']").change(function () {
        if ($(this).val() === "employee-info") {
            showHideDateDiv("hide");
        } else {
            showHideDateDiv("show");
        }
    });
});