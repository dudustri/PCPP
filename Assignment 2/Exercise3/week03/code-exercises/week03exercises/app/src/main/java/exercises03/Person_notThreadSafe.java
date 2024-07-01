package exercises03;

public class Person_notThreadSafe {
    
    private int id; //requirement: id cannot be changed -> final -> safe publication
    private String name; //volatile ensures visibility
    private int zip; //volatile ensures visibility
    private String adress; //volatile ensures visibility
    private static int lastId = 0; //static variable (shared by all instances of the class)
    
    public Person_notThreadSafe(){
        id = lastId++;
    }

    public Person_notThreadSafe(final int initId){
        if(lastId == 0){
            this.id = initId;
            lastId = initId + 1;
        } else {
            this.id = lastId++;
        }
    }

    public static int getLastid() {
        return lastId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getZip() {
        return zip;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdressZip(int newZip, String newAdress){
        this.zip = newZip;
        this.adress = newAdress;
    }

    public void setName(String newName){
        this.name = newName;
    }

}