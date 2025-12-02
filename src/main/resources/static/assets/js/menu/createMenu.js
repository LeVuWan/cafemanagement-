$('#saveButton').click(async function () {
    const name = $('#name').val().trim();
    let price = $('#money').val().trim();

    if (!name) {
        showToast("Vui lòng nhập tên menu!", "warning");
        return;
    }

    price = parseFloat(price.replace(/[.,]/g, ''));

    if (isNaN(price)) {
        showToast("Giá không được để trống!", "warning");
        return;
    }

    if (price <= 0) {
        showToast("Vui lòng nhập giá hợp lệ!", "warning");
        return;
    }

    const menu = {
        name: name,
        price: price,
        ingredients: []
    };

    // Lấy dữ liệu từ table
    $('#ingredientTable tr').each(function () {
        const $tr = $(this);

        const quantity = parseFloat($tr.find("td:nth-child(2)").text().trim()) || 0;
        const productId = $tr.find("td:nth-child(4)").text().trim() || null;
        const unitId = $tr.find("td:nth-child(5)").text().trim() || null;

        if (quantity > 0 && productId && unitId) {
            menu.ingredients.push({
                quantity: quantity,
                productId: productId,
                unitId: unitId
            });
        }
    });

    if (menu.ingredients.length === 0) {
        showToast("Vui lòng thêm ít nhất một nguyên liệu!", "warning");
        return;
    }

    try {
        const res = await $.ajax({
            url: '/admin/menu/create',
            method: 'POST',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(menu)
        });

        window.location.href = '/admin/menu';
    } catch (error) {
        console.error(error);
        showToast("Tạo menu thất bại, vui lòng thử lại.", "error");
    }
});
