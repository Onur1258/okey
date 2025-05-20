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
        System.out.println("pairmode eşleşme sayısı:" + pairModeUsedTiles);
         System.out.println("---------------------------");

        int normalModeUsedTiles = hand.size() - this.normalMode(hand);

       
        System.out.println("normalmode eşleşme sayısı:" + normalModeUsedTiles);

        int score = normalModeUsedTiles;
        if(pairModeUsedTiles > normalModeUsedTiles){
            System.out.println("Player should choose pair mode");
            score = pairModeUsedTiles;
        }
        else{
            System.out.println("Player should choose normal mode");
        }
        return score;
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
            System.out.println("-------------");
            System.out.println("Aynı sayı subset:");
            for (OkeyTile okeyTile : candidateSameNum) {
                okeyTile.display();
            }
            System.out.println("-------------");

            ArrayList<OkeyTile> commons = this.commonElements(candidateSameNum, candidateSameColor);
            
            if(candidateSameNum.size() < 3 && candidateSameColor.size() < 3){
                break;
            }

            if(this.chooseNormalModeType(candidateSameNum, candidateSameColor, commons)){
                System.out.println("Aynı renk seçildi.");
                this.removeSubset(leftovers, candidateSameColor);
            }
            else{
                System.out.println("Aynı sayı seçildi.");
                this.removeSubset(leftovers, candidateSameNum);
            }
            System.out.println();
        }
        return leftovers.size();
    }

    private boolean chooseNormalModeType(ArrayList<OkeyTile> candidateSameNum, ArrayList<OkeyTile> candidateSameColor, ArrayList<OkeyTile> commons){
        //true:same color false: same num
        if((candidateSameNum.size() == 3 && candidateSameColor.size() == 3)){
            return true;
        }

        if(commons.size() == 2 || commons.isEmpty()){
            return candidateSameNum.size() > candidateSameColor.size() ? false : true;
        }

        OkeyTile commonTile = commons.get(0);
        int sameColorIndex = candidateSameColor.indexOf(commonTile) + 1;
        if(candidateSameColor.size() > 3 && (candidateSameColor.size() - sameColorIndex >= 3 || sameColorIndex > 3)){
            candidateSameColor.remove(commonTile);

        }
        if(candidateSameNum.size() > 3){
            candidateSameNum.remove(commonTile);
        }

        if(candidateSameNum.size() > candidateSameColor.size()){
            return false;
        }
        return true;
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

    private ArrayList<OkeyTile> longestSameColor(ArrayList<OkeyTile> list){
        HashMap<String, ArrayList<OkeyTile>> colorMap = new HashMap<String, ArrayList<OkeyTile>>();
        colorMap.put("sarı", new ArrayList<>());
        colorMap.put("mavi", new ArrayList<>());
        colorMap.put("siyah", new ArrayList<>());
        colorMap.put("kırmızı", new ArrayList<>());
        HashMap<String, ArrayList<OkeyTile>> jokerAndFakeJ = new HashMap<String, ArrayList<OkeyTile>>();
        jokerAndFakeJ.put("joker", new ArrayList<>());
        

        for (OkeyTile okeyTile : list) {
            if(okeyTile.isFakeJoker()){
                colorMap.get(this.joker.getColor()).add(okeyTile);
                continue;
            }
            if(okeyTile.isJoker()){
                jokerAndFakeJ.get("joker").add(okeyTile);
                continue;
            }
            colorMap.get(okeyTile.getColor()).add(okeyTile);
        }
        ArrayList<OkeyTile> maxUsedSubset = new ArrayList<>();
        for (String key : colorMap.keySet()) {
            Collections.sort(colorMap.get(key));
            ArrayList<OkeyTile> usedSubset = this.findLongestConsecutive(colorMap.get(key), jokerAndFakeJ);
            if(usedSubset.size() > maxUsedSubset.size()){
                maxUsedSubset.removeAll(maxUsedSubset);
                maxUsedSubset.addAll(usedSubset);
            }
        }

        return maxUsedSubset;
    }

    private ArrayList<OkeyTile> findLongestConsecutive(ArrayList<OkeyTile> list, HashMap<String, ArrayList<OkeyTile>> jMap){
        ArrayList<OkeyTile> max = new ArrayList<>();
        ArrayList<OkeyTile> tmpMax = new ArrayList<>();
        
        if(list.isEmpty()){
            return max;
        }

        int jokerUsage = 0;
        int realJokerUsage = 0;

        tmpMax.add(list.get(0));
        int tmpIndex = 0;
        for (int i = 1; i < list.size(); i++) {
            if(list.get(i).getNum() == tmpMax.get(tmpIndex).getNum()){
                continue;
            }

            if ((list.get(i).getNum() == tmpMax.get(tmpIndex).getNum() + 1 && !tmpMax.get(tmpIndex).isJoker()) || (tmpMax.get(tmpIndex).isJoker() && tmpMax.get(tmpIndex-1).getNum() + 2 == list.get(i).getNum())) {
                tmpMax.add(list.get(i));
                tmpIndex++;
            } else {
                if(jokerUsage < jMap.get("joker").size()){
                    tmpMax.add(jMap.get("joker").get(jokerUsage));
                    tmpIndex++;
                    jokerUsage++;
                    i--;
                    continue;
                }
                if(tmpMax.size() > max.size()){
                    max.removeAll(max);
                    max.addAll(tmpMax);
                    tmpIndex = 0;
                    realJokerUsage = jokerUsage;
                    jokerUsage = 0;
                }
                tmpMax.removeAll(tmpMax);
                tmpMax.add(list.get(i));
                tmpIndex = 0;
            }
        }
        if(tmpMax.size() > max.size()){
            max.removeAll(max);
            max.addAll(tmpMax);
            realJokerUsage = jokerUsage;
        }

        if(max.get(max.size()-1).getNum() == 13){
            if(max.get(0).getNum() != 1 && list.get(0).getNum() == 1){
                max.add(list.get(0));
                return max;
            }
        }
        
        if(realJokerUsage < jMap.get("joker").size()){
            max.add(jMap.get("joker").get(realJokerUsage));
        }
        return max;
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
                    System.out.println("Pair mode çıkanlar");
                    leftovers.get(i).display();
                    leftovers.get(j).display();
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
    
    private ArrayList<OkeyTile> commonElements(ArrayList<OkeyTile> candidateSameNum, ArrayList<OkeyTile> candidateSameColor){
        ArrayList<OkeyTile> commons = new ArrayList<>(); // max 2 olabilir.

        for (int i = 0; i < candidateSameNum.size(); i++) {
            for (int j = 0; j < candidateSameColor.size(); j++) {
                if(candidateSameNum.get(i).equals(candidateSameColor.get(j))){
                    if(!commons.contains(candidateSameNum.get(i))){
                        commons.add(candidateSameNum.get(i));
                    }
                }
            }
        }

        return commons;
    }
}
