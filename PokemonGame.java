class Type {
    static int FIRE = 0; 
    static int WATER = 1; 
    static int GRASS = 2; 
    static double[][] effectiveness = {
        {0.5, 0.5, 2.0}, 
        {2.0, 0.5, 0.5}, 
        {0.5, 2.0, 0.5} 
    }; 
}

abstract class Move {
    String name;
    int maxPP; 
    int remainingPP; 
    int type; 
    int priority; 
    static int SUCCESS = 0; 
    static int NO_PP = 1; 
    static int MISS = 2; 
    static int NO_EFFECT = 3; 
    static int IM_FAINTED = 4;
    static int TARGET_FAINTED = 5; 
    
    Move(String name, int type, int maxPP, int priority) {
        this.name = name; 
        this.type = type; 
        this.maxPP = maxPP; 
        remainingPP = maxPP; 
        this.priority = priority; 
    }
    
    int use(Pokemon user) {
        if (user.status == Pokemon.FAINTED) {
            return IM_FAINTED; 
        }
        
        if (remainingPP == 0) {
            return NO_PP; 
        }
        remainingPP--; 
        return SUCCESS; 
    }
}

abstract class TargetingMove extends Move {
    TargetingMove(String name, int type, int maxPP, int priority) {
        super(name, type, maxPP, priority);
    }
    
    int use(Pokemon user, Pokemon target) {
        int moveStatus = super.use(user); 
        if (moveStatus != SUCCESS) {
            return moveStatus; 
        }
        
        if (target.status == Pokemon.FAINTED) {
            return TARGET_FAINTED; 
        }
        
        if (Type.effectiveness[type][target.type] == 0) {
            return NO_EFFECT; 
        }
        
        return SUCCESS; 
    }
}

abstract class AttackingMove extends TargetingMove {
    int power;
    
    AttackingMove(String name, int type, int maxPP, int priority, int power) {
        super(name, type, maxPP, priority);
        this.power = power; 
    }
    
    int use(Pokemon user, Pokemon target) {
        int moveStatus = super.use(target); 
        if (moveStatus != SUCCESS) {
            return moveStatus; 
        }
        
        int movePower = (int) (power * user.attack * Type.effectiveness[type][target.type])/target.defense; 
        
        return SUCCESS; 
    }
}

abstract class StatusMove extends TargetingMove {
    StatusMove(String name, int type, int maxPP, int priority) { 
        super(name, type, maxPP, priority);
    }
}

abstract class Pokemon {
    int maxHealth; 
    int remainingHealth;
    int attack; 
    int defense; 
    int speed; 
    int type; 
    int status; 
    static int NO_STATUS = 0; 
    static int FAINTED = 1; 
    String name;
    Move[] moves = new Move[4]; 

    Pokemon(String name, int maxHealth, int attack, int defense, int speed, int type) {
        this.maxHealth = maxHealth; 
        this.remainingHealth = maxHealth; 
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.name = name; 
        this.type = type; 
    }
    
    void takeDamage(int amount) {
        if (remainingHealth <= amount) {
            status = FAINTED; 
            remainingHealth = 0; 
        }
        
        remainingHealth -= amount; 
    }
}

class Charmander extends Pokemon {
    Charmander(String name) {
        super(name, 39, 52, 43, 65, Type.FIRE);
        moves[0] = new AttackingMove("Ember1", Type.FIRE, 40, 0, 40); 
        moves[1] = new AttackingMove("Ember2", Type.FIRE, 40, 0, 40); 
        moves[2] = new AttackingMove("Ember3", Type.FIRE, 40, 0, 40); 
        moves[3] = new AttackingMove("Ember4", Type.FIRE, 40, 0, 40); 
    }
}

class Bulbasaur extends Pokemon {
    Bulbasaur(String name) {
        super(name, 45, 49, 49, 45, Type.GRASS);
    }
}

class Squirtle extends Pokemon {
    Squirtle(String name) {
        super(name, 44, 48, 65, 43, Type.WATER);
    }
}

class Player {
    Pokemon[] team = new Pokemon[6]; 
    
}

class Game {
    Player[] players = new Player[2]; 
    
    Game() {
        players[0].team[0] = new Charmander("Charmander1"); 
        players[1].team[0] = new Charmander("Charmander2"); 
    }
    
    void start() {
        
    }
    
    int choosePokemon(int index) {
        for (int )
    }
}

public class Main {
	public static void main(String[] args) {

	}
}
