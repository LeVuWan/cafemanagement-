// Validation functions
function validateName() {
    const name = $('#name').val().trim();
    const $errorSpan = $('#name').next('.form-message');

    if (!name) {
        $errorSpan.text('Trường này không được trống!');
        return false;
    } else {
        $errorSpan.text('');
        return true;
    }
}

function validatePrice() {
    const price = $('#money').val().trim();
    const $errorSpan = $('#money').next('.form-message');

    if (!price) {
        $errorSpan.text('Trường này không được trống!');
        return false;
    }

    const parsedPrice = parseFloat(price.replace(/[.,]/g, ''));

    if (isNaN(parsedPrice)) {
        $errorSpan.text('Giá không hợp lệ!');
        return false;
    }

    if (parsedPrice <= 0) {
        $errorSpan.text('Vui lòng nhập giá hợp lệ!');
        return false;
    }

    $errorSpan.text('');
    return true;
}

// Blur event - Show errors
$('#name').on('blur', function () {
    validateName();
});

$('#money').on('blur', function () {
    validatePrice();
});

// Input event - Clear errors when user starts typing
$('#name').on('input', function () {
    const $errorSpan = $(this).next('.form-message');
    $errorSpan.text('');
});

$('#money').on('input', function () {
    const $errorSpan = $(this).next('.form-message');
    $errorSpan.text('');
});

// Save button click
$('#saveButton').click(async function () {
    const isNameValid = validateName();
    const isPriceValid = validatePrice();

    if (!isNameValid || !isPriceValid) {
        return;
    }

    const name = $('#name').val().trim();
    let price = parseFloat($('#money').val().trim().replace(/[.,]/g, ''));

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
        console.log("Run here");

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
