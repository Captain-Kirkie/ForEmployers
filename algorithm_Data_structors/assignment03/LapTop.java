package assignment03;

public class LapTop {
    public String name;
    public long ID;
    public String brand;

    public LapTop(String name, long ID, String brand){
        this.name = name;
        this.ID = ID;
        this.brand = brand;
    }

    public String getBrand(){return this.brand;}
    public String getName() {
        return this.name;
    }

    public long getID() {
        return this.ID;
    }


}
