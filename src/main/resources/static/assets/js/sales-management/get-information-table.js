const modalInfomationTable = $('#infomationTableModal');

$('#btn-infor-table').click(async () => {
    const table = JSON.parse(sessionStorage.getItem('selectedTable'));
    modalInfomationTable.modal('show');
    $("#infomationTableModalLabel").text(`Thông tin bàn ${table.name}`);

    try {
        const response = await $.ajax({
            url: '/admin/table/get-information-table/' + table.id,
            method: 'GET',
            contentType: 'application/json'
        });

        const tbody = $('#infomationTableModal tbody');
        tbody.empty();

        const inforTable = response.data;

        const customerName = inforTable.nameCustomer ? inforTable.nameCustomer : 'Khách vảng lai';

        $('#customerNameModalInforTable').text(customerName);

        const orderTime = inforTable.orderTime.substring(0, 5);
        const orderDate = new Date(inforTable.orderDate);

        const formattedDate = `${orderDate.getDate()}/${orderDate.getMonth() + 1}/${orderDate.getFullYear()}`;

        const dateTimeOrder = `${orderTime} ${formattedDate}`;
        $('#dateTimeOrder').text(dateTimeOrder);

        inforTable.infoMenuRes.forEach(item => {

            const row = `
                <tr>
                    <td class="text-center">${item.dishName}</td>
                    <td class="text-right">${item.quantity}</td>
                </tr>
            `;
            tbody.append(row);
        });
        resetManagementButtons();
    } catch (xhr) {
        console.log("check: " + xhr.responseJSON.message);

        const msg = xhr.responseJSON?.message || 'Không thể tải danh sách thực đơn!';
        showToast(msg, 'danger');
    }
})

