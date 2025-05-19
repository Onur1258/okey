package logic;
import inventory.*;
import java.util.ArrayList;

public class Game {
    private Services service;
    private ArrayList<OkeyTile> okeyTiles;
    private ArrayList<Player> players;

    public Game(){
        this.service = new Services();
        this.players = new ArrayList<>(4);
        this.okeyTiles = new ArrayList<>(106);
    }

    public void initGame(){
        this.service.createOkeyTiles(this.okeyTiles);

        int tileIndex = 0;
        boolean isIndicatorReserved = false;
        for (int i = 0; i < 4; i++) {
            Player p = new Player();
            
            int handSize = 14;
            if(i == 0){
                handSize = 15;
            }
            
            for (int j = 0; j < handSize; j++) {
                OkeyTile tmpTile = this.okeyTiles.get(tileIndex);
    
                if(tmpTile.isIndicator() && !isIndicatorReserved){
                    j = j == 0 ? 0 : j-1;
                    tileIndex++;
                    isIndicatorReserved = true;
                    continue;
                }

                p.addOkeyTileToHand(tmpTile);   
                
                
                tileIndex++;
            }
            this.players.add(p);
        }
        System.out.println(tileIndex);
        int x = 1;
        for (Player p : this.players) {
            System.out.println("Player-" + x);
            p.displayHand();

            System.out.println("--------------------");
            x++;
        }
    }
}
