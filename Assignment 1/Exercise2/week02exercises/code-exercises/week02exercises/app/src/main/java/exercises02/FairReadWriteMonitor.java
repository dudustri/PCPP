package exercises02;

public class FairReadWriteMonitor {
    private int addReader = 0;
    private int signOffReader = 0;
    private boolean writer = false;

    public static void main(String[] args) {
      FairReadWriteMonitor frwm = new FairReadWriteMonitor();
    }

    
    public synchronized void readLock() {
      while(writer){
        try{
          this.wait();
        }catch(InterruptedException e){
          e.printStackTrace();
        }
        addReader++;
      }
    }

    public synchronized void readUnlock() {
		  signOffReader++;
		  if(addReader==signOffReader){
			  this.notifyAll();
      }
    }   
    
    public synchronized void writeLock() {
		  while(writer){
			  try{
          this.wait();
        } catch(InterruptedException e){
          e.printStackTrace();
        }
      }
		  writer=true;
		  while(addReader != signOffReader){
			  try{
          this.wait();
        }catch(InterruptedException e){
          e.printStackTrace();
        }
      }
    }

    public synchronized void writeUnlock() {
		  writer=false;
		  this.notifyAll();
    }
	
}
