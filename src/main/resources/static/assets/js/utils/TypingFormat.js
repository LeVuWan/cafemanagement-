$(document).ready(function () {
    $('#phoneNumber').on("input", () => {
        let value = $('#phoneNumber').val();

        value = value.replace(/\D/g, "");

        // Chia nhóm 3-3-3
        if (value.length > 3 && value.length <= 6) {
            value = value.replace(/(\d{3})(\d+)/, "$1 $2");
        } else if (value.length > 6) {
            value = value.replace(/(\d{3})(\d{3})(\d+)/, "$1 $2 $3");
        }

        $('#phoneNumber').val(value);

    });

    $('#money, #customer-paid').on("input", function () {
        let value = $(this).val();

        // Xóa mọi ký tự không phải số
        value = value.replace(/\D/g, "");

        // Giới hạn tối đa 9 chữ số
        if (value.length > 9) {
            value = value.substring(0, 9);
        }

        // Thêm dấu chấm phân tách hàng nghìn
        value = value.replace(/\B(?=(\d{3})+(?!\d))/g, ".");

        $(this).val(value);
    });

    $('#discountValue').on("input", () => {
        let value = $('#discountValue').val();

        value = value.replace(/[^0-9]/g, "");

        if (value.length > 9) {
            value = value.substring(0, 9);
        }

        $('#discountValue').val(value);
    });

    $(document).on('input', '.integer, #integer', function () {
        this.value = this.value.replace(/[^0-9]/g, '');
    });

    $(document).on('input', '.positiveDecimal', function () {
        console.log("Run here");

        let value = this.value;

        // Xóa ký tự không phải số hoặc dấu chấm
        value = value.replace(/[^0-9.]/g, '');

        // Chỉ cho phép 1 dấu chấm
        const parts = value.split('.');
        if (parts.length > 2) {
            value = parts[0] + '.' + parts.slice(1).join('');
        }

        // Không cho phép bắt đầu bằng '.'
        if (value.startsWith('.')) {
            value = '0' + value; // .5 -> 0.5
        }

        this.value = value;
    });

});