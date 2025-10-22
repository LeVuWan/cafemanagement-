$('#saveButton').click(function () {
    const menu = {
        name: $('#name').val(),
        price: $('#price').val(),
        ingredients: []
    };

    $('#ingredientTable tr').each(function () {
        const $tr = $(this);
        menu.ingredients.push({
            quantity: $tr.find("td:nth-child(2)").text().trim(),
            productId: $tr.find("td:nth-child(4)").text().trim(),
            unitId: $tr.find("td:nth-child(5)").text().trim()
        });
    });

    $.ajax({
        url: '/admin/menu/create',
        method: 'POST',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(menu),
        success: function () {
            window.location.href = '/admin/menu'; // redirect sau khi lưu
        },
        error: function () {
            alert('Đã xảy ra lỗi khi lưu menu.');
        }
    });
});
