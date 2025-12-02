$('#comfirmAddProduct').click(function () {
    let isChooseProduct;
    let listProduct = [];
    let hasEmptyQuantity = false;

    $('#addProductModal tbody tr').each(function () {
        isChooseProduct = $(this).find('.isChooseProduct').is(':checked');
        let quantity = $(this).find('.quantity').val();
        let productName = $(this).find('.productName').text().trim();
        let unitName = $(this).find('.unitName').text().trim();
        let productId = $(this).find('.productId').text().trim();
        let unitId = $(this).find('.unitId').text().trim();

        if (isChooseProduct) {
            if (quantity > 0) {
                listProduct.push({
                    productId: productId,
                    productName: productName,
                    quantity: quantity,
                    unitId: unitId,
                    unitName: unitName,
                })
            } else {
                showToast(`Vui lòng nhập số lượng của ${productName}`, "warning");
                hasEmptyQuantity = true;
                return false;
            }
        }
    })

    if (hasEmptyQuantity) {
        return;
    }

    if (listProduct.length < 1) {
        showToast(`Vui lòng chọn thành phần cho menu`, "warning");
        return;
    }

    resetAddProductModalTable();
    populateIngredientTableWithMerge(listProduct);
    $('#addProductModal').modal('hide');
    return;
})

function resetAddProductModalTable() {
    let table = $('#addProductModal tbody');

    table.find('input[type="number"]').val('');

    table.find('input[type="checkbox"]').prop('checked', false);
}

function populateIngredientTableWithMerge(products) {
    let tbody = $('#ingredientTable');

    products.forEach((item) => {
        let existingRow = tbody.find('tr').filter(function () {
            return $(this).find('td:first').text().trim() === item.productName;
        });

        if (existingRow.length > 0) {
            let qtyTd = existingRow.find('td:nth-child(2)');
            let currentQty = parseFloat(qtyTd.text()) || 0;
            let newQty = currentQty + parseFloat(item.quantity);
            qtyTd.text(newQty);
        } else {
            let tr = $('<tr></tr>');
            tr.append(`<td class="text-center">${item.productName}</td>`);
            tr.append(`<td class="text-center">${item.quantity}</td>`);
            tr.append(`<td class="text-center">${item.unitName}</td>`);
            tr.append(`<td style="display:none;">${item.productId}</td>`);
            tr.append(`<td style="display:none;">${item.unitId}</td>`);
            tr.append(`
                <td class="text-center">
                    <button type="button" class="btn btn-danger btn-sm remove-row">Xóa</button>
                </td>
            `);

            tbody.append(tr);

            tr.find('.remove-row').click(function () {
                $(this).closest('tr').remove();
            });
        }
    });
}
