package unoeste.fipp.springplaymysongs.repositories;

import org.springframework.stereotype.Repository;
import unoeste.fipp.springplaymysongs.entities.Music;
import unoeste.fipp.springplaymysongs.entities.MusicStyles;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MusicStyleRepository {
    private List<MusicStyles> musicStyles  = new ArrayList<>();

    public MusicStyleRepository() {
        musicStyles.add(new MusicStyles("1", "Sertanejo"));
        musicStyles.add(new MusicStyles("2", "Funk"));
        musicStyles.add(new MusicStyles("3", "Pop"));
        musicStyles.add(new MusicStyles("4", "Rock"));
        musicStyles.add(new MusicStyles("5", "MPB"));
        musicStyles.add(new MusicStyles("6", "Pagode"));
        musicStyles.add(new MusicStyles("7", "Forró"));
        musicStyles.add(new MusicStyles("8", "Eletrônica"));
        musicStyles.add(new MusicStyles("9", "Rap"));
        musicStyles.add(new MusicStyles("10", "Hip Hop"));
        musicStyles.add(new MusicStyles("11", "Axé"));
        musicStyles.add(new MusicStyles("12", "Reggae"));
        musicStyles.add(new MusicStyles("13", "Jazz"));
        musicStyles.add(new MusicStyles("14", "Blues"));
        musicStyles.add(new MusicStyles("15", "Clássica"));
        musicStyles.add(new MusicStyles("16", "Country"));
        musicStyles.add(new MusicStyles("17", "Gospel"));
        musicStyles.add(new MusicStyles("18", "Indie"));
        musicStyles.add(new MusicStyles("19", "K-pop"));
        musicStyles.add(new MusicStyles("20", "Samba"));
    }

    public List<MusicStyles> getMusicStyles() {
        return musicStyles;
    }

    public MusicStyles findById(String id) {
        for (MusicStyles m : musicStyles) {
            if(m.id().equals(id))
                return m;
        }
        return null;
    }
}
