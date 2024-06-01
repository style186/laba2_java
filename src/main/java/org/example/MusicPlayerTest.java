import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.List;

class MusicPlayerTest {

    @Test
    void testSavePlaylist() throws IOException {
        // Создаем тестовый файл
        BufferedWriter writer = new BufferedWriter(new FileWriter("aa.txt"));
        writer.newLine();
        writer.close();

        Playlist playlist = new Playlist("Test Playlist");
        playlist.addSong(new Song("Test Song", "Test Artist"));
        playlist.saveToFile("aa.txt");

        File file = new File("aa.txt");  // Создаем объект файла для представления файла "aa.txt".
        assertTrue(file.exists());

        // Проверяем содержимое файла
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        reader.close();
        assertEquals("Test Song;Test Artist", line);
        // Удаляем тестовый файл после проверки
        file.delete();
    }

    @Test
    void testLoadPlaylistFromFile() throws IOException {
        // Создаем тестовый файл
        BufferedWriter writer = new BufferedWriter(new FileWriter("bb.txt"));
        writer.write("Test Song;Test Artist");
        writer.newLine();
        writer.close();

        Playlist playlist = new Playlist("Test Playlist");
        playlist.loadFromFile("bb.txt");

        List<Song> songs = playlist.getSongs();
        assertEquals(1, songs.size());
        assertEquals("Test Song", songs.get(0).getTitle());
        assertEquals("Test Artist", songs.get(0).getArtist());

        // Удаляем тестовый файл после проверки
        new File("bb.txt").delete();
    }

    @Test
    void testCreatePlaylist() {
        MusicPlayer.createPlaylist("Test Playlist");
        assertNotNull(MusicPlayer.playlists.get(1));
        assertEquals("Test Playlist", MusicPlayer.playlists.get(1).getName());
    }

    @Test
    void testDeletePlaylist() {
        MusicPlayer.createPlaylist("New Playlist");
        MusicPlayer.deletePlaylist(1);
        assertNull(MusicPlayer.playlists.get(1));
    }

    @Test
    void testRemoveSongFromPlaylist() {
        Playlist playlist = new Playlist("Test Playlist");
        playlist.addSong(new Song("Test Song", "Test Artist"));
        MusicPlayer.playlists.put(1, playlist);

        MusicPlayer.removeSongFromPlaylist(1, 1);
        assertTrue(playlist.getSongs().isEmpty());
    }
}



