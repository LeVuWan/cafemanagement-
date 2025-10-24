
$('#btn-choose-menu').click(async () => {
    const table = JSON.parse(sessionStorage.getItem('selectedTable'));
    $('#chooseMenuModal').modal('show'); // hiển thị modal

    try {
        const response = await $.ajax({
            url: '/admin/table/get-menu',
            method: 'GET',
            contentType: 'application/json'
        });

        const menuList = response.data;

        const tbody = $('#chooseMenuModal tbody');

        menuList.forEach(item => {

            const row = `
                <tr>
                    <td>${item.dishName}</td>
                    <td><input type="number" class="form-control quantity-input" data-id="${item.menuId}"></td>
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
