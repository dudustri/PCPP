Exercise 1.1 Consider the code in the file TestLongCounterExperiments.java. Note that this file contains a class, LongCounter, used by two threads:

    class LongCounter {
        private long count = 0;
        public void increment() {
            count = count + 1;
        }
        public long get() {
            return count;
    }

}

Mandatory

1. The main method creates a LongCounter object. Then it creates and starts two threads that run concurrently,
   and each increments the count field 10 million times by calling method increment.
   What output values do you get? Do you get the expected output, i.e., 20 million?

- The output(s) we get are:
- "Count is 19763029 and should be 20000000"
- "Count is 19666424 and should be 20000000"
- "Count is 19350130 and should be 20000000"
  ...
  In other words, the count seems to never reach the expected value (20000000).

2. Reduce the counts value from 10 million to 100, recompile, and rerun the code. It is now likely that you
   get the expected result (200) in every run.
   Explain how this could be. Is it guaranteed that the output is always 200?

- The reason why we always get the expected result (200) is because
  thread t1 finishes before thread t2 starts. Hence, when thread t1 is done the count of lc is 100 and after thread t2 is finished the count of lc is 200. However, there is no guarantee that the count will always be 200. In a super slow environment, thread t1 might NOT finish before thread t2 starts which would result in the count not being 200.

3. The increment method in LongCounter uses the assignment
   count = count + 1; to add one to count. This could be expressed also as count += 1 or as count++. Do you think it would make any difference to use one of these forms instead? Why? Change the code and run it.

- Using one instead of the other two makes no difference. The reason is that all of them are in fact shorthand for three operations: read the value, add one to it, and write out the new value (Goetz, p. 6)

Do you see any difference in the results for any of these alternatives?

- No, we get exactly the same result when using a small or larger starting count.

4. Set the value of counts back to 10 million. Use Java ReentrantLock to ensure that the output of the program equals 20 million. Explain why your solution is correct, and why no other output is possible.

Note: In your explanation, please use the concepts and vocabulary introduced during the lecture, e.g., critical sections, interleavings, race conditions, mutual exclusion, etc.
Note II: The notes above applies to all exercises asking you to explain the correctness of your solution.

- To ensure mutual exclusion and no race conditions, we want to identify the critical section of the code. To do that, we locate the place(s) where the threads share memory/resources which would be the incrementation inside both loops:

      lc.increment();

- To achieve mutual exclusion, we create a lock (Java ReentrantLock) and aquire its lock right before lc.increment(). In addition, we release the lock with .unlock() right after lc.increment(). Thus, none of the two threads will increment the shared counter variable until the other is finished incrementing.

- This is also why no other output will be possible. If thread t2 attemps to aquire the lock while thread t1 is still holding it, thread t2 will have to wait until thread t1 is finished incrementing and has released the lock.

5. By using the ReetrantLock in the exercise above, you have defined a critical section. Does your critical section contain the least number of lines of code? If so, explain why. If not, fix it and explain why your new critical section contains the least number of lines of code. Hint: Recall that the critical section should only include the parts of the program that only one thread can execute at the same time.

- As stated in question 4 above, the only block of code that cannot be executed by both threads at a time is lc.increment(). Both threads may start and run their respective loops after one another or simultaneously, however, the incrementation of count is the only part where we must ensure mutual exclusion.

[Q]: There seems to be a difference in performance when we place the lock outside the for-loop and unlock after the for-loop as well. Naturally, the loop will do n-amount of locks and unlocks if it is placed inside and thereby covering the least amount of lines. However, from a performance standpoint this seems to be not wise.

Challenging 6. Decompile the methods increment from above to see the byte code in the three versions (as is, +=, ++). The basic decompiler is javap. The decompiler takes as input a (target) .class file. In Gradle projects, .class files are located in the directory app/build/classes/—after compiling the .java files. The flag -c decompiles the code of a class. Does the output of javap verify or refuse the explanation you provided in part 3.?

 - Verify!

7. Extend the LongCounter class with a decrement() method which subtracts 1 from the count field without using locks. Change the code in main so that t1 calls decrement 10 million times, and t2 calls increment 10 million times, on a LongCounter instance. What should the expected output be after both threads have completed?

- The expected output will be either a positive or negative number since the threads are not mutually exclusive without locks.

Use ReentrantLock to ensure that the program outputs 0. Explain why your solution is correct, and why no other output is possible.

- Assuming locks are placed correctly, the only output possible is 0 since we make sure that
  the two threads are not executing their respective critical sections at the same time. In other words, a decrement will always be followed by an increment and the other way as well.

8. Explain, in terms of the happens-before relation, why your solution for part 4 produces the expected output.

- The solution we have provided for part produces the correct result because the l.unlock() inside t1 happens-before the l.lock() inside t2. Thus, we have:
  t1(l.lock()),...t1(l.unlock()),...t2(l.lock()),...t2(l.unlock())...
  which satifies definition of the happens-before relation.

  from 

9. Again for the code in part 4 (i.e., without decrement()), remove the ReetrantLock you added. Set the variable counts to 3. What is the minimum value that the program prints?

- The minimum value the program prints is 6
- Feedback: WRONG, the right value is acutally 2. There can be multiple combinations of interleavings
resulting in both threads being 1 (because of read,modify,write)

Does the minimum value change if we set counts to
a larger number (e.g., 20 million)?
Provide an interleaving showing how the minimum value output can occur.

 - Yes, output: "Count is 39824053 and should be 40000000"
   Interleaving: t1(increment(read, modify)), t2(increment(read, modify, write)), t1(increment(write)).

      As we show above, the reason why the minimum value can occur is because
      count++ is in fact three ops: read, modify, write. Thus, if t1 has read and modified but not written to memory before t2 starts to read, modify and write, there will be a mix up of the current count actual is.



Exercise 1.2 Consider this class, whose print method prints a dash “-”, waits for 50 milliseconds, and then prints a vertical bar “|”:

class Printer {
public void print() {
System.out.print("-");
try { Thread.sleep(50); } catch (InterruptedException exn) { }
System.out.print("|");
}
}

Mandatory

1. Write a program that creates a Printer object p, and then creates and starts two threads. Each thread must call p.print() forever. Note: You can easily run your program using the gradle project for the exercises by placing your code in the directory week01exercises/app/src/main/java/exercises01/(remember to add package exercises01; as the first line of your Java files). You will observe that, most of the time, your program print the dash and bar symbols alternate neatly as in -|-|-|-|-|-|-|. But occasionally two bars are printed in a row, or two dashes are printed in a row, creating small “weaving faults” like those shown below:
   -|-|-|-||--|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
   -|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
   -|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
   -|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
   -|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-||--|
   -|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
   -|-|-|-|-|-|-|-|-||--|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
   -|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
   -|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
2. Describe and provide an interleaving where this happens.

- The two threads will interleave when they run simultaneously. This happens when thread t1 starts and the print method of thread t2 happens before t1 has waited 50 miliseconds. Thus, an interleaving could look like:
  t1(print("-")), t1(sleep(50)), t2(print("-")), t2(sleep(50)), t1(print("|")), t2(print("|"))

3. Use Java ReentrantLock to ensure that the program outputs the expected sequence -|-|-|-|.... Compile and run the improved program to see whether it works. Explain why your solution is correct, and why it is not possible for incorrect patterns, such as in the output above, to appear.

- The solution works because we stop the race condition for shared memory using mutual exclusion by locking and unlocking the threads. The critical section of each thread is the print method within the infinite while loops.

Challenging 4. Explain, in terms of the happens-before relation, why your solution for part 3 produces the correct output.

- The reason why our solution for part 3 produces the correct output is because l.lock() and l.unlock() within thread t1 happens before l.lock() and l.unlock within thread t2. Thus, we remove any undesired interleavings.

Exercise 1.3 Imagine that due to the COVID-19 pandemic Tivoli decides to limit the number of visitors to 15000. To this end, you are asked to modify the code for the turnstiles we saw in the lecture. The file CounterThreads2Covid.java includes a new constant MAX_PEOPLE_COVID equal to 15000. However,the two threads simulate 20000 people entering to the park, so unfortunately some people will not get in :’(.

Mandatory

1. Modify the behaviour of the Turnstile thread class so that that exactly 15000 enter the park; no less no more. To this end, simply add a check before incrementing counter to make sure that there areless than 15000 people in the park. Note that the class does not use any type of locks. You must use ReentrantLock to ensure that the program outputs the correct value, 15000.

- The code implementatiion is in the respective file (CounterThreads2Covid.java).

2. Explain why your solution is correct, and why it always output 15000.

- First of all we lock the threads when performing the counting process to ensure the mutual exclusion. In this way, it is possible to ensure that there will be no data race to update the counter value. Now, about the people limitation, it was implemented inside the thread an if statement checking the counter value and breaking the for loop process when there is the maximum amount of people forcing that thread to join the method who called it. Therefore, the implementation is correct since it uses mutual exclusion between the threads and break the counter incrementation when enough people passed through the turnstile.

Exercise 1.4 In Goetz chapter 1.1, three motivations for concurrency is given: resource utilization, fairness and convenience. In the note about concurrency there is an alternative characterization of the different motivations for
concurrency (coined by the Norwegian computer scientist Kristen Nygaard):
• Inherent User interfaces and other kinds of input/output.
• Exploitation Hardware capable of simultaneously executing multiple streams of statements. A special (but
important) case is communication and coordination of independent computers on the internet,
• Hidden Enabling several programs to share some resources in a manner where each can act as if they had
sole ownership.
Mandatory
Neither of the definitions is very precise, so for this exercise, there are many possible and acceptable answers.

1. Compare the categories in the concurrency note (https://github.itu.dk/jst/PCPP2023-public/blob/main/week01/
   concurrencyNotes/concurrencyPCPP.pdf and Goetz, try to find some examples of systems which are included in the categories of Goetz, but not in those in the concurrency note, and vice versa (if possible - if
   not possible, argue why).

Goetz’s Motivations for Concurrency:

- **Resource Utilization:** Efficiently utilizing system resources by letting multiple tasks run concurrently.
- **Fairness:** Allowing multiple tasks to progress at a relatively even pace.
- **Convenience:** Making systems more responsive and user-friendly by managing multiple tasks simultaneously.

Nygaard’s Motivations for Concurrency:

- **Inherent:** Relating to user interfaces and other kinds of I/O.
- **Exploitation:** Hardware's capability of executing multiple streams simultaneously, especially when considering independent computers communicating over the internet.
- **Hidden:** Allowing several programs to share resources as if each one had sole ownership.

1. **Exploitation and Resource Utilization**
- Example fitting both: Multi-core processors executing multiple threads in parallel.
- Example fitting only Goetz's definition: Load balancing among distributed servers where the focus is not on hardware's inherent capabilities but on optimizing resources.
- Example fitting only Nygaard's definition Focusing on hardware capabilities over internet communication.

2. **Fairness and Hidden:**
- Fairness focuses on ensuring tasks progress evenly, while Hidden focuses on sharing resources seamlessly.
- Example fitting both: Time-sharing systems where each user gets an equal slice of CPU time.
- Example fitting only Goetz's definition: Ensuring every process gets a fair shot at the CPU.
- Example fitting only Nygaard's definition: Virtualization, where multiple operating systems share the same physical hardware but each thinks it's running alone.

3. **Convenience vs. Inherent:**
- Convenience deals with system responsiveness and user interaction, whereas Inherent focuses on UI and I/O operations.

1. Find examples of 3 systems in each of the categories in the Concurrency note which you have used yourself
   (as a programmer or user).

**Inherent (User interfaces and other kinds of input/output):**
   - Web Browsers: As a user, when I open multiple tabs in browsers like Chrome or Firefox, each tab may run in its own process or thread, ensuring that one unresponsive tab doesn't freeze the entire browser. 
   - Text Editors or IDEs: As a programmer, when I use software like Visual Studio Code, concurrent processes manage the user interface, syntax highlighting, code suggestions, and other real-time features
   - Video Players:  YouTube's web player must handle user input (like play, pause, skip) while concurrently decoding and rendering video.

**Exploitation:**
   - Multiplayer Online Games
   - Cloud-based Collaboration Tools: As a user of tools like Google Docs, I've collaborated with multiple users in real-time, with the platform handling concurrent edits and updates.
   - Distributed Computing Platforms

**Hidden**
   - Operating Systems for example  Windows, macOS or giving each the impression it has complete access to resources
   - Database Management Systems like MySQL they allow multiple clients to concurrently read or write to the database
   - Virtualization Tools: VirtualBox for example to run multiple virtualized environments on a single physical machine

