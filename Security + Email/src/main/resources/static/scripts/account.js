let theme = loadThemePreference() === null ? 'light-theme' : loadThemePreference()
applyTheme(theme)
saveThemePreference(theme)

// Сохранение выбранной темы
function saveThemePreference(theme) {
    localStorage.setItem('theme', theme);
}

// Загрузка сохранённой темы
function loadThemePreference() {
    return localStorage.getItem('theme');
}

document.getElementById("theme-toggle").addEventListener('click', () => {
    const currentTheme = loadThemePreference();
    const newTheme = currentTheme === 'dark-theme' ? 'light-theme' : 'dark-theme';

    // Применение новой темы
    applyTheme(newTheme);

    // Сохранение выбранной темы
    saveThemePreference(newTheme);
});

function applyTheme(theme) {
    document.body.classList.remove('light-theme', 'dark-theme');
    document.body.classList.add(theme);

    if (theme !== 'dark-theme') {
        document.body.classList.remove("dark-theme");
        document.getElementById("theme-toggle").classList.add("fa-moon");
        document.getElementById("theme-toggle").classList.remove("fa-sun");
    } else {
        document.body.classList.add("dark-theme");
        document.getElementById("theme-toggle").classList.remove("fa-moon");
        document.getElementById("theme-toggle").classList.add("fa-sun");
    }
}