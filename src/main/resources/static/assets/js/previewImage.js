$(document).ready(() => {

    const avatarFile = $("#imageFile");

    avatarFile.on("click", function () {
        $("#imagePriview").attr("src", "").hide();
        $(this).val("");
    });

    avatarFile.change(function (e) {
        const imgURL = URL.createObjectURL(e.target.files[0]);
        $("#imagePriview").attr("src", imgURL).show();
    });
});