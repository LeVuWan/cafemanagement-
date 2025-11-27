const Validator = (options) => {
    const formElement = document.querySelector(options.form);
    var selectorRules = {};

    // Hàm validate chung
    function validate(inputElement, rule) {
        var errorMessage;
        var errorElement = inputElement.parentElement.querySelector(options.errorSelector);
        var rules = selectorRules[rule.selector];

        // Chạy từng test trong rules
        for (var i = 0; i < rules.length; i++) {
            errorMessage = rules[i](inputElement.value);
            if (errorMessage) break;
        }

        // Hiển thị lỗi
        if (errorMessage) {
            errorElement.innerText = errorMessage;
            inputElement.parentElement.classList.add("invalid");
        } else {
            errorElement.innerText = "";
            inputElement.parentElement.classList.remove("invalid");
        }

        return !errorMessage; // true = hợp lệ, false = lỗi
    }

    if (formElement) {
        formElement.onsubmit = function (event) {
            var isFormValid = true;

            options.rules.forEach(function (rule) {
                var inputElement = formElement.querySelector(rule.selector);
                var valid = validate(inputElement, rule);

                if (!valid) {
                    isFormValid = false;
                }
            });

            if (!isFormValid) {
                event.preventDefault();
            }
        };

        options.rules.forEach((rule) => {
            if (!selectorRules[rule.selector]) {
                selectorRules[rule.selector] = [rule.test];
            } else {
                selectorRules[rule.selector].push(rule.test);
            }

            var inputElement = formElement.querySelector(rule.selector);

            if (inputElement) {
                inputElement.onblur = function () {
                    validate(inputElement, rule);
                };

                inputElement.oninput = function () {
                    var errorElement = inputElement.parentElement.querySelector(options.errorSelector);
                    errorElement.innerText = "";
                    inputElement.parentElement.classList.remove("invalid");
                };
            }
        })
    }
}

// =====================
//   RULE DEFINITIONS
// =====================

Validator.isRequired = function (selector) {
    return {
        selector: selector,
        test: function (value) {
            console.log("Check value: " + value.trim());

            return value.trim() ? undefined : "Vui lòng nhập trường này";
        }
    };
};

Validator.isSelected = function (selector) {
    return {
        selector: selector,
        test: function (value) {
            return value ? undefined : "Vui lòng chọn một mục";
        }
    };
};

Validator.isPhoneNumber = function (selector) {
    return {
        selector: selector,
        test: function (value) {
            var regex = /(84|0[3|5|7|8|9])+([0-9]{8})\b/g;
            return regex.test(value) ? undefined : "Số điện thoại không hợp lệ";
        }
    };
};

// Healper funtion
