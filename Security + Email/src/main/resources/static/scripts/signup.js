let passIsValid = false;
window.addEventListener("load", function () {
    if (document.getElementById("name").value.length > 0) {
        document.getElementById("label_name").classList.add("focused");
        document.getElementById("label_name").style.color="#636363";
    }
    if (document.getElementById("password").value.length > 0) {
        document.getElementById("label_password").classList.add("focused");
        document.getElementById("label_password").style.color="#636363";
    }
    if (document.getElementById("email").value.length > 0) {
        document.getElementById("label_email").classList.add("focused");
        document.getElementById("label_email").style.color="#636363";
    }
    if (document.getElementById("phone").value.length > 0) {
        document.getElementById("label_phone").classList.add("focused-phone");
        document.getElementById("label_phone").style.color="#636363";
    }
});

function validate() {
    let name = document.getElementById("name").value;
    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;
    let phone = document.getElementById("phone");
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

    if (name == "") {
        document.getElementById("submit").setAttribute("disabled", "true");
        document.getElementById("submit").classList.add("disabled");
    } else if (email == "") {
        document.getElementById("submit").setAttribute("disabled", "true");
        document.getElementById("submit").classList.add("disabled");
    } else if (phone.value.length < 10) {
        document.getElementById("submit").setAttribute("disabled", "true");
        document.getElementById("submit").classList.add("disabled");
    } else if (!passIsValid) {
        document.getElementById("submit").setAttribute("disabled", "true");
        document.getElementById("submit").classList.add("disabled");
    } else {
        document.getElementById("submit").removeAttribute("disabled");
        document.getElementById("submit").classList.remove("disabled");
    }

    if (!isPhone(phone.value)) {
        phone.value = phone.value.substring(0, phone.value.length - 1);
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

function isPhone(object) {
    for (let i = 0; i < object.length; i++) {
        if (!isDigit(object.charAt(i))) return false;
    }
    return true;
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

document.getElementById("name").addEventListener("focus", function () {
    document.getElementById("label_name").classList.add("focused");
    document.getElementById("label_name").style.color = "#10A37F";
});
document.getElementById("name").addEventListener("focusout", function () {
    if (document.getElementById("name").value == "") {
        document.getElementById("label_name").classList.remove("focused");
    }
    document.getElementById("label_name").style.color="#636363";
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

document.getElementById("email").addEventListener("focus", function () {
    document.getElementById("label_email").classList.add("focused");
    document.getElementById("label_email").style.color = "#10A37F";
});
document.getElementById("email").addEventListener("focusout", function () {
    if (document.getElementById("email").value == "") {
        document.getElementById("label_email").classList.remove("focused");
    }
    document.getElementById("label_email").style.color = "#636363";
});

document.getElementById("phone").addEventListener("focus", function () {
    document.getElementById("label_phone").classList.add("focused-phone");
    document.getElementById("label_phone").style.color = "#10A37F";
});
document.getElementById("phone").addEventListener("focusout", function () {
    if (document.getElementById("phone").value == "") {
        document.getElementById("label_phone").classList.remove("focused-phone");
    }
    document.getElementById("label_phone").style.color = "#636363";
});