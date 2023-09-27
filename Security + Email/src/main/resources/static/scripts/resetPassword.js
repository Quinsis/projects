let passIsValid = false;
window.addEventListener("load", function () {
    if (document.getElementById("password").value.length > 0) {
        document.getElementById("label_password").classList.add("focused");
        document.getElementById("label_password").style.color="#636363";
    }
    if (document.getElementById("confirm-password").value.length > 0) {
        document.getElementById("label_confirm-password").classList.add("focused");
        document.getElementById("label_confirm-password").style.color="#636363";
    }
});

document.getElementById("password").addEventListener("focus", function () {
    document.getElementById("label_password").classList.add("focused");
    document.getElementById("label_password").style.color = "#10A37F";
});
document.getElementById("password").addEventListener("focusout", function () {
    if (document.getElementById("password").value == "") {
        document.getElementById("label_password").classList.remove("focused");
    }
    document.getElementById("label_password").style.color = "#636363";
});

document.getElementById("confirm-password").addEventListener("focus", function () {
    document.getElementById("label_confirm-password").classList.add("focused");
    document.getElementById("label_confirm-password").style.color = "#10A37F";
});
document.getElementById("confirm-password").addEventListener("focusout", function () {
    if (document.getElementById("confirm-password").value == "") {
        document.getElementById("label_confirm-password").classList.remove("focused");
    }
    document.getElementById("label_confirm-password").style.color = "#636363";
});

function validate() {
    let password = document.getElementById("password").value;
    let confirmPassword = document.getElementById("confirm-password").value;
    let symbolAndDigit;
    let length;
    let latin;

    length = password.length >= 16;
    if (length) document.getElementsByClassName("main__form__tips__item__small")[0].classList.add("tip-complete");
    else document.getElementsByClassName("main__form__tips__item__small")[0].classList.remove("tip-complete");

    latin = !containsCyrillic(password) && containsLatin(password) && hasUpperCaseAndLowerCase(password);
    if (latin) document.getElementsByClassName("main__form__tips__item__small")[1].classList.add("tip-complete");
    else document.getElementsByClassName("main__form__tips__item__small")[1].classList.remove("tip-complete");

    symbolAndDigit = containsSpecSymbol(password) && containsDigit(password);
    if (symbolAndDigit) document.getElementsByClassName("main__form__tips__item__small")[2].classList.add("tip-complete");
    else document.getElementsByClassName("main__form__tips__item__small")[2].classList.remove("tip-complete");

    passIsValid = symbolAndDigit && length && latin;

    if (passIsValid) {
        document.getElementsByClassName("main__form__tips")[0].style.border="1px solid #10A37F";
        document.getElementsByClassName("main__form__tips__text")[0].innerHTML = "Все требования выполнены!";
        document.getElementsByClassName("main__form__tips__text")[0].style.color = "#10A37F";
    } else {
        document.getElementsByClassName("main__form__tips")[0].style.border="1px solid #d3d3d3";
        document.getElementsByClassName("main__form__tips__text")[0].innerHTML = "Ваш пароль должен содержать:";
        document.getElementsByClassName("main__form__tips__text")[0].style.color = "#636363";
    }

    if (!passIsValid) {
        document.getElementById("submit").setAttribute("disabled", "true");
        document.getElementById("submit").classList.add("disabled");
    } else if (password === confirmPassword) {
        document.getElementById("submit").removeAttribute("disabled");
        document.getElementById("submit").classList.remove("disabled");
    }
}
setInterval(validate, 1);

function hasUpperCaseAndLowerCase(object) {
    let hasUpperCase = false, hasLowerCase = false;
    for (let i = 0; i < object.length; i++) {
        if (isDigit(object.charAt(i)) || isSpecSymbol(object.charAt(i))) continue;
        if (object.charAt(i) == object.charAt(i).toUpperCase()) hasUpperCase = true;
        else if (object.charAt(i) == object.charAt(i).toLowerCase()) hasLowerCase = true;
    }
    return hasUpperCase && hasLowerCase;
}

function containsDigit(object) {
    for (let i = 0; i < object.length; i++) {
        if (isDigit(object.charAt(i))) return true;
    }
    return false;
}

function containsSpecSymbol(object) {
    for (let i = 0; i < object.length; i++) {
        if (isSpecSymbol(object.charAt(i))) return true;
    }
    return false;
}

function containsLatin(object) {
    for (let i = 0; i < object.length; i++) {
        if (isLatin(object.charAt(i))) return true;
    }
    return false;
}

function containsCyrillic(object) {
    for (let i = 0; i < object.length; i++) {
        if (isCyrillic(object.charAt(i))) return true;
    }
    return false;
}

function isSpecSymbol(object) {
    return /[!"#$%&'()*+,\-./:;<=>?@\[\\\]\^_`\{|\}~]/i.test(object);
}

function isDigit(object) {
    return /[0-9]/i.test(object);
}

function isLatin(object) {
    return /[a-z]/i.test(object);
}

function isCyrillic(object) {
    return /[а-я]/i.test(object);
}

function generatePassword() {
    document.getElementById("label_password").classList.add("focused");
    document.getElementById("label_password").style.color = "#636363";
    const charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    let password = "";

    while (!containsSpecSymbol(password) || !containsDigit(password) || !hasUpperCaseAndLowerCase(password)) {
        password = "";
        for (let i = 0; i < 16; i++) {
            const randomIndex = Math.floor(Math.random() * charset.length);
            password += charset.charAt(randomIndex);
        }
    }

    document.getElementById("password").value = password;
}

document.getElementsByClassName("main__form__tips__generate")[0].addEventListener("click", generatePassword);

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

document.getElementById("confirm-password-toggle").addEventListener("click", function () {
    if (document.getElementById("confirm-password-toggle").classList.contains("toggled")) {
        document.getElementById("confirm-password-toggle").classList.remove("toggled");
        document.getElementById("confirm-password-toggle").classList.add("fa-eye");
        document.getElementById("confirm-password-toggle").classList.remove("fa-eye-slash");
        document.getElementById("confirm-password").setAttribute("type", "password");
    } else {
        document.getElementById("confirm-password-toggle").classList.add("toggled");
        document.getElementById("confirm-password-toggle").classList.add("fa-eye-slash");
        document.getElementById("confirm-password-toggle").classList.remove("fa-eye");
        document.getElementById("confirm-password").setAttribute("type", "text");
    }
});