package logic;
import java.util.ArrayList;
import java.util.Collections;

import inventory.OkeyTile;

public class OkeyHandEvaluater {
    OkeyTile joker;

    public OkeyHandEvaluater(OkeyTile joker){
        this.joker = joker;
    }

    public int calculateScore(ArrayList<OkeyTile> hand){
        ArrayList<OkeyTile> leftovers = new ArrayList<OkeyTile>(hand);

        int pairModeUsedTiles = hand.size() - this.piarMode(leftovers);
        
        return 0;
    }

    private int piarMode(ArrayList<OkeyTile> leftovers){
        for (int i = leftovers.size()-1; i > 1; i--) {
            for (int j = i-1; j > 0; j--) {
                if(this.isValidPair(leftovers.get(i), leftovers.get(j))){
                    leftovers.remove(i);
                    leftovers.remove(j);
                    i = leftovers.size() - 1;
                    break;
                }
            }
        }

        return leftovers.size();
    }

    private boolean isValidPair(OkeyTile t1, OkeyTile t2){
        if(t1.isJoker() || t2.isJoker()){
            return true;
        }

        if(t1.isFakeJoker()){
            if(joker.getColor().equals(t2.getColor()) && joker.getNum() == t2.getNum()){
                return true;
            }
            return false;
        }
        if(t2.isFakeJoker()){
            if(joker.getColor().equals(t1.getColor()) && joker.getNum() == t1.getNum()){
                return true;
            }
            return false;
        }

        if(t1.getColor().equals(t2.getColor()) && t1.getNum() == t2.getNum()){
            return true;
        }

        return false;
    }
    

}
