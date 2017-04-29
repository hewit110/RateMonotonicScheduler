import java.util.concurrent.Semaphore;
/**
 • Rate Monotonic Scheduler with four threads
 • Each thread will execute the same doWork method but run it different amount of times:
 • Thread T0 has a period of 1 unit, executes doWork 1 time
 • Thread T1 has a period of 2 units, executes doWork 2 times
 • Thread T2 has a period of 4 units, executes doWork 4 times
 • Thread T3 has a period of 16 units, executes doWork 16 times
 • A unit shall be anywhere from 10-100 ms (depending on operating
 system options)
 • Scheduler shall have a major frame period of 16 units of
 time
 • Program shall start scheduler and four threads that are to
 be scheduled
 • Scheduler needs to be woken up by a periodic source
 (signal/timer/etc) and it shall schedule the threads
 • The program shall run for 10 periods and then terminate, but not
 before printing out how many times each thread ran
 • Each thread shall increment a dedicated counter each
 time it runs
 • The scheduler shall be able to identify if a thread has
 missed its deadline and keep track of how many times it
 happens
 *
 * @author: Kevin Hewitt
 * @date: 4/24/2017
 * @version: 1.0
 */
public class main {

    //TODO demonstrate test cases
    //TODO provide average execution time
    //TODO implement thread counters
    //TODO identify missed deadlines
    //doWork function Will multiply the content of each cell of a 10x10 matrix
    // tarting with column 0 followed by 5, then 1 followed by 6, etc
    public static void doWork()
    {

        //load up a 2d array with 1's
        int work[][] = new int[10][10];
        for (int i = 0; i < 10; ++i)
        {
            for (int j = 0; j < 10; ++j)
            {
                //System.out.println("Doing work");
                work[i][j] = 1;
            }
        }
        //just do some generic work
        //TODO fix this mess with two independent alternating "for" loops
        for (int k = 0; k<10;++k)
        {
            int arbitrary = work[0][k] * work[0][k];
        }
        for (int k = 0; k<10;++k)
        {
            int arbitrary = work[5][k] * work[5][k];
        }
        for (int k = 0; k<10;++k)
        {
            int arbitrary = work[1][k] * work[1][k];
        }
        for (int k = 0; k<10;++k)
        {
            int arbitrary = work[6][k] * work[6][k];
        }
        for (int k = 0; k<10;++k)
        {
            int arbitrary = work[2][k] * work[2][k];
        }
        for (int k = 0; k<10;++k)
        {
            int arbitrary = work[7][k] * work[7][k];
        }
        for (int k = 0; k<10;++k)
        {
            int arbitrary = work[3][k] * work[3][k];
        }
        for (int k = 0; k<10;++k)
        {
            int arbitrary = work[8][k] * work[8][k];
        }
        for (int k = 0; k<10;++k)
        {
            int arbitrary = work[4][k] * work[4][k];
        }
        for (int k = 0; k<10;++k)
        {
            int arbitrary = work[9][k] * work[9][k];
        }
    }

    public static void main(String[] argvs) {
        //queue will hold strings that are thread references.
        //Strings are called later on to fire off a corresponding thread.
        Queue q = new Queue();
        //Semaphore for preventing a time unit from executing in the scheduler before the thread
        Semaphore schedulerWait = new Semaphore(1);
        //Semaphore for preventing a time unit from executing in a thread before the scheduler
        Semaphore workWait = new Semaphore(1);
        //semaphore for preventing multiple threads from running at the same time
        Semaphore threadWait = new Semaphore (1);


        Thread t0 = new Thread() {
            @Override
            public void run() {
                try
                {
                    //schedulerWait is released at the end of one iteration of the scheduler (one for every time unit)
                    workWait.acquire();
                    doWork();
                    //workWait.release tells the scheduler that one time unit has executed in the thread
                    schedulerWait.release();
                    //threadWait.release indicates the conclusion of the thread
                    threadWait.release();
                    System.out.println("Thread t0 has ended");
                } catch (Exception e) {
                    System.out.println("Something went wrong");
                }
            }
        };
        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    for (int i=0;i<2;++i) {
                        workWait.acquire();
                        doWork();
                        schedulerWait.release();
                    }
                    threadWait.release();
                    System.out.println("Thread t1 has ended");
                } catch (Exception e) {
                    System.out.println("Something went wrong");
                }
            }
        };
        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {
                    for (int i=0;i<4;++i) {
                        workWait.acquire();
                        doWork();
                        schedulerWait.release();
                    }
                    threadWait.release();
                    System.out.println("Thread t2 has ended");
                } catch (Exception e) {
                    System.out.println("Something went wrong");
                }
            }
        };
        Thread t3 = new Thread() {
            @Override
            public void run() {
                try {
                    for (int i=0;i<16;++i) {
                        workWait.acquire();
                        doWork();
                        schedulerWait.release();
                    }
                    threadWait.release();
                    System.out.println("Thread t3 has ended");
                } catch (Exception e) {
                    System.out.println("Something went wrong");
                }
            }
        };

//----------------------------------------------scheduler section-----------------------------------------------------

        //every iteration of i represents one generic time unit.
        //10 periods of 16 time units = 160.
        //workWait prevents the scheduler from executing a time unit until a thread has released workWait(), indicating
        //that it has finished one of its own time units
        for (int i = 0; i < 160; ++i) {
            System.out.println("Scheduler has completed one time unit");

            try
            {
                schedulerWait.acquire();
            } catch (Exception e)
            {
                System.out.println("Error in attempting to acquire workWait permit");
            }

            //Every 16'th iteration marks the end of a period and the start of a new one: trigger 4 new threads
            //Threads are added to a queue and ordered according to time period manually.
            if (i%16 == 0)
            {
                q.enqueue("t0");
                q.enqueue("t1");
                q.enqueue("t2");
                q.enqueue("t3");
            }

            //If a thread has finished during this period:
            //System.out.println("Available permits are " + schedulerWait.availablePermits());
            if (threadWait.availablePermits() > 0)
            {
                //If a thread has completed all of its time units and released threadWait, remove it from the queue. The thread has concluded.
                //unless it is the first iteration, since no threads have been started yet.
                //availablePermits() > 0 marks the lacking of a running thread.
                try {
                    if (i != 0)
                    {
                        q.dequeue();
                    }
                    //Take a look at the next thread in line and kick it off.
                    Object thread = q.peek();
                    //System.out.println("Object in the queue is " + thread.toString());
                    switch (thread.toString()) {
                        case "t0":
                            threadWait.acquire();
                            //System.out.println("t0 just took a permit,  " + schedulerWait.availablePermits() + "permits remaining");
                            System.out.println("Thread t0 has begun");
                            t0.start();
                            break;
                        case "t1":
                            threadWait.acquire();
                            System.out.println("Thread t1 has begun");
                            t1.start();
                            break;
                        case "t2":
                            threadWait.acquire();
                            System.out.println("Thread t2 has begun");
                            t2.start();
                            break;
                        case "t3":
                            threadWait.acquire();
                            System.out.println("Thread t3 has begun");
                            t3.start();
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Something went wrong while modifying the queue or starting a thread!");
                    e.printStackTrace();
                }
            }//close availablePermits() > 0
            //Now that the scheduler has completed a time unit, signal a thread to execute on of its time units
            try
            {
                if (workWait.availablePermits() < 1) {
                    workWait.release();
                }
            }
            catch (Exception e)
            {
                System.out.println("Somethign happened while attempting to release workWait permit");
            }//close catch
        }//close for
    }//close main method
}//close class