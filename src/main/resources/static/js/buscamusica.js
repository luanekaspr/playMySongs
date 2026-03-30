async function pesquisarMusicas(){
    const keyword = document.getElementById("keyword").value.trim();
    const response = await fetch(`http://localhost:8080/apis/find-musics?keyword=${keyword}`);
    const data = await response.json();

    carregarmusicas(data);
}

function carregarmusicas(data){
    const lista = document.getElementById("lista-musicas");
    const contador = document.getElementById("contador");
    lista.innerHTML = '';

    if(!data || data.length === 0){
        lista.innerHTML = '<p>Nenhuma música encontrada.</p>';
        return;
    }

    contador.textContent = `${data.length} música(s) encontrada(s)`;

    data.forEach(music => {
        const extensao = music.musicFileName?.split('.').pop().toLowerCase();
        const mimeType = extensao === 'ogg' ? 'audio/ogg' : 'audio/mp3';

        const card = document.createElement('div');
        card.innerHTML = `
            <h4>${music.titulo}</h4>
            <p>Artista: ${music.artista}</p>
            <p>Estilo: ${music.estilo}</p>
            <audio controls>
                <source src="${music.url}" type="${mimeType}">
            </audio>
        `;
        lista.appendChild(card);
    });
}