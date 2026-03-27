function cadastrarMusica(){
    const fuser = document.forms[0];

    const arquivo = document.getElementById('arquivo').files[0];
    if (!arquivo) {
        alert("Por favor, selecione um arquivo MP3 ou OGG!");
        return;
    }

    const nomeArquivo = arquivo.name.toLowerCase();
    if (!nomeArquivo.endsWith('.mp3') && !nomeArquivo.endsWith('.ogg')) {
        alert("Formato inválido! Apenas arquivos MP3 ou OGG são permitidos.");
        return;
    }

    fetch("http://localhost:8080/apis/music-upload", {
        method: 'POST',
        body: new FormData(fuser)
    })
    .then(response => response.json().then(json => ({ status: response.status, body: json })))
    .then(({ status, body }) => {
        console.log("Resposta do backend:", status, body);
        if(status >= 200 && status < 300) {
            alert("Música "+body.titulo+" cadastrada com sucesso!");
            fuser.reset();
        } else {
            alert("Erro: " + (body.mensagem || "Não foi possível cadastrar a música"));
        }
    })
    .catch(error => alert("Problemas ao cadastrar a música: " + error));
}

document.addEventListener('DOMContentLoaded', function() {
    carregarEstilos();
});

// Função para carregar os estilos
async function carregarEstilos() {
    try {
        const response = await fetch('http://localhost:8080/apis/get-music-styles');
        const estilos = await response.json();

        const selectEstilo = document.getElementById('estilo');
        selectEstilo.innerHTML = '<option value="">Selecione um estilo</option>';

        estilos.forEach(estilo => {
            const option = document.createElement('option');
            option.value = estilo.nome;
            option.textContent = estilo.nome;
            selectEstilo.appendChild(option);
        });
    } catch (error) {
        console.error('Erro ao carregar estilos:', error);
        const selectEstilo = document.getElementById('estilo');
        selectEstilo.innerHTML = '<option value="">Erro ao carregar estilos</option>';
    }
}