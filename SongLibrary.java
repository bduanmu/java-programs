import java.util.ArrayList;
import java.util.Scanner;
import java.util.Comparator;

class Time {
    int minutes;
    int seconds;
    
    Time(String time) {
        minutes = Integer.parseInt (time.substring(0, 2)); 
        seconds = Integer.parseInt (time.substring(3, 5));
    }
    
    Time(Time time) {
        minutes = time.minutes; 
        seconds = time.seconds; 
    }
    
    void add(Time time) {
        this.minutes = this.minutes + time.minutes + (this.seconds + time.seconds)/60; 
        this.seconds = (this.seconds + time.seconds) % 60; 
    }
    
    void subtract(Time time) {
        if (this.seconds < time.seconds) {
            this.seconds += 60; 
            this.minutes--;
        }
        this.seconds -= time.seconds; 
        this.minutes -= time.minutes; 
    }
    
    public String toString() {
        return String.format("%02d:%02d", minutes, seconds);
    }
}

class Song {
    String title;
    String artist;
    Time duration; 
    
    Song(String title, String artist, String duration) {
        this.title = title; 
        this.artist = artist; 
        this.duration = new Time(duration); 
    }
    
    public String toString() {
        return String.format("%-7s", title) + "\t" + artist + "\t" + duration;
    }
}

class Playlist {
    String name; 
    ArrayList <Song> songs; 
    Time duration; 
    
    Playlist(String name) {
        this.name = name;
        songs = new ArrayList <Song>();
        duration = new Time("00:00");
    }
    
    Playlist(Playlist playlist) {
        name = playlist.name + "-Copy"; 
        songs = new ArrayList <Song> (playlist.songs); 
        duration = new Time(playlist.duration); 
    }
    
    void addSong(Song song) {
        songs.add(song); 
        duration.add(song.duration);
    }
    
    void removeSong(int index) {
        duration.subtract(songs.get(index).duration);
        songs.remove(index); 
    }
    
    String getName() {
        return name.toUpperCase(); 
    }
    
    void print() {
        System.out.println(name + "\t" + duration);
        int i = 1;
        System.out.println("Song Title\tArtist\tDuration");
        for (Song song: songs) {
            System.out.println(i + ". " + song); 
            i++;
        }
    }
}

class Library {
    ArrayList <Playlist> playlists; 
    
    Library() {
        playlists = new ArrayList <Playlist>(); 
    }
    
    void createNewPlaylist(String name) {
        Playlist playlist = new Playlist(name); 
        playlists.add(playlist); 
    }
    
    void listAllPlaylists() {
        int i = 1; 
        if (playlists.size() == 0) {
            System.out.println("Library is empty. "); 
        } 
        for (Playlist playlist: playlists) {
            System.out.println(i + ". " + String.format("%-10s", playlist.name) + "\t" + playlist.duration);
            i++; 
        }
    }
    
    void deletePlaylist(int index) {
        playlists.remove(index);
    }
    
    void copyPlaylist(int index) {
        Playlist playlistCopy = new Playlist(playlists.get(index));
        playlists.add(playlistCopy); 
    }
    
    void sortByName() {
        playlists.sort(Comparator.comparing(Playlist::getName));
    }
    
    void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            listAllPlaylists();
            System.out.println("Press \"a\" to create new playlist, \"d\" to delete a playlist, \"c\" to copy a playlist and \"s\" to sort the playlists. Type the index of a playlist to open it. To exit the program, press \"q\"");
            String input = scanner.nextLine(); 
            if (input.equals("q")) {
                break;
            } else if (input.equals("a")) {
                System.out.print("Input the name of your playlist: ");
                input = scanner.nextLine(); 
                createNewPlaylist(input); 
            } else if (input.equals("d")) {
                System.out.print("Input the index of the playlist you want to delete: ");
                input = scanner.nextLine();
                deletePlaylist(Integer.parseInt(input) - 1); 
            } else if (input.equals("c")) {
                System.out.print("Input the index of the playlist you want to copy: ");
                input = scanner.nextLine();
                copyPlaylist(Integer.parseInt(input) - 1); 
            } else if (input.equals("s")) {
                sortByName();
            } else {
                Playlist playlist = playlists.get(Integer.parseInt(input) - 1);
                while (true) {
                    playlist.print(); 
                    System.out.println("Press \"a\" to add a song and \"d\" to remove a song. Press \"q\" to go back to the library. ");
                    String songInput = scanner.nextLine();
                    if (songInput.equals("a")) {
                        System.out.print("Input the name of your song: ");
                        String songName = scanner.nextLine(); 
                        System.out.print("Input the artist of your song: ");
                        String songArtist = scanner.nextLine(); 
                        System.out.print("Input the duration of your song in this format: \"MM:SS\": ");
                        String songDuration = scanner.nextLine(); 
                        playlist.addSong(new Song(songName, songArtist, songDuration));
                    } else if (songInput.equals("d")) {
                        System.out.print("Input the index of the song you want to delete: ");
                        songInput = scanner.nextLine();
                        playlist.removeSong(Integer.parseInt(songInput) - 1);
                    } else if (songInput.equals("q")) {
                        break;
                    }
                }
            }
        }
    }
}

public class SongLibrary
{
	public static void main(String[] args) {
		Library library = new Library(); 
		library.start(); 
	}
}
