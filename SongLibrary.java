import java.util.ArrayList;

class Time {
    int minutes;
    int seconds;
    
    Time(String time) {
        minutes = Integer.parseInt (time.substring(0, 2)); 
        seconds = Integer.parseInt (time.substring(3, 5));
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
        return minutes + ":" + seconds; 
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
    
    void print() {
        System.out.println(title + "\t" + artist + "\t" + duration);
    }
}

class Playlist {
    ArrayList <Song> songs; 
    Time duration; 
    
    Playlist() {
        songs = new ArrayList <Song>();
        duration = new Time("00:00");
    }
    
    void addSong(Song song) {
        songs.add(song); 
        duration.add(song.duration);
    }
    
    void removeSong(int index) {
        duration.subtract(songs.get(index).duration);
        songs.remove(index); 
    }
    
    void print() {
        for (Song song: songs) {
            song.print(); 
        }
    }
}

class Library {
    
}

public class SongLibrary
{
	public static void main(String[] args) {
		Playlist playlist = new Playlist(); 
		playlist.addSong(new Song("title", "artist", "03:12"));
		playlist.print(); 
	}
}
