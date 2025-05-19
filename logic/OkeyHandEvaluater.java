package logic;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import inventory.OkeyTile;

public class OkeyHandEvaluater {
    OkeyTile joker;

    public OkeyHandEvaluater(OkeyTile joker){
        this.joker = joker;
    }

    public int calculateScore(ArrayList<OkeyTile> hand){
        int pairModeUsedTiles = hand.size() - this.piarMode(hand);
        int normalModeUsedTiles = hand.size() - this.normalMode(hand);

        return normalModeUsedTiles > pairModeUsedTiles ? normalModeUsedTiles: pairModeUsedTiles;
    }

    private int normalMode(ArrayList<OkeyTile> hand){
        ArrayList<OkeyTile> leftovers = new ArrayList<OkeyTile>(hand);
        while(true){
            ArrayList<OkeyTile> candidateSameNum = this.longestSameNum(leftovers);
            ArrayList<OkeyTile> candidateSameColor = this.longestSameColor(leftovers);

            System.out.println("Aynı renk subset:");
            for (OkeyTile okeyTile : candidateSameColor) {
                okeyTile.display();
            }
            System.out.println("Aynı sayı subset:");
            for (OkeyTile okeyTile : candidateSameNum) {
                okeyTile.display();
            }

            if(candidateSameNum.size() < 3 && candidateSameColor.size() < 3){
                break;
            }
            if(candidateSameColor.size() >= candidateSameNum.size()){
                this.removeSubset(leftovers, candidateSameColor);
            }
            else{
                this.removeSubset(leftovers, candidateSameNum);
            }
        }
        return leftovers.size();
    }

    private ArrayList<OkeyTile> longestSameNum(ArrayList<OkeyTile> leftovers){
        ArrayList<OkeyTile> maxSameNum = new ArrayList<>();

        for (int i = leftovers.size()-1; i > 1; i--) {
            ArrayList<OkeyTile> tmpMaxSameNum = new ArrayList<>();
            tmpMaxSameNum.add(leftovers.get(i));
            for (int j = i-1; j > 0; j--){
                if(tmpMaxSameNum.size() < 4){
                    this.addValidSameNum(tmpMaxSameNum, leftovers.get(j));
                }
                if(tmpMaxSameNum.size() == 4){
                    return tmpMaxSameNum;
                }
            }
            if(tmpMaxSameNum.size() > maxSameNum.size()){
                maxSameNum.removeAll(maxSameNum);
                maxSameNum.addAll(tmpMaxSameNum);
            }
        }

       return maxSameNum;
    }

    private void addValidSameNum(ArrayList<OkeyTile> sameNum, OkeyTile newTile){
        if(newTile.isJoker()){
            sameNum.add(newTile);
            return;
        }
        OkeyTile candidate = new OkeyTile(newTile.getNum(), newTile.getColor());

        if(candidate.isFakeJoker()){
            candidate.setNum(joker.getNum());
            candidate.setColor(joker.getColor());
        }

        for (OkeyTile okeyTile : sameNum) {
            if(okeyTile.getNum() != candidate.getNum() || okeyTile.getColor().equals(candidate.getColor())){
                return;
            }
        } 
        sameNum.add(newTile);   
    }

    private ArrayList<OkeyTile> longestSameColor(ArrayList<OkeyTile> leftovers){
        HashMap<String, ArrayList<OkeyTile>> colorMap = this.sortSameColor(leftovers);

        ArrayList<OkeyTile> maxSameColor = new ArrayList<>();

        for (int i = leftovers.size()-1; i > 1; i--) {
            ArrayList<OkeyTile> tmpMaxSameColor = new ArrayList<>();
            tmpMaxSameColor.add(leftovers.get(i));
            for (int j = i-1; j > 0; j--){
                this.addValidSameColor(tmpMaxSameColor, leftovers.get(j));
            }
            if(tmpMaxSameColor.size() > maxSameColor.size()){
                maxSameColor.removeAll(maxSameColor);
                maxSameColor.addAll(tmpMaxSameColor);
            }
        }

       return maxSameColor;
    }

    private HashMap<String, ArrayList<OkeyTile>> sortSameColor(ArrayList<OkeyTile> list){
        HashMap<String, ArrayList<OkeyTile>> colorMap = new HashMap<String, ArrayList<OkeyTile>>();
        boolean hasJoker = false;
        for (OkeyTile okeyTile : list) {
            if(!colorMap.containsKey(okeyTile.getColor())){
                colorMap.put(okeyTile.getColor(), new ArrayList<>());
            }
            colorMap.get(okeyTile.getColor()).add(okeyTile);
        }

        colorMap.forEach((key, value) -> {
            Collections.sort(value);
        });

        return colorMap;
    }

    private void addValidSameColor(ArrayList<OkeyTile> sameColor, OkeyTile newTile){
        OkeyTile lastTile = sameColor.get(sameColor.size() - 1);
        if(lastTile.isJoker()){
            
        }
        if(lastTile.isFakeJoker()){

        }
    }

    private boolean sameColorJokerValidation(OkeyTile lastTile, OkeyTile newTile, int subsetLen){
        if(lastTile.getNum() == 13){
            if(newTile.getNum() != 1){
               return false;
            }
        }
        return true;
    }

    private void removeSubset(ArrayList<OkeyTile> mainList, ArrayList<OkeyTile> removedList){
        for (OkeyTile okeyTile : removedList) {
            mainList.remove(okeyTile);
        }
    }

    private int piarMode(ArrayList<OkeyTile> hand){
        ArrayList<OkeyTile> leftovers = new ArrayList<OkeyTile>(hand);
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
