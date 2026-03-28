package unoeste.fipp.springplaymysongs.restcontrollers;

import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private HttpServletRequest request;

    @GetMapping("find-musics")
    public ResponseEntity<Object> findMusics(@RequestParam("keyword") String keyword) {
        List<Music> musics = musicService.findMusicsByKeyWord(keyword);

        if (musics == null || musics.isEmpty()) {
            return ResponseEntity.badRequest().body(new Erro("Nenhuma música encontrada", ""));
        }

        for (Music music : musics) {
            String url = getHostStatic() + music.getMusicFileName();
            music.setUrl(url);
        }

        return ResponseEntity.ok(musics);
    }


    @GetMapping("get-music-styles")
    public ResponseEntity<Object> getStyles() {
        List<Style> styles = musicService.findMusicStyles();
        return ResponseEntity.ok(styles);
    }

    @PostMapping("music-upload")
    public ResponseEntity<Object> addMusic(String titulo, String estilo, String artista, MultipartFile arquivo) {

        if(titulo == null || titulo.isEmpty()) {
            return ResponseEntity.badRequest().body(new Erro("Música sem título",""));
        }

        if (arquivo == null || arquivo.isEmpty()) {
            return ResponseEntity.badRequest().body(new Erro("Arquivo não enviado", ""));
        }

        Style style = musicService.getStyleByName(estilo);
        if(style == null){
            return ResponseEntity.badRequest().body(new Erro("Estilo não encontrado",""));
        }

        try {
            Music music = new Music(titulo, estilo, artista);
            boolean sucesso = musicService.musicUpload(music, arquivo);
            if(!sucesso) {
                return ResponseEntity.badRequest().body(new Erro("Erro ao gravar no banco!",""));
            }
            return ResponseEntity.ok().body(music);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao gravar!",""));
        }
    }

    @GetMapping("get-all-musics")
    public ResponseEntity<Object> getAllMusics() {
        List<Music> musicas = musicService.findAllMusics();

        for (Music music : musicas) {
            String url = getHostStatic() + music.getMusicFileName();
            music.setUrl(url);
        }

        return ResponseEntity.ok(musicas);
    }

    private String getHostStatic() {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + "/uploads/";
    }
}
