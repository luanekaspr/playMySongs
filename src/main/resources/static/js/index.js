async function carregarMusicas() {
    const response = await fetch('http://localhost:8080/apis/get-all-musics');
    const data = await response.json();

    const lista = document.getElementById('lista-musicas');
    const contador = document.getElementById('contador');

    if (!data || data.length === 0) {
        lista.innerHTML = '<p>Nenhuma música cadastrada.</p>';
        return;
    }

    contador.textContent = `${data.length} música(s)`;

    data.forEach(music => {
        const extensao = music.musicFileName?.split('.').pop().toLowerCase();
        const mimeType = extensao === 'ogg' ? 'audio/ogg' : 'audio/mpeg';

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

carregarMusicas();