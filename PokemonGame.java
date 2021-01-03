import java.util.*;

class Utility {
    static boolean roll(int n) {
        if (Math.random() * 100 < n) {
            return true; 
        }
        return false; 
    }
}

class Type {
    static int NO_TYPE = 0; 
    static int FIRE = 1; 
    static int WATER = 2; 
    static int GRASS = 3; 
    static double[][] effectiveness = {
        {1.0, 1.0, 1.0, 1.0}, 
        {1.0, 0.5, 0.5, 2.0}, 
        {1.0, 2.0, 0.5, 0.5}, 
        {1.0, 0.5, 2.0, 0.5} 
    }; 
    
    static String[] typeString = {"NO TYPE", "FIRE", "WATER", "GRASS"}; 
}

class Move {
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
    
    int use(Pokemon user, Pokemon target) {
        if (user.status == Pokemon.FAINTED) {
            return IM_FAINTED; 
        }
        
        if (remainingPP == 0) {
            return NO_PP; 
        }
        remainingPP--; 
        return SUCCESS; 
    }
    
    public String toString() {
        return name + " (" + Type.typeString[type] + ") (" + priority + ") " + "PP: " + remainingPP + "/" + maxPP;
    }
}

abstract class TargetingMove extends Move {
    TargetingMove(String name, int type, int maxPP, int priority) {
        super(name, type, maxPP, priority);
    }
    
    int use(Pokemon user, Pokemon target) {
        int moveStatus = super.use(user, target); 
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

class AttackingMove extends TargetingMove {
    int power;
    
    AttackingMove(String name, int type, int maxPP, int priority, int power) {
        super(name, type, maxPP, priority);
        this.power = power; 
    }
    
    int use(Pokemon user, Pokemon target) {
        int moveStatus = super.use(user, target); 
        if (moveStatus != SUCCESS) {
            return moveStatus; 
        }
        
        int movePower = (int) (power * user.attack * Type.effectiveness[type][target.type])/target.defense; 
        target.takeDamage(movePower); 
        
        return SUCCESS; 
    }
}

class StatusMove extends TargetingMove {
    StatusMove(String name, int type, int maxPP, int priority) { 
        super(name, type, maxPP, priority);
    }
}

class SwitchMove extends Move {
    SwitchMove() {
        super("Switch", Type.NO_TYPE, -1, 6);
    }
    
    int use(Pokemon user, Pokemon target, int player) {
        Game.choosePokemon(player); 
        return SUCCESS; 
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
    Move[] moves = new Move[5]; 
    
    private String[] statusString = {"", "Fainted"}; 

    Pokemon(String name, int maxHealth, int attack, int defense, int speed, int type) {
        this.maxHealth = maxHealth; 
        this.remainingHealth = maxHealth; 
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.name = name; 
        this.type = type; 
        moves[0] = new SwitchMove();
    }
    
    void takeDamage(int amount) {
        if (remainingHealth <= amount) {
            status = FAINTED; 
            remainingHealth = 0; 
            return; 
        }
        
        remainingHealth -= amount; 
    }
    
    public String toString() {
        return name + " (" + statusString[status] + ") " + remainingHealth + "/" + maxHealth; 
    }
}

class Charmander extends Pokemon {
    Charmander(String name) {
        super(name, 39, 52, 43, 65, Type.FIRE);
        moves[1] = new AttackingMove("Ember1", Type.FIRE, 40, 0, 40); 
        moves[2] = new AttackingMove("Ember2", Type.FIRE, 40, 0, 40); 
        moves[3] = new AttackingMove("Ember3", Type.FIRE, 40, 0, 40); 
        moves[4] = new AttackingMove("Ember4", Type.FIRE, 40, 0, 40); 
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
    static Player[] players = new Player[2]; 
    static Pokemon[] activePokemon = new Pokemon[2]; 
    static Move[] queuedMove = new Move[2];
    static int[] numAlivePokemon = new int[2];
    
    static private Scanner scanner = new Scanner(System.in);
    
    Game() {
        players[0] = new Player(); 
        players[1] = new Player(); 
        players[0].team[0] = new Charmander("Charmander1.0"); 
        players[0].team[1] = new Charmander("Charmander1.1"); 
        players[0].team[2] = new Charmander("Charmander1.2"); 
        players[0].team[3] = new Charmander("Charmander1.3"); 
        players[1].team[0] = new Charmander("Charmander2.0"); 
        players[1].team[1] = new Charmander("Charmander2.1"); 
        players[1].team[2] = new Charmander("Charmander2.2"); 
        players[1].team[3] = new Charmander("Charmander2.3");
        numAlivePokemon[0] = 4; 
        numAlivePokemon[1] = 4;
    }
    
    void start() {
        choosePokemon(0); 
        choosePokemon(1); 
        while (true) {
            System.out.println(activePokemon[0]);
            System.out.println(activePokemon[1]);
            displayActivePokemon(0); 
            chooseMove(0);
            displayActivePokemon(1); 
            chooseMove(1); 
            resolveMoves();
            for (int i = 0; i < 2; i++) {
                if (activePokemon[i].status == Pokemon.FAINTED) {
                    numAlivePokemon[i]--; 
                }
            }
            if (numAlivePokemon[0] == 0 && numAlivePokemon[1] == 0) {
                System.out.println("Its a draw. "); 
                return; 
            } else if (numAlivePokemon[0] == 0) {
                System.out.println("Player 2 wins! "); 
                return; 
            } else if (numAlivePokemon[1] == 0) {
                System.out.println("Player 1 wins! ");
                return; 
            } 
            for (int i = 0; i < 2; i++) {
                if (activePokemon[i].status == Pokemon.FAINTED) {
                    choosePokemon(i); 
                }
            }
        }
    }
    
    static void choosePokemon(int index) {
        while (true) {
            System.out.println("Choose a Pokemon: ");
            for (int i = 0; i < players[index].team.length; i++) {
                if (players[index].team[i] == null) {
                    break; 
                }
                System.out.println((i + 1) + ": " + players[index].team[i]); 
            }
            int pokemonIndex = Integer.parseInt(scanner.nextLine()) - 1; 
            
            if (players[index].team[pokemonIndex] == null) {
                System.out.println("Invalid index. ");
                continue; 
            }
            if (players[index].team[pokemonIndex].status == Pokemon.FAINTED) {
                System.out.println("That Pokemon is fainted. "); 
                continue; 
            }
            activePokemon[index] = players[index].team[pokemonIndex]; 
            return; 
        }
    }
    
    void displayActivePokemon(int index) {
        System.out.println(activePokemon[index]);
        for (int i = 0; i < 5; i++) {
            System.out.println(i + ": " + activePokemon[index].moves[i]);
        }
    }
    
    void chooseMove(int index) {
        System.out.println("Choose a move: "); 
        int moveIndex = Integer.parseInt(scanner.nextLine()); 
        queuedMove[index] = activePokemon[index].moves[moveIndex];
    }
    
    void resolveMoves() {
        int first; 
        int second; 
        
        if (queuedMove[0].priority > queuedMove[1].priority) {
            first = 0; 
            second = 1; 
        } else if (queuedMove[1].priority > queuedMove[0].priority) {
            first = 1; 
            second = 0; 
        } else {
            if (activePokemon[0].speed > activePokemon[1].speed) {
                first = 0; 
                second = 1; 
            } else if (activePokemon[1].speed > activePokemon[0].speed) {
                first = 1; 
                second = 0; 
            } else if (Utility.roll(50)) {
                first = 0;
                second = 1; 
            } else {
                first = 1; 
                second = 0;
            }
        }
        
        int status; 
        
        if (queuedMove[first] instanceof SwitchMove) {
            status = ((SwitchMove) queuedMove[first]).use(activePokemon[first], activePokemon[second], first);
        } else {
            status = queuedMove[first].use(activePokemon[first], activePokemon[second]);
        }
        
        if (status != Move.SUCCESS) {
            System.out.println("The move was not successful. ");
        }
        
        if (queuedMove[second] instanceof SwitchMove) {
            status = ((SwitchMove) queuedMove[second]).use(activePokemon[second], activePokemon[first], second);
        } else {
            status = queuedMove[second].use(activePokemon[second], activePokemon[first]);
        }
        
        if (status != Move.SUCCESS) {
            System.out.println("The move was not successful. ");
        }
    }
}

public class Main {
	public static void main(String[] args) {
        Game game = new Game(); 
        game.start(); 
	}
}
