import java.util.ArrayList;
import java.util.Scanner;

class Utility {
    static int randInt(int min, int max) {
        return (int)(min + Math.random() * (max - min));
    }
}

class Property {
    int value; 
    ArrayList<String> stringValues = new ArrayList<String>();
    
    Property(int value) {
        this.value = value;
    }
    
    public String toString() {
        return this.getClass().getName().replace("$", " ");
    }
}

/* class Colour extends Property {
    Colour.Code colour;
    
    enum Code {
        BROWN, 
        BLACK,
        GREEN,
        BLUE;
    }
    
    Colour(Colour.Code colour) {
        this.colour = colour; 
    }
} */

/*class Size extends Property {
    enum Code {
        LARGE,
        MEDIUM,
        SMALL;
    }
}*/

class Trait {
    ArrayList<Property> properties = new ArrayList<Property>(); 
    
    public String toString() {
        return this.getClass().getName();
    }
}

class Eye extends Trait {
    
    public static class Colour extends Property {
        public enum Code {
            BROWN, 
            BLACK,
            GREEN,
            BLUE;
        }
        
        Colour(int colour) {
            super(colour);
            for (Code code: Code.values()) {
                stringValues.add(code.toString()); 
            }
        }
    }
    public static class Size extends Property {
        public enum Code {
            LARGE, 
            MEDIUM, 
            SMALL;
        }
        
        Size(int size) {
            super(size);
            for (Code code: Code.values()) {
                stringValues.add(code.toString()); 
            }
        }
    }
    
    Eye(int colour, int size) {
        properties.add(new Colour(colour)); 
        properties.add(new Size(size)); 
    }
}

class Name extends Trait {
    String name; 
    
    Name(String name) {
        this.name = name; 
    }
}

class Hair extends Trait {
    
    public static class Colour extends Property {
        public enum Code {
            BROWN, 
            BLACK,
            BLONDE,
            WHITE, 
            GRAY;
        }
        
        Colour(int colour) {
            super(colour); 
            for (Code code: Code.values()) {
                stringValues.add(code.toString()); 
            }
        }
    }
    public static class Length extends Property {
        public enum Code {
            LONG, 
            MEDIUM, 
            SHORT;
        }
        
        Length(int length) {
            super(length);
            for (Code code: Code.values()) {
                stringValues.add(code.toString()); 
            }
        }
    }
    
    Hair(int colour, int length) {
        properties.add(new Colour(colour));  
        properties.add(new Length(length)); 
    }
}

class Gender extends Trait {
    public static class GenderProperty extends Property {
        public enum Code {
            MALE, 
            FEMALE; 
        }
        
        GenderProperty(int gender) {
            super(gender);
            for (Code code: Code.values()) {
                stringValues.add(code.toString()); 
            }
        }
    }
    
    Gender(int gender) {
        properties.add(new GenderProperty(gender)) ;
    }
}

class Person {
    ArrayList<Trait> traits = new ArrayList<Trait>(); 
    
    /*Person(String name, boolean gender, Hair.Colour.Code hairColour, Hair.Length.Code hairLength, Eye.Colour.Code eyeColour, Eye.Size.Code eyeSize) {
        traits.add(new Name(name)); 
        traits.add(new Gender(gender)); 
        traits.add(new Hair(hairColour, hairLength)); 
        traits.add(new Eye(eyeColour, eyeSize));
    }*/
    
    Person(String name) {
        traits.add(new Name(name)); 
        traits.add(new Gender(Utility.randInt(0, 2))); 
        traits.add(new Hair(Utility.randInt(0, Hair.Colour.Code.values().length), Utility.randInt(0, Hair.Length.Code.values().length))); 
        traits.add(new Eye(Utility.randInt(0, Eye.Colour.Code.values().length), Utility.randInt(0, Eye.Size.Code.values().length)));
    }
}

class Game {
    Scanner scanner = new Scanner(System.in); 
    ArrayList<ArrayList<Person>> players = new ArrayList<ArrayList<Person>>(2);
    
    Game() {
        players.add(new ArrayList<Person>(25)); 
        players.add(new ArrayList<Person>(25)); 
        
        for (int i = 0; i < 25; i++) {
            // create then copy the person, add to both players' board
            players.get(0).add("Person " + i);
        }
    }
    
    void ask() {
        System.out.println("Ask about: ");
        int i = 0; 
        for (Trait t: person.traits) {
            System.out.println(i + ": " + t);
            i++; 
        }
        i = 0; 
        int input = Integer.parseInt(scanner.nextLine());
        Trait trait = person.traits.get(input);
        System.out.println("Ask about the " + trait + "'s:");
        for (Property p: trait.properties) {
            System.out.println(i + ": " + p); 
            i++;
        }
        i = 0; 
        input = Integer.parseInt(scanner.nextLine());
        System.out.println("Is the " + trait.properties.get(input) + ": ");
        for (String s: trait.properties.get(input).stringValues) { 
            System.out.println(i + ": " + s); 
            i++;
        }
        i = 0;
        input = Integer.parseInt(scanner.nextLine());
    }
    
    void start() {
        ask();
    }
}

public class Main
{
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
}
