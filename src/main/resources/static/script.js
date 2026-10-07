document.addEventListener("DOMContentLoaded", function () {
    const colorCodeEl = document.getElementById("color-code");
    const saludoEl = document.getElementById("saludo");
    const copyBtn = document.getElementById("copy-button");
    const nombreInput = document.getElementById("nombre-input");
    const saludarBtn = document.getElementById("saludar-button");
    const lista = document.getElementById("lista-saludos");

    function actualizarColor(color) {
        document.body.style.backgroundColor = color;
        colorCodeEl.textContent = color;
    }

    async function copiarTexto(texto) {
        try {
            await navigator.clipboard.writeText(texto);
            return true;
        } catch (err) {
            const temp = document.createElement("input");
            temp.value = texto;
            document.body.appendChild(temp);
            temp.select();
            try {
                const success = document.execCommand("copy");
                document.body.removeChild(temp);
                return success;
            } catch (e) {
                document.body.removeChild(temp);
                return false;
            }
        }
    }

    async function refrescarSaludos() {
        const response = await fetch("/saludos");
        if (!response.ok) {
            return;
        }
        const items = await response.json();
        lista.innerHTML = "";
        items.forEach(function (item) {
            const li = document.createElement("li");
            const span = document.createElement("span");
            const code = document.createElement("code");
            span.textContent = item.saludo;
            code.textContent = item.color;
            li.appendChild(span);
            li.appendChild(code);
            lista.appendChild(li);
        });
    }

    copyBtn.addEventListener("click", async function () {
        const ok = await copiarTexto(colorCodeEl.textContent);
        copyBtn.textContent = ok ? "Copiado" : "No se pudo copiar";
        setTimeout(function () {
            copyBtn.textContent = "Copiar hex";
        }, 2000);
    });

    saludarBtn.addEventListener("click", async function () {
        const nombre = nombreInput.value.trim();
        if (nombre === "") {
            alert("Escribe un nombre");
            return;
        }
        try {
            const response = await fetch("/saludar", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ nombre: nombre })
            });
            const data = await response.json();
            if (!response.ok) {
                throw new Error(data.detail || "Error al saludar");
            }
            saludoEl.textContent = data.saludo;
            actualizarColor(data.color);
            nombreInput.value = "";
            await refrescarSaludos();
        } catch (error) {
            alert(error.message);
        }
    });

    nombreInput.addEventListener("keypress", function (event) {
        if (event.key === "Enter") {
            saludarBtn.click();
        }
    });
});
