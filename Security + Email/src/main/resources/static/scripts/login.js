window.addEventListener("load", function () {
    if (document.getElementById("name").value.length > 0) {
        document.getElementById("label_name").classList.add("focused");
        document.getElementById("label_name").style.color="#636363";
    }
    if (document.getElementById("password").value.length > 0) {
        document.getElementById("label_password").classList.add("focused");
        document.getElementById("label_password").style.color="#636363";
    }
});

document.getElementById("password-toggle").addEventListener("click", function () {
    if (document.getElementById("password-toggle").classList.contains("toggled")) {
        document.getElementById("password-toggle").classList.remove("toggled");
        document.getElementById("password-toggle").classList.add("fa-eye");
        document.getElementById("password-toggle").classList.remove("fa-eye-slash");
        document.getElementById("password").setAttribute("type", "password");
    } else {
        document.getElementById("password-toggle").classList.add("toggled");
        document.getElementById("password-toggle").classList.add("fa-eye-slash");
        document.getElementById("password-toggle").classList.remove("fa-eye");
        document.getElementById("password").setAttribute("type", "text");
    }
});

document.getElementById("name").addEventListener("focus", function () {
    document.getElementById("label_name").classList.add("focused");
    document.getElementById("label_name").style.color="#10A37F";
});
document.getElementById("name").addEventListener("focusout", function () {
    if (document.getElementById("name").value == "") {
        document.getElementById("label_name").classList.remove("focused");
    }
    document.getElementById("label_name").style.color="#636363";
});

document.getElementById("password").addEventListener("focus", function () {
    document.getElementById("label_password").classList.add("focused");
    document.getElementById("label_password").style.color="#10A37F";
});
document.getElementById("password").addEventListener("focusout", function () {
    if (document.getElementById("password").value == "") {
        document.getElementById("label_password").classList.remove("focused");
    }
    document.getElementById("label_password").style.color="#636363";
});