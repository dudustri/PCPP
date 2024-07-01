package exercises03;

public class Person {
    
    //remove volatile and start with values instead (faster) - no need to use it here

    private final int id; //requirement: id cannot be changed -> final -> safe publication
    private volatile String name; //volatile ensures visibility
    private volatile int zip;//volatile ensures visibility
    private volatile String adress; //volatile ensures visibility
    private volatile static int lastId = 0; //static variable (shared by all instances of the class)

    /*requirement:
    There must be a constructor for Person that takes no parameters. 
    When calling this constructor, each new instance of Person gets an id one higher
    than the previously created person. In case the constructor is used
    to create the first instance of Person, then the id for that object is set to 0.
    */
    
    public Person(){
        id = lastId++;
    }

    /*There must be a constructor for Person that takes as parameter
    the initial id value from which future ids are generated. 
    In case the constructor is used to create the first instance of Person, 
    the initial parameter must be used. For subsequent instances, 
    the parameter must be ignored and the value of the previously
    created person must be used (as stated in the previous requirement).
    */

    //TODO: Change this to syncronize the classes and avoid data races between the instances

    public Person(final int initId) throws Exception { //person construct with
    //Correction!!!!!!!!
        if(initId < 0) throw new Exception("initId must be equal greater than 0");

        if(lastId == 0){ //if first instance, use parameter as ID
            this.id = initId;
            lastId = initId + 1; //increment ID for next instance
        } else {
            this.id = lastId++;//if not first instance, ignore and set to lastId++
        }
    }  
    //CORRECTION: ID MUST NOT BE NEGATIVE

    //Get methods for all fields
    public static synchronized int getLastid() {
        return lastId;
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized int getZip() {
        return zip;
    }

    public synchronized String getAdress() {
        return adress;
    }

    //requirement: it must be possible to change zip and address together
    public synchronized void setAdressZip(int newZip, String newAdress){
        this.zip = newZip;
        this.adress = newAdress;
    }

    public synchronized void setName(String newName){
        this.name = newName;
    }

}