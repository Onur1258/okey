package logic;

import inventory.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Services {

    public void createOkeyTiles(ArrayList<OkeyTile> okeyTiles){
        Random random = new Random();
        int randomColorIndex = random.nextInt(4);
        int randomNumIndex = random.nextInt(13) + 1;

        String[] colors = {"sarı", "mavi", "siyah", "kırmızı"};

        String randColor = colors[randomColorIndex];
        int indicatorIndex = 0;

        System.out.println(randColor);
        System.out.println(randomNumIndex);

        for (String s : colors) {
            for (int i = 1; i <= 13; i++) {
                OkeyTile t1 = new OkeyTile(i, s.concat("-1"));
                OkeyTile t2 = new OkeyTile(i, s.concat("-2"));

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

        okeyTiles.add(new OkeyTile(0, "fake-joker1"));
        okeyTiles.add(new OkeyTile(0, "fake-joker2"));

        Collections.shuffle(okeyTiles);
    }
}
