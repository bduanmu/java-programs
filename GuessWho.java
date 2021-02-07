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
    public static class Value extends Property {
        public enum Code {
            MALE, 
            FEMALE; 
        }
        
        Value(int gender) {
            super(gender);
            for (Code code: Code.values()) {
                stringValues.add(code.toString()); 
            }
        }
    }
    
    Gender(int gender) {
        properties.add(new Value(gender));
    }
}

class Shirt extends Trait {
    public static class Colour extends Property {
        public enum Code {
            BLACK, 
            WHITE,
            RED,
            BLUE, 
            YELLOW, 
            GREEN, 
            PINK;
        }
        
        Colour(int colour) {
            super(colour); 
            for (Code code: Code.values()) {
                stringValues.add(code.toString()); 
            }
        }
    }
    
    Shirt(int colour) {
        properties.add(new Colour(colour));
    }
}

class Person {
    ArrayList<Trait> traits = new ArrayList<Trait>(); 
    boolean faceUp = true;
    
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
        traits.add(new Shirt(Utility.randInt(0, Shirt.Colour.Code.values().length)));
    }
    
    Person(Person person) {
        traits = person.traits; 
        faceUp = person.faceUp; 
    }
    
    public String toString() {
        String string = ""; 
        for (Trait t: traits) {
            string += t.toString() + ": ";
            if (t instanceof Name) {
                string += ((Name) t).name; 
            }
            for (Property p: t.properties) {
                string += p.stringValues.get(p.value) + ", ";
            }
            string += "\n";
        }
        
        return string;
    }
}

class Game {
    Scanner scanner = new Scanner(System.in); 
    ArrayList<ArrayList<Person>> players = new ArrayList<ArrayList<Person>>(2);
    int[] numFaceUp = new int[2]; 
    Person[] chosenPerson = new Person[2]; 
    private final int CORRECT = 0; 
    private final int INCORRECT = 1; 
    private final int WIN = 2; 
    
    Game() {
        players.add(new ArrayList<Person>(25)); 
        players.add(new ArrayList<Person>(25)); 
        
        for (int i = 0; i < 25; i++) {
            players.get(0).add(new Person("" + i));
            players.get(1).add(new Person(players.get(0).get(i)));
        }
        
        chosenPerson[0] = players.get(0).get(Utility.randInt(0, 25)); 
        chosenPerson[1] = players.get(1).get(Utility.randInt(0, 25)); 
        numFaceUp[0] = 25; 
        numFaceUp[1] = 25; 
    }
    
    int ask(int turn) {
        int opponent = (turn + 1) % 2; 
        
        System.out.println("Ask about: ");
        int i = 0; 
        for (Trait t: chosenPerson[opponent].traits) {
            System.out.println(i + ": " + t);
            i++; 
        }
        i = 0; 
        int traitIndex = Integer.parseInt(scanner.nextLine());
        Trait trait = chosenPerson[opponent].traits.get(traitIndex);
        if (trait instanceof Name) {
            System.out.println("What is your guess?");
            String nameGuess = scanner.nextLine();
            if (((Name) trait).name.equals(nameGuess)) {
                return WIN; 
            }
            for (Person person: players.get(turn)) {
                if (((Name) person.traits.get(0)).name.equals(nameGuess)) {
                    if (person.faceUp) {
                        person.faceUp = false;
                        numFaceUp[turn]--;
                    }
                    return INCORRECT; 
                }
            }
            
            return INCORRECT;
        }
        System.out.println("Ask about the " + trait + "'s:");
        for (Property p: trait.properties) {
            System.out.println(i + ": " + p); 
            i++;
        }
        i = 0; 
        int propertyIndex = Integer.parseInt(scanner.nextLine());
        System.out.println("Is the " + trait.properties.get(propertyIndex) + ": ");
        for (String s: trait.properties.get(propertyIndex).stringValues) { 
            System.out.println(i + ": " + s); 
            i++;
        }
        i = 0;
        int propertyValue = Integer.parseInt(scanner.nextLine()); 
        
        if (chosenPerson[opponent].traits.get(traitIndex).properties.get(propertyIndex).value == propertyValue) {
            for (Person person: players.get(turn)) {
                if (person.traits.get(traitIndex).properties.get(propertyIndex).value != propertyValue) {
                    if (person.faceUp) {
                        person.faceUp = false; 
                        numFaceUp[turn]--;
                    }
                }
            }
            return CORRECT;
        } else {
            for (Person person: players.get(turn)) {
                if (person.traits.get(traitIndex).properties.get(propertyIndex).value == propertyValue) {
                    if (person.faceUp) {
                        person.faceUp = false; 
                        numFaceUp[turn]--;
                    }
                }
            }
            return INCORRECT; 
        }
    }
    
    void start() {
        int turn = 0;
        while (true) {
            printPeople(turn); 
            System.out.println("It is Player " + turn + "'s turn. ");
            System.out.println(numFaceUp[turn] + " people remain. ");
            int result = ask(turn); 
            if (result == WIN || numFaceUp[turn] == 1) {
                System.out.println("Player " + turn + " wins! ");
                return;
            }
            turn = (turn + 1) % 2;
        }
    }
    
    void printPeople(int index) {
        for (Person p: players.get(index)) {
            if (p.faceUp) {
                System.out.println(p); 
            }
        }
    }
}

public class Main
{
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
}
