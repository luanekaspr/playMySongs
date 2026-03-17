package unoeste.fipp.springplaymysongs.services;

import org.springframework.stereotype.Service;
import unoeste.fipp.springplaymysongs.entities.Music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MusicService {

    public List<Music> findMusicsByKeyWord(String keyword){
        List<Music> musicList = new ArrayList<>();
        musicList.add(new Music("Jetski","funk","Pedro Sampaio"));
        musicList.add(new Music("Para sempre com você","sertanejo","Jorge e Mateus"));
        return musicList;
    }
}
