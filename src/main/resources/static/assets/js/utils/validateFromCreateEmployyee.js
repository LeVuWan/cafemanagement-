document.getElementById("comfirmCreateEmployyee").addEventListener("click", function (event) {
    // alert("Click me");
    event.preventDefault();

    const fullname = $("#fullname");
    const permissionId = $("#permissionId");
    const salary = $("#salary");
    const phone = $("#phone");
    const username = $("#username");
    const password = $("#password");

    const fullnameError = $("#fullnameError");
    const permissionIdError = $("#permissionIdError");
    const salaryError = $("#salaryError");
    const phoneError = $("#phoneError");
    const usernameError = $("#usernameError");
    const passwordError = $("#passwordError");

    let isValid = true;

    $("span.text-danger").text("");

    if (fullname.val().trim().length < 3) {
        fullnameError.text("Họ tên phải ít nhất 3 ký tự");
        isValid = false;
    }

    if (permissionId.val() === "") {
        permissionIdError.text("Vui lòng chọn chức vụ");
        isValid = false;
    }

    if (salary.val() === "" || parseInt(salary.val()) < 0 || parseInt(salary.val()) > 30000000) {
        salaryError.text("Lương không hợp lệ (0 - 30,000,000)");
        isValid = false;
    }

    const phoneRegex = /^(0|\+84)[0-9]{8,10}$/;
    if (!phoneRegex.test(phone.val())) {
        phoneError.text("Số điện thoại không hợp lệ");
        isValid = false;
    }

    if (username.val().trim().length < 3) {
        usernameError.text("Tên đăng nhập phải ít nhất 3 ký tự");
        isValid = false;
    }

    if (password.val().trim().length < 3) {
        passwordError.text("Mật khẩu phải ít nhất 3 ký tự");
        isValid = false;
    }

    if (isValid) {
        $("form").submit();
    }
});
