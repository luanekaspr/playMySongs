package unoeste.fipp.springplaymysongs.services;

import com.google.gson.Gson;
import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unoeste.fipp.springplaymysongs.entities.Music;
import unoeste.fipp.springplaymysongs.entities.Style;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Service
public class MusicService {

    // Diretório onde as músicas serão salvas
    // Colocamos fora do resources para que fosse lido o arquivo mp3 assim que cadastrado, sem reiniciar
    private static final String UPLOAD_FOLDER = "uploads/";

    public List<Music> findMusicsByKeyWord(String keyword){
        String connectionString = "mongodb://localhost:27017";

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {

            MongoDatabase database = mongoClient.getDatabase("my_musics");
            MongoCollection<Document> collection = database.getCollection("musics");

            Document filter = new Document("$or", Arrays.asList(
                    new Document("titulo",  new Document("$regex", keyword).append("$options", "i")),
                    new Document("estilo",  new Document("$regex", keyword).append("$options", "i")),
                    new Document("artista", new Document("$regex", keyword).append("$options", "i"))
            ));

            List<Music> musicList = new ArrayList<>();
            MongoCursor<Document> mongoCursor = collection.find(filter).iterator(); // filter vai no find!

            while(mongoCursor.hasNext()) {
                musicList.add(new Gson().fromJson(mongoCursor.next().toJson(), Music.class));
            }
            return musicList;

        } catch (Exception e) {
            System.err.println("Erro ao encontrar músicas: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Music> findAllMusics() {
        List<Music> musicList = new ArrayList<>();
        String connectionString = "mongodb://localhost:27017";

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase("my_musics");
            MongoCollection<Document> collection = database.getCollection("musics");

            MongoCursor<Document> mongoCursor = collection.find().iterator();

            while (mongoCursor.hasNext()) {
                musicList.add(new Gson().fromJson(mongoCursor.next().toJson(), Music.class));
            }
            return musicList;

        } catch (Exception e) {
            System.err.println("Erro ao buscar músicas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Style> findMusicStyles() {
        List<Style> styleList = new ArrayList<>();
        String connectionString = "mongodb://localhost:27017";
        MongoClient mongoClient = MongoClients.create(connectionString);

        MongoDatabase database = mongoClient.getDatabase("my_musics");
        MongoCollection<Document> collection = database.getCollection("styles");
        MongoCursor<Document> mongoCursor = collection.find().sort(eq("nome", 1L)).iterator();
        while(mongoCursor.hasNext()) {
            styleList.add(new Gson().fromJson(mongoCursor.next().toJson(),Style.class));
        }
        return styleList;
    }

    public boolean musicUpload(Music music, MultipartFile file) {
        String connectionString = "mongodb://localhost:27017";

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {

            MongoDatabase database = mongoClient.getDatabase("my_musics");
            MongoCollection<Document> collection = database.getCollection("musics");

            String nomeArquivo = gerarNomeArquivo(music.getTitulo(), music.getEstilo(),
                    music.getArtista(), file.getOriginalFilename());

            try {
                File uploadFolder = new File(UPLOAD_FOLDER);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs();
                }
                file.transferTo(new File(uploadFolder.getAbsolutePath() + File.separator + nomeArquivo));
            } catch (Exception e) {
                System.err.println("Erro ao armazenar o arquivo: " + e.getMessage());
                e.printStackTrace();
                return false;
            }

            Music m = new Music(music.getTitulo(), music.getEstilo(),
                    music.getArtista(), nomeArquivo);
            m.setUrl("/uploads/" + nomeArquivo);

            collection.insertOne(Document.parse(new Gson().toJson(m)));
            return true;

        } catch (Exception e) {
            System.err.println("Erro geral: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private String gerarNomeArquivo(String titulo, String estilo, String artista, String nomeOriginal) {
        String extensao = "";
        if (nomeOriginal != null && nomeOriginal.contains(".")) {
            extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
        }
        String tituloLimpo = limparString(titulo);
        String estiloLimpo = limparString(estilo);
        String artistaLimpo = limparString(artista);

        return String.format("%s_%s_%s%s", tituloLimpo, estiloLimpo, artistaLimpo, extensao);
    }

    private String limparString(String texto) {
        if (texto == null) return "";

        return texto.toLowerCase()
                .replaceAll("[\\s]+", "") // Remove espaços
                .replaceAll("[áàâãä]", "a")
                .replaceAll("[éèêë]", "e")
                .replaceAll("[íìîï]", "i")
                .replaceAll("[óòôõö]", "o")
                .replaceAll("[úùûü]", "u")
                .replaceAll("[ç]", "c")
                .replaceAll("[^a-z0-9]", "");
    }

    public Style getStyleByName(String nome) {
        List<Style> styles = findMusicStyles();
        for (Style s : styles) {
            if(s.getNome().equalsIgnoreCase(nome))
                return s;
        }
        return null;
    }
}
