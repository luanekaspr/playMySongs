function cadastrarMusica(){
    const fuser = document.forms[0];
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