package unoeste.fipp.springplaymysongs.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unoeste.fipp.springplaymysongs.entities.Music;
import unoeste.fipp.springplaymysongs.entities.MusicStyles;
import unoeste.fipp.springplaymysongs.repositories.MusicStyleRepository;
import unoeste.fipp.springplaymysongs.services.MusicService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("apis")
public class MusicRestController {

    @Autowired
    private MusicService musicService;
    @Autowired
    private MusicStyleRepository musicStyleRepository;

    @GetMapping("find-musics")
    public ResponseEntity<Object> findMusic(String keyword){
        if(!keyword.isEmpty()){
            List<Music> musicList=musicService.findMusicsByKeyWord(keyword);
            return ResponseEntity.ok(musicList);
        }
        return ResponseEntity.badRequest().body("objeto de erro");
    }

    @GetMapping("get-music-styles")
    public ResponseEntity<Object> getStyles() {
        List<MusicStyles> styles = musicStyleRepository.getMusicStyles();
        return ResponseEntity.ok().body(styles);
    }
}
