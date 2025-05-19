package logic;
import inventory.*;
import java.util.ArrayList;

public class Game {
    private ArrayList<OkeyTile> okeyTiles;
    private ArrayList<Player> players;
    private OkeyTile joker;

    public Game(){
        this.players = new ArrayList<>(4);
        this.okeyTiles = new ArrayList<>(106);
        this.joker = new OkeyTile(-1, null);
    }

    public void initGame(){
        Services service = new Services();
        service.createOkeyTiles(this.okeyTiles, this.joker);// sets joker
        service.distributeOkeyTilesToPlayers(this.okeyTiles, this.players);
    }

    public void evaluateGame(){
        OkeyHandEvaluater evaluater = new OkeyHandEvaluater(this.joker);
        System.out.println("Joker:" + this.joker.getColor() + "-" + this.joker.getNum());
        for (Player p : this.players) {
            System.out.println("Player id:" + p.getId());
            System.out.println("Player Hand:");
            p.displayHand();
            int score = evaluater.calculateScore(p.getHand());
            p.setScore(score);
        }
    }
}
