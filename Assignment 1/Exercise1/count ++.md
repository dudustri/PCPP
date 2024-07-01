Warning: File ./TestLongCounterExperiments.class does not contain class TestLongCounterExperiments
Compiled from "TestLongCounterExperiments.java"
public class E1.TestLongCounterExperiments {
  E1.TestLongCounterExperiments$LongCounter lc;

  int counts;

  java.util.concurrent.locks.Lock l;

  public E1.TestLongCounterExperiments();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: aload_0
       5: new           #7                  // class E1/TestLongCounterExperiments$LongCounter
       8: dup
       9: aload_0
      10: invokespecial #9                  // Method E1/TestLongCounterExperiments$LongCounter."<init>":(LE1/TestLongCounterExperiments;)V
      13: putfield      #12                 // Field lc:LE1/TestLongCounterExperiments$LongCounter;
      16: aload_0
      17: ldc           #18                 // int 1000000
      19: putfield      #19                 // Field counts:I
      22: aload_0
      23: new           #23                 // class java/util/concurrent/locks/ReentrantLock
      26: dup
      27: invokespecial #25                 // Method java/util/concurrent/locks/ReentrantLock."<init>":()V
      30: putfield      #26                 // Field l:Ljava/util/concurrent/locks/Lock;
      33: new           #30                 // class java/lang/Thread
      36: dup
      37: aload_0
      38: invokedynamic #32,  0             // InvokeDynamic #0:run:(LE1/TestLongCounterExperiments;)Ljava/lang/Runnable;
      43: invokespecial #36                 // Method java/lang/Thread."<init>":(Ljava/lang/Runnable;)V
      46: astore_1
      47: new           #30                 // class java/lang/Thread
      50: dup
      51: aload_0
      52: invokedynamic #39,  0             // InvokeDynamic #1:run:(LE1/TestLongCounterExperiments;)Ljava/lang/Runnable;
      57: invokespecial #36                 // Method java/lang/Thread."<init>":(Ljava/lang/Runnable;)V
      60: astore_2
      61: new           #30                 // class java/lang/Thread
      64: dup
      65: aload_0
      66: invokedynamic #40,  0             // InvokeDynamic #2:run:(LE1/TestLongCounterExperiments;)Ljava/lang/Runnable;
      71: invokespecial #36                 // Method java/lang/Thread."<init>":(Ljava/lang/Runnable;)V
      74: astore_3
      75: new           #30                 // class java/lang/Thread
      78: dup
      79: aload_0
      80: invokedynamic #41,  0             // InvokeDynamic #3:run:(LE1/TestLongCounterExperiments;)Ljava/lang/Runnable;
      85: invokespecial #36                 // Method java/lang/Thread."<init>":(Ljava/lang/Runnable;)V
      88: astore        4
      90: invokestatic  #42                 // Method java/time/LocalDateTime.now:()Ljava/time/LocalDateTime;
      93: astore        5
      95: aload_1
      96: invokevirtual #48                 // Method java/lang/Thread.start:()V
      99: aload_2
     100: invokevirtual #48                 // Method java/lang/Thread.start:()V
     103: aload_1
     104: invokevirtual #51                 // Method java/lang/Thread.join:()V
     107: aload_2
     108: invokevirtual #51                 // Method java/lang/Thread.join:()V
     111: goto          124
     114: astore        6
     116: getstatic     #56                 // Field java/lang/System.out:Ljava/io/PrintStream;
     119: ldc           #62                 // String Some thread was interrupted
     121: invokevirtual #64                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
     124: invokestatic  #42                 // Method java/time/LocalDateTime.now:()Ljava/time/LocalDateTime;
     127: astore        6
     129: aload_3
     130: invokevirtual #48                 // Method java/lang/Thread.start:()V
     133: aload         4
     135: invokevirtual #48                 // Method java/lang/Thread.start:()V
     138: aload_3
     139: invokevirtual #51                 // Method java/lang/Thread.join:()V
     142: aload         4
     144: invokevirtual #51                 // Method java/lang/Thread.join:()V
     147: goto          160
     150: astore        7
     152: getstatic     #56                 // Field java/lang/System.out:Ljava/io/PrintStream;
     155: ldc           #62                 // String Some thread was interrupted
     157: invokevirtual #64                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
     160: invokestatic  #42                 // Method java/time/LocalDateTime.now:()Ljava/time/LocalDateTime;
     163: astore        7
     165: getstatic     #70                 // Field java/time/temporal/ChronoUnit.MILLIS:Ljava/time/temporal/ChronoUnit;
     168: aload         5
     170: aload         6
     172: invokevirtual #76                 // Method java/time/temporal/ChronoUnit.between:(Ljava/time/temporal/Temporal;Ljava/time/temporal/Temporal;)J
     175: lstore        8
     177: getstatic     #70                 // Field java/time/temporal/ChronoUnit.MILLIS:Ljava/time/temporal/ChronoUnit;
     180: aload         5
     182: aload         7
     184: invokevirtual #76                 // Method java/time/temporal/ChronoUnit.between:(Ljava/time/temporal/Temporal;Ljava/time/temporal/Temporal;)J
     187: lload         8
     189: lsub
     190: lstore        10
     192: getstatic     #56                 // Field java/lang/System.out:Ljava/io/PrintStream;
     195: lload         8
     197: lload         10
     199: invokedynamic #80,  0             // InvokeDynamic #4:makeConcatWithConstants:(JJ)Ljava/lang/String;
     204: invokevirtual #64                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
     207: getstatic     #56                 // Field java/lang/System.out:Ljava/io/PrintStream;
     210: aload_0
     211: getfield      #12                 // Field lc:LE1/TestLongCounterExperiments$LongCounter;
     214: invokevirtual #84                 // Method E1/TestLongCounterExperiments$LongCounter.get:()J
     217: iconst_4
     218: aload_0
     219: getfield      #19                 // Field counts:I
     222: imul
     223: invokedynamic #88,  0             // InvokeDynamic #5:makeConcatWithConstants:(JI)Ljava/lang/String;
     228: invokevirtual #64                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
     231: return
    Exception table:
       from    to  target type
         103   111   114   Class java/lang/InterruptedException
         138   147   150   Class java/lang/InterruptedException

  public static void main(java.lang.String[]);
    Code:
       0: new           #13                 // class E1/TestLongCounterExperiments
       3: dup
       4: invokespecial #91                 // Method "<init>":()V
       7: pop
       8: return
}