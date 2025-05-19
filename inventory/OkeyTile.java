package inventory;

public class OkeyTile {
    private int num;
    private String color;
    private boolean isJoker;
    private boolean isIndicator;

    public OkeyTile(int num, String color){
        this.num = num;
        this.color = color;
        this.isJoker = false;
        this.isIndicator = false;
    }

    public int getNum(){
        return this.num;
    }

    public String getColor(){
        return this.color;
    }

    public boolean isJoker(){
        return this.isJoker;
    }

    public void setJoker(){
        this.isJoker = true;
    }

    public boolean isFakeJoker(){
        return this.num == 0 ? true : false;
    }

    public boolean isIndicator(){
        return this.isIndicator;
    }

    public void setIndicator(){
        this.isIndicator = true;
    }
}
