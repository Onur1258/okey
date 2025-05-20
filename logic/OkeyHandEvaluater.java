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

    private ArrayList<OkeyTile> longestSameColor(ArrayList<OkeyTile> list){
        HashMap<String, ArrayList<OkeyTile>> colorMap = new HashMap<String, ArrayList<OkeyTile>>();
        colorMap.put("sarı", new ArrayList<>());
        colorMap.put("mavi", new ArrayList<>());
        colorMap.put("siyah", new ArrayList<>());
        colorMap.put("kırmızı", new ArrayList<>());
        HashMap<String, ArrayList<OkeyTile>> jokerAndFakeJ = new HashMap<String, ArrayList<OkeyTile>>();
        jokerAndFakeJ.put("fake", new ArrayList<>());
        jokerAndFakeJ.put("joker", new ArrayList<>());
        

        for (OkeyTile okeyTile : list) {
            if(okeyTile.isFakeJoker()){
                jokerAndFakeJ.get("fake").add(okeyTile);
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
            System.out.println("Coloooormap:" + key);
            Collections.sort(colorMap.get(key));
            System.out.println("SOOOORT");
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

        int fakeUsage = 0, jokerUsage = 0;
        int realFakeUsage = 0, realJokerUsage = 0;

        tmpMax.add(list.get(0));
        int tmpIndex = 0;
        for (int i = 1; i < list.size(); i++) {
            if(list.get(i).getNum() == tmpMax.get(tmpIndex).getNum()){
                continue;
            }

            if (list.get(i).getNum() == tmpMax.get(tmpIndex).getNum() + 1 || (tmpMax.get(tmpIndex).isJoker() && tmpMax.get(tmpIndex-1).getNum() + 2 == list.get(i).getNum())) {
                tmpMax.add(list.get(i));
                tmpIndex++;
            } else {
                if(fakeUsage < jMap.get("fake").size()){
                    if(list.get(i).getColor().equals(this.joker.getColor()) && list.get(i).getNum() == this.joker.getNum() + 1){
                        tmpMax.add(jMap.get("fake").get(fakeUsage));
                        tmpIndex++;
                        fakeUsage++;
                        i--;
                        continue;
                    }
                }
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
                    realFakeUsage = fakeUsage;
                    realJokerUsage = jokerUsage;
                    jokerUsage = 0;
                    fakeUsage = 0;
                }
                tmpMax.removeAll(tmpMax);
                tmpMax.add(list.get(i));
            }
        }
        if(tmpMax.size() > max.size()){
            max.removeAll(max);
            max.addAll(tmpMax);
            realFakeUsage = fakeUsage;
            realJokerUsage = jokerUsage;
        }

        if(max.get(max.size()-1).getNum() == 13){
            if(max.get(0).getNum() != 1 && list.get(0).getNum() == 1){
                max.add(list.get(0));
            }
            if(realJokerUsage < jMap.get("joker").size()){
                max.add(jMap.get("joker").get(realJokerUsage));
            }
            if(realFakeUsage < jMap.get("fake").size() && max.get(max.size()-1).getColor().equals(this.joker.getColor()) && this.joker.getNum() == 1){
                tmpMax.add(jMap.get("fake").get(realFakeUsage));
            }
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
