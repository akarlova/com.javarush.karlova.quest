
const display = document.getElementById('timer');
const form    = document.getElementById('quizForm');
let remaining = TOTAL_MS;

function format(ms) {
    const totalSec = Math.ceil(ms/1000);
    const h = Math.floor(totalSec/3600).toString().padStart(2,'0');
    const m = Math.floor((totalSec%3600)/60).toString().padStart(2,'0');
    const s = (totalSec%60).toString().padStart(2,'0');
    return `${h}:${m}:${s}`;
}

display.textContent = format(remaining);

const interval = setInterval(() => {
    remaining -= 1000;
    if (remaining <= 0) {
        clearInterval(interval);
        display.textContent = "00:00:00";
        alert("Время вышло!");

        // создаём перед отправкой скрытое поле action=finishQuiz
        const ai = document.createElement('input');
        ai.type  = 'hidden';
        ai.name  = 'action';
        ai.value = 'finishQuiz';
        form.appendChild(ai);

        form.submit();
        return;
    }
    display.textContent = format(remaining);
}, 1000);
