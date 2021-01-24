import java.util.ArrayList;

class Property {
    
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

class Size extends Property {
    enum Code {
        LARGE,
        MEDIUM,
        SMALL;
    }
}

class Trait {
    
}

class Eye extends Trait {
    Colour colour; 
    Size size; 
    
    public static class Colour extends Property {
        Colour.Code colour;
        
        public enum Code {
            BROWN, 
            BLACK,
            GREEN,
            BLUE;
        }
        
        Colour(Colour.Code colour) {
            this.colour = colour; 
        }
    }
    
    Eye(Colour.Code colour, Size.Code size) {
        this.colour = new Colour(colour); 
        this.size = new Size(size); 
    }
}

class Name extends Trait {
    String name; 
    
    Name(String name) {
        this.name = name; 
    }
}

class Hair extends Trait {
    Colour colour; 
    Length length; 
    
    public static class Colour extends Property {
        Colour.Code colour;
        
        public enum Code {
            BROWN, 
            BLACK,
            GREEN,
            BLUE;
        }
        
        Colour(Colour.Code colour) {
            this.colour = colour; 
        }
    }
    public static class Size extends Property {
        Size.Code size; 
        
        //enum here
    }
    public static class Length extends Property {
        public enum Code {
            LONG, 
            MEDIUM, 
            SHORT;
        }
        
        Length(Length.Code length) {
            this.length = new Length(length); 
        }
    }
    
    Hair(Colour.Code colour, Length.Code length) {
        this.colour = new Colour(colour); 
        this.length = new Length(length); 
    }
}

class Gender extends Trait {
    boolean gender; 
    
    Gender(boolean gender) {
        this.gender = gender; 
    }
}

class Person {
    Gender gender; 
    Eye eye;
    Name name; 
    Hair hair; 
    
    Person(String name, boolean gender, Hair.Colour.Code hairColour, Eye.Colour.Code eyeColour) {
        this.name = new Name(name); 
        this.gender = new Gender(gender);
        //this.hair = new Hair(hairColour); 
        //this.eye = new Eye(eyeColour);
    }
}

class Game {
    void start() {
        
    }
}

public class GuessWho
{
	public static void main(String[] args) {
		System.out.println(Eye.Colour.Code.BROWN);
	}
}
