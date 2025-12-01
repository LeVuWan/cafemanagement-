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

    $('#salary').on("input", () => {
        let value = $('#salary').val();

        value = value.replace(/\D/g, "");

        if (value.length > 9) {
            value = value.substring(0, 9);
        }

        value = value.replace(/\B(?=(\d{3})+(?!\d))/g, ".");

        $('#salary').val(value);
    });
});