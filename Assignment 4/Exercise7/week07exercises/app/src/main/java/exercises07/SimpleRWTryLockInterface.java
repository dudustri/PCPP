// For week 7
// raup@itu.dk * 10/10/2021
package exercises07;

interface SimpleRWTryLockInterface {
    public boolean readerTryLock();
    public void readerUnlock();
    public boolean writerTryLock();
    public void writerUnlock();
}
