package unoeste.fipp.springplaymysongs.services;

import com.google.gson.Gson;
import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.stereotype.Service;
import unoeste.fipp.springplaymysongs.entities.Music;
import unoeste.fipp.springplaymysongs.entities.Style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Service
public class MusicService {

    public List<Music> findMusicsByKeyWord(String keyword){
        List<Music> musicList = new ArrayList<>();
        musicList.add(new Music("Jetski","funk","Pedro Sampaio"));
        musicList.add(new Music("Para sempre com você","sertanejo","Jorge e Mateus"));
        return musicList;
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

    public boolean musicUpload(Music music) {
        String connectionString = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {

            MongoDatabase database = mongoClient.getDatabase("my_musics");
            MongoCollection<Document> collection = database.getCollection("musics");

            Music m = new Music(music.getTitulo(), music.getEstilo(), music.getArtista());
            collection.insertOne(Document.parse(new Gson().toJson(m)));
            return true;
        }catch (Exception e) {
                return false;
            }
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
