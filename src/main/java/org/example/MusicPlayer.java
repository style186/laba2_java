import java.util.*;
import java.io.*;

class Song {
    public String title;
    public String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return title + " - " + artist;
    }
}

class Playlist {
    public String name;
    public List<Song> songs;
    public int currentSongIndex;

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
        this.currentSongIndex = -1; // Нет текущей песни
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public boolean removeSong(int songIndex) {
        if (songIndex >= 0 && songIndex < songs.size()) {
            songs.remove(songIndex);
            return true;
        }
        return false;
    }

    public Song getCurrentSong() {
        if (currentSongIndex >= 0 && currentSongIndex < songs.size()) {
            return songs.get(currentSongIndex);
        }
        return null;
    }

    // В классе Playlist
    public Song getPreviousSong() {
        if (songs.isEmpty()) {
            return null;
        }
        currentSongIndex = (currentSongIndex - 1 + songs.size()) % songs.size();
        return getCurrentSong();
    }

    public Song getNextSong() {
        if (songs.isEmpty()) {
            return null;
        }
        currentSongIndex = (currentSongIndex + 1) % songs.size();
        return getCurrentSong();
    }

    public void setCurrentSongIndex(int index) {
        if (index >= 0 && index < songs.size()) {
            currentSongIndex = index;
        }
    }

    public void saveToFile(String filename) throws IOException {
        File file = new File(filename);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (Song song : songs) {
            writer.write(song.getTitle() + ";" + song.getArtist());
            writer.newLine();
        }
        writer.close();
    }

    public void loadFromFile(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length == 2) {
                addSong(new Song(parts[0], parts[1]));
            }
        }
        reader.close();
    }
    // Геттер для имени плейлиста
    public String getName() {
        return name;
    }

    // Геттер для списка песен
    public List<Song> getSongs() {
        return new ArrayList<>(songs); // Возвращаем копию списка, чтобы предотвратить изменения извне
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Плейлист: " + name + "\n");
        for (int i = 0; i < songs.size(); i++) {
            sb.append(i + 1).append(". ").append(songs.get(i)).append("\n");
        }
        return sb.toString();
    }

}

public class MusicPlayer {
    public static final Scanner scanner = new Scanner(System.in);
    public static final Map<Integer, Playlist> playlists = new HashMap<>();
    public static int playlistIdCounter = 1;
    public static Playlist currentPlaylist;
    public static Song currentSong;

    public static void showSongs() {
        if (currentPlaylist != null) {
            System.out.println(currentPlaylist);
        } else {
            System.out.println("Нет активного плейлиста.");
        }
    }
    public static void loadPlaylistFromFile(int id,String filename) {

        if (playlists.containsKey(id)) {
            currentPlaylist = playlists.get(id);
            try {

                currentPlaylist.loadFromFile(filename);
                System.out.println("Плейлист загружен из файла '" + filename + "'.");
            } catch (IOException e) {
                System.out.println("Ошибка при загрузке плейлиста: " + e.getMessage());
            }
        } else {
            System.out.println("Плейлист с таким ID не найден.");
        }
    }



    public static void createPlaylist(String name) {
        Playlist playlist = new Playlist(name);
        playlists.put(playlistIdCounter++, playlist);
        System.out.println("Плейлист '" + name + "' создан.");
    }

    public static void playPlaylist(int playlistNumber) {

        if (playlistNumber > 0 && playlistNumber <= playlists.size()) {
            currentPlaylist = playlists.get(playlistNumber);
            if (currentPlaylist != null && !currentPlaylist.getSongs().isEmpty()) {
                currentPlaylist.setCurrentSongIndex(0); // Устанавливаем воспроизведение с первого трека
                currentSong = currentPlaylist.getCurrentSong();
                System.out.println("Воспроизведение плейлиста: " + currentPlaylist.getName());
                System.out.println("Текущий трек: " + currentSong);
            } else {
                System.out.println("Плейлист пуст или не найден.");
            }
        } else {
            System.out.println("Плейлист с таким номером не существует.");
        }
    }

    public static void savePlaylist(int playlistNumber, String filename) {
        if (!playlists.isEmpty()) {

            if (playlistNumber > 0 && playlistNumber <= playlists.size()) {
                Playlist selectedPlaylist = playlists.get(playlistNumber); // Получаем плейлист по номеру
                try {

                    selectedPlaylist.saveToFile(filename);
                    System.out.println("Плейлист сохранен в файл '" + filename + "'.");
                } catch (IOException e) {
                    System.out.println("Ошибка при сохранении плейлиста: " + e.getMessage());
                }
            } else {
                System.out.println("Плейлиста с таким номером не существует.");
            }
        } else {
            System.out.println("Нет плейлистов для сохранения.");
        }
    }

    public static void deletePlaylist(int id) {

        if (playlists.remove(id) != null) {
            System.out.println("Плейлист удален.");
        } else {
            System.out.println("Плейлист с таким ID не найден.");
        }
    }





    public static void addSongToPlaylist(String title,String artist) {
        if (currentPlaylist != null) {

            currentPlaylist.addSong(new Song(title, artist));
            System.out.println("Песня '" + title + "' добавлена в плейлист.");
        } else {
            System.out.println("Нет активного плейлиста для добавления песни.");
        }
    }

    public static void showPlaylist(int playlistNumber) {
        Playlist selectedPlaylist = playlists.get(playlistNumber);
        if (selectedPlaylist != null) {
            System.out.println(selectedPlaylist);
        } else {
            System.out.println("Плейлист с номером " + playlistNumber + " не найден.");
        }
    }

    public static void removeSongFromPlaylist(int playlistNumber,int songIndex) {

        Playlist playlist = playlists.get(playlistNumber);

        if (playlist != null) {
            System.out.println(playlist);

            songIndex = songIndex - 1;
            if (playlist.removeSong(songIndex)) {
                System.out.println("Песня удалена из плейлиста.");
            } else {
                System.out.println("Песня с таким номером не найдена в плейлисте " + playlistNumber + ".");
            }
        } else {
            System.out.println("Плейлист с номером " + playlistNumber + " не найден.");
        }
    }

    public static void showAllSongs() {
        if (playlists.isEmpty()) {
            System.out.println("Нет плейлистов для отображения песен.");
            return;
        }

        System.out.println("Все песни:");
        for (Playlist playlist : playlists.values()) {
            System.out.println("Плейлист: " + playlist.getName()); // Используем геттер для имени
            for (Song song : playlist.getSongs()) { // Используем геттер для списка песен
                System.out.println(song);
            }
            System.out.println(); // Добавляем пустую строку для разделения плейлистов
        }
    }

    // В классе MusicPlayer
    public static void playPreviousTrack() {
        if (currentPlaylist != null) {
            currentSong = currentPlaylist.getPreviousSong();
            if (currentSong != null) {
                System.out.println("Воспроизведение предыдущего трека: " + currentSong);
            }
        } else {
            System.out.println("Нет активного плейлиста.");
        }
    }

    public static void playNextTrack() {
        if (currentPlaylist != null) {
            currentSong = currentPlaylist.getNextSong();
            if (currentSong != null) {
                System.out.println("Воспроизведение следующего трека: " + currentSong);
            }
        } else {
            System.out.println("Нет активного плейлиста.");
        }
    }

    public static void repeatCurrentTrack() {
        if (currentSong != null) {
            System.out.println("Повтор текущего трека: " + currentSong);
        } else {
            System.out.println("Нет трека для повтора.");
        }
    }}
