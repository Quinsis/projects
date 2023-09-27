window.addEventListener("load", function () {
    if (document.getElementById("email").value.length > 0) {
        document.getElementById("label_email").classList.add("focused");
        document.getElementById("label_email").style.color="#636363";
    }
});

document.getElementById("email").addEventListener("focus", function () {
    document.getElementById("label_email").classList.add("focused");
    document.getElementById("label_email").style.color="#10A37F";
});
document.getElementById("email").addEventListener("focusout", function () {
    if (document.getElementById("email").value == "") {
        document.getElementById("label_email").classList.remove("focused");
    }
    document.getElementById("label_email").style.color="#636363";
});