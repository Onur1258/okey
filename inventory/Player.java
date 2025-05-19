package inventory;

import java.util.ArrayList;

public class Player {
    private int id;
    private ArrayList<OkeyTile> hand;
    private int score;

    public Player(int id){
        this.id = id;
        this.hand = new ArrayList<>();
        this.score = 0;
    }

    public int getId(){
        return this.id;
    }

    public ArrayList<OkeyTile> getHand(){
        return hand;
    }

    public void addOkeyTileToHand(OkeyTile ot){
        hand.add(ot);
    }

    public int getScore(){
        return score;
    }
    
    public void setScore(int score){
        this.score = score;
    }

    public void displayHand() {
        for (OkeyTile tile : this.hand) {
            System.out.println(tile.getColor() + "-" + tile.getNum() + ", Is Joker:" + tile.isJoker() + ", Is Indicator:" + tile.isIndicator());
        }
    }
}
