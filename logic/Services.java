package logic;

import inventory.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Services {

    public void createOkeyTiles(ArrayList<OkeyTile> okeyTiles, OkeyTile joker){
        Random random = new Random();
        int randomColorIndex = random.nextInt(4);
        int randomNumIndex = random.nextInt(13) + 1;

        String[] colors = {"sarı", "mavi", "siyah", "kırmızı"};

        String randColor = colors[randomColorIndex];
        
        joker.setNum(randomNumIndex);
        joker.setColor(randColor);

        int indicatorIndex = 0;
        for (String s : colors) {
            for (int i = 1; i <= 13; i++) {
                OkeyTile t1 = new OkeyTile(i, s);
                OkeyTile t2 = new OkeyTile(i, s);

                if(s.equals(randColor) && indicatorIndex == i){
                    t1.setIndicator();
                    t2.setIndicator();
                    indicatorIndex = 0;
                }

                if(s.equals(randColor) && randomNumIndex == i){
                    t1.setJoker();
                    t2.setJoker();
                    if(i == 1){
                        indicatorIndex = 13;
                    }
                    else{
                        okeyTiles.get(okeyTiles.size()-1).setIndicator();
                        okeyTiles.get(okeyTiles.size()-2).setIndicator();
                    }
                }

                okeyTiles.add(t1);
                okeyTiles.add(t2);
            }
        }

        okeyTiles.add(new OkeyTile(0, "fake-joker"));
        okeyTiles.add(new OkeyTile(0, "fake-joker"));
    }

    public void distributeOkeyTilesToPlayers(ArrayList<OkeyTile> okeyTiles, ArrayList<Player> players){
        Collections.shuffle(okeyTiles);

        int tileIndex = 0;
        boolean isIndicatorReserved = false;
        for (int i = 0; i < 4; i++) {
            Player p = new Player(i+1);
            
            int handSize = 14;
            if(i == 0){
                handSize = 15;
            }
            
            for (int j = 0; j < handSize; j++) {
                OkeyTile tmpTile = okeyTiles.get(tileIndex);
    
                if(tmpTile.isIndicator() && !isIndicatorReserved){
                    j = j == 0 ? 0 : j-1;
                    tileIndex++;
                    isIndicatorReserved = true;
                    continue;
                }

                p.addOkeyTileToHand(tmpTile);   
                tileIndex++;
            }

            players.add(p);
        }
    }

    
}
