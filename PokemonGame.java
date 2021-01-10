import java.util.*;

class Utility {
    static boolean roll(int n) {
        if (Math.random() * 100 < n) {
            return true; 
        }
        return false; 
    }
    
    static int rng(int n, int m) {
        return (int)(Math.random() * (m - n + 1) + n);
    }
}

class Type {
    static int NO_TYPE = 0; 
    static int FIRE = 1; 
    static int WATER = 2; 
    static int GRASS = 3; 
    static int NORMAL = 4; 
    static double[][] effectiveness = {
        {1.0, 1.0, 1.0, 1.0, 1.0}, 
        {1.0, 0.5, 0.5, 2.0, 1.0}, 
        {1.0, 2.0, 0.5, 0.5, 1.0}, 
        {1.0, 0.5, 2.0, 0.5, 1.0}, 
        {1.0, 1.0, 1.0, 1.0, 1.0} 
    }; 
    
    static String[] typeString = {"NO TYPE", "FIRE", "WATER", "GRASS", "NORMAL"}; 
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
    static int IS_ASLEEP = 6; 
    
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
        
        if (user.status == Pokemon.ASLEEP) {
            return IS_ASLEEP;
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
    int statusEffect; 
    int accuracy; 
    
    AttackingMove(String name, int type, int maxPP, int priority, int power, int statusEffect, int accuracy) {
        super(name, type, maxPP, priority);
        this.power = power; 
        this.statusEffect = statusEffect; 
        this.accuracy = accuracy; 
    }
    
    int use(Pokemon user, Pokemon target) {
        int moveStatus = super.use(user, target); 
        if (moveStatus != SUCCESS) {
            return moveStatus; 
        }
        
        if (accuracy != -1 && !Utility.roll(accuracy)) {
            return MISS; 
        }
        
        target.inflictStatus(statusEffect); 
        
        int movePower = (int) (power * user.attack * Type.effectiveness[type][target.type])/target.defense; 
        target.takeDamage(movePower); 
        
        return SUCCESS; 
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
    static int ASLEEP = 2; 
    static int BURN = 3; 
    String name;
    Move[] moves = new Move[5]; 
    int sleepCounter = 0; 
    
    private String[] statusString = {"", "Fainted", "Sleep", "Burn"}; 

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
    
    void inflictStatus(int statusEffect) {
        if (type == Type.FIRE && statusEffect == BURN) {
            return; 
        }
        if ((statusEffect == ASLEEP && status != ASLEEP) || status == NO_STATUS) {
            status = statusEffect; 
            if (status == ASLEEP) {
                sleepCounter = Utility.rng(2, 4);
            }
        }
    }
    
    void onTurnEnd() {
        if (status == ASLEEP) {
            sleepCounter--; 
            if (sleepCounter == 0) {
                status = Pokemon.NO_STATUS; 
            }
        } else if (status == BURN) {
            takeDamage(maxHealth / 8);
        }
    }
    
    public String toString() {
        return name + " (" + statusString[status] + ") " + remainingHealth + "/" + maxHealth; 
    }
}

class Charmander extends Pokemon {
    Charmander(String name) {
        super(name, 39, 52, 43, 65, Type.FIRE);
        moves[1] = new AttackingMove("Ember 100%", Type.FIRE, 25, 0, 40, Pokemon.NO_STATUS, 100); 
        moves[2] = new AttackingMove("Ember 50%", Type.FIRE, 25, 0, 40, Pokemon.NO_STATUS, 50); 
        moves[3] = new AttackingMove("Ember 0%", Type.FIRE, 25, 0, 40, Pokemon.NO_STATUS, 0); 
        moves[4] = new AttackingMove("Will-O-Wisp", Type.FIRE, 25, 0, 0, Pokemon.BURN, 85); 
    }
}

class Bulbasaur extends Pokemon {
    Bulbasaur(String name) {
        super(name, 45, 49, 49, 45, Type.GRASS);
        moves[1] = new AttackingMove("Vine Whip1", Type.GRASS, 25, 0, 45, Pokemon.NO_STATUS, 100);
        moves[2] = new AttackingMove("Vine Whip2", Type.GRASS, 25, 0, 45, Pokemon.NO_STATUS, 100);
        moves[3] = new AttackingMove("Vine Whip3", Type.GRASS, 25, 0, 45, Pokemon.NO_STATUS, 100);
        moves[4] = new AttackingMove("Sleep Powder", Type.GRASS, 15, 0, 0, Pokemon.ASLEEP, 75);
    }
}

class Squirtle extends Pokemon {
    Squirtle(String name) {
        super(name, 44, 48, 65, 43, Type.WATER);
        moves[1] = new AttackingMove("Quick Attack1", Type.NORMAL, 30, 1, 40, Pokemon.NO_STATUS, 100); 
        moves[2] = new AttackingMove("Quick Attack2", Type.NORMAL, 30, 1, 40, Pokemon.NO_STATUS, 100); 
        moves[3] = new AttackingMove("Quick Attack3", Type.NORMAL, 30, 1, 40, Pokemon.NO_STATUS, 100); 
        moves[4] = new AttackingMove("Quick Attack 0 Damage", Type.NORMAL, 30, 1, -1, Pokemon.NO_STATUS, 100); 
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
        players[0].team[2] = new Squirtle("Squirtle1"); 
        players[0].team[3] = new Bulbasaur("Bulbasaur1"); 
        players[1].team[0] = new Charmander("Charmander2.0"); 
        players[1].team[1] = new Charmander("Charmander2.1"); 
        players[1].team[2] = new Squirtle("Squirtle2"); 
        players[1].team[3] = new Bulbasaur("Bulbasaur2");
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
            activePokemon[0].onTurnEnd();
            activePokemon[1].onTurnEnd();
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
