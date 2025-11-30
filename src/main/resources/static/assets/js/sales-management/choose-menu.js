
const modalChooseMenu = $('#chooseMenuModal');
$('#btn-choose-menu').click(async () => {
    const table = JSON.parse(sessionStorage.getItem('selectedTable'));
    modalChooseMenu.modal('show'); // hiển thị modal

    try {
        const response = await $.ajax({
            url: '/admin/table/get-menu',
            method: 'GET',
            contentType: 'application/json'
        });

        const menuList = response.data;

        const tbody = $('#chooseMenuModal tbody');

        tbody.empty();

        menuList.forEach(item => {

            const row = `
                <tr>
                    <td class="menuName">${item.dishName}</td>
                    <td>
                     <input type="number" class="form-control quantity-input" data-id="${item.menuId}"> 
                    </td>
                    <td class="text-center">
                        <input type="checkbox" class="form-check-input menu-checkbox" data-id="${item.menuId}">
                    </td>
                </tr>
            `;
            tbody.append(row);
        });

    } catch (xhr) {
        console.log("check: " + xhr.responseJSON.message);

        const msg = xhr.responseJSON?.message || 'Không thể tải danh sách thực đơn!';
        showToast(msg, 'danger');
    }
});

$('#btn-confirm-choose').click(async () => {
    const btn = $('#btn-confirm-choose');
    const rows = [];
    const table = JSON.parse(sessionStorage.getItem('selectedTable') || '{}');
    let hasError = false;

    btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Đang xử lý...');

    $('#chooseMenuModal tbody tr').each(function () {
        const checkbox = $(this).find(".menu-checkbox");

        if (checkbox.is(":checked")) {
            const quantity = $(this).find(".quantity-input").val();
            const menuId = checkbox.data("id");
            const menuName = $(this).find(".menuName").text().trim();

            if (!quantity || quantity <= 0) {
                showToast("Vui lòng nhập số lượng của " + menuName, "warning",);
                hasError = true;
                return false;
            }

            rows.push({
                menuId,
                menuName,
                quantity
            });
        }
    });

    if (hasError) {
        btn.prop('disabled', false).text('Xác nhận');
        return;
    }

    if (rows.length === 0) {
        showToast('Vui lòng chọn ít nhất 1 món!', 'warning');
        btn.prop('disabled', false).text('Xác nhận');
        return;
    }

    const data = {
        tableId: table.id,
        menu: rows
    };

    try {
        const response = await $.ajax({
            url: '/admin/table/choose-menu',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data)
        });

        showToast(response.message || 'Thêm món thành công!', 'success');
        modalChooseMenu.modal('hide');
        sessionStorage.removeItem('selectedTable');
        await getListTable();
    } catch (xhr) {
        const msg = xhr.responseJSON?.message || 'Thêm món thất bại!';
        showToast(msg, 'danger');
    } finally {
        btn.prop('disabled', false).text('Xác nhận');
    }
});