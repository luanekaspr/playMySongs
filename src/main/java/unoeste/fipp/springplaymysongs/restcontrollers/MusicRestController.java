package unoeste.fipp.springplaymysongs.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unoeste.fipp.springplaymysongs.entities.Erro;
import unoeste.fipp.springplaymysongs.entities.Music;
import unoeste.fipp.springplaymysongs.entities.Style;
import unoeste.fipp.springplaymysongs.services.MusicService;

import java.io.File;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("apis")
public class MusicRestController {

    @Autowired
    private MusicService musicService;

    @GetMapping("find-musics")
    public ResponseEntity<Object> findMusic(String keyword){
        if(!keyword.isEmpty()){
            List<Music> musicList=musicService.findMusicsByKeyWord(keyword);
            return ResponseEntity.ok(musicList);
        }
        return ResponseEntity.badRequest().body(new Erro("Música não encontrada!",""));
    }

    @GetMapping("get-music-styles")
    public ResponseEntity<Object> getStyles() {
        List<Style> styles = musicService.findMusicStyles();
        return ResponseEntity.ok(styles);
    }

    @PostMapping("music-upload")
    public ResponseEntity<Object> addMusic(String titulo, String estilo, String artista) {

        if(titulo == null || titulo.isEmpty()) {
            return ResponseEntity.badRequest().body(new Erro("Música sem título",""));
        }

        Style style = musicService.getStyleByName(estilo);
        if(style == null){
            return ResponseEntity.badRequest().body(new Erro("Estilo não encontrado",""));
        }

        try {
            Music music = new Music(titulo, estilo, artista);
            boolean sucesso = musicService.musicUpload(music);
            if(!sucesso) {
                return ResponseEntity.badRequest().body(new Erro("Erro ao gravar no banco!",""));
            }
            return ResponseEntity.ok().body(music);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao gravar!",""));
        }
    }
}
