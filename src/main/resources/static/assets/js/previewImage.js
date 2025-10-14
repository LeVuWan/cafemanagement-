$(document).ready(() => {

    const avatarFile = $("#imageFile");
    avatarFile.change(function (e) {

        const imgURL = URL.createObjectURL(e.target.files[0]);
        $("#imagePriview").attr("src", imgURL).show();
    });
});