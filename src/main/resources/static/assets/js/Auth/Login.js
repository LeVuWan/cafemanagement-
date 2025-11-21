document.getElementById("btnComfirmLogin").addEventListener("click", function(event) {
    const username = document.querySelector('input[name="username"]').value.trim();
    const password = document.querySelector('input[name="password"]').value.trim();

    const errorBox = document.getElementById("loginError");

    errorBox.classList.add("d-none");
    errorBox.textContent = "";

    if (username === "" || password === "") {
        event.preventDefault(); 
        errorBox.textContent = "Username và Password không được để trống!";
        errorBox.classList.remove("d-none");
        return;
    }
});
