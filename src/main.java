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
 * @date: 4/24/2017 - 5/7/2017
 * @version: 1.7.3
 */
public class main {
    public static int t0_counter;
    public static int t1_counter;
    public static int t2_counter;
    public static int t3_counter;
    public static int overrun_counter;
    public static boolean isFinished = true;

    /*
    * doWork function Will multiply the content of each cell of a 10x10 matrix
    * tarting with column 0 followed by 5, then 1 followed by 6, etc
    */
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
        //Strings are called later on to execute a corresponding thread.
        Queue q = new Queue();

        //Semaphore used to synchronize the time units of the scheduler and the threads
        Semaphore schedulerWait = new Semaphore(1);

        /*
        Threads will attempt to acquire a semaphore permit.
        Permits are held by the scheduler until the scheduler has indicated that it is ready to begin one time unit.
        Threads will then call doWork() for the specified number of times.
        Each iteration of doWork() represents one single time period.
        When a thread has completed an iteration of work, it will release a semaphore, allowing the Scheduler to complete its time unit.
        Upon completion threads set a boolean iSFinished value to true, which informs the scheduler that it should look for and
        run the next thread in the queue.
        Lastly, a called thread will increment its respective counter by 1.
         */
        Thread t0 = new Thread() {
            @Override
            public void run() {
                try
                {
                    schedulerWait.acquire();
                    System.out.println("T0: schedulerWait: LOCKED");

                    doWork();
                    System.out.println("Thread t0 has finished an iteration of doWork");

                    if (schedulerWait.availablePermits() < 1)
                    {
                        schedulerWait.release();
                        System.out.println("T0: schedulerWait: RELEASED");
                    }

                    System.out.println("Thread t0 has finished");

                    isFinished = true;

                    ++t0_counter;

                } catch (Exception e) {
                    System.out.println("Error: t0");
                }
            }
        };
        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    for (int i=0;i<2;++i) {
                        schedulerWait.acquire();
                        System.out.println("T1: schedulerWait: LOCKED");

                        doWork();
                        System.out.println("Thread t1 has finished an iteration of doWork: i = " + i);

                        if (schedulerWait.availablePermits() < 1) {
                            schedulerWait.release();
                            System.out.println("T1: schedulerWait: RELEASED");
                        }
                    }

                    System.out.println("Thread t1 has finished");

                    isFinished = true;

                    ++t1_counter;

                } catch (Exception e) {
                    System.out.println("Error: t1");
                }
            }
        };
        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {
                    for (int i=0;i<4;++i) {
                        schedulerWait.acquire();
                        System.out.println("T2: WORKWAIT LOCKED");

                        doWork();
                        System.out.println("Thread t2 has finished an iteration of doWork: i = " + i);

                        if (schedulerWait.availablePermits() < 1) {
                            schedulerWait.release();
                            System.out.println("T2: schedulerWait: RELEASED");
                        }
                    }

                    System.out.println("Thread t2 has finished");

                    isFinished = true;

                    ++t2_counter;

                } catch (Exception e) {
                    System.out.println("Error: t2");
                }
            }
        };
        Thread t3 = new Thread() {
            @Override
            public void run() {
                try {
                    for (int i=0;i<16;++i) {
                        schedulerWait.acquire();
                        System.out.println("T3: schedulerWait LOCKED");

                        doWork();
                        System.out.println("Thread t3 has finished an iteration of doWork: i = " + i);

                        if (schedulerWait.availablePermits() < 1) {
                            schedulerWait.release();
                            System.out.println("T3: schedulerWait: RELEASED");
                        }
                    }

                    System.out.println("Thread t3 has finished");

                    isFinished = true;

                    ++t3_counter;

                } catch (Exception e) {
                    System.out.println("Error: t3");
                }
            }
        };

//----------------------------------------------scheduler section-----------------------------------------------------

        /*
        i = one time unit.
        16 time units = 1 period
        10 periods of 16 time units = ( int i = 160...)
         */
        for (int i = 0; i < 160; ++i) {
            System.out.println("---------------------i is " + i + "----------------------------");
            try
            {
                //only award permits if one is taken: prevents extra permits
                if (schedulerWait.availablePermits() < 1)
                {
                    schedulerWait.release();
                }
            } catch (Exception e)
            {
                System.out.println("ERROR: schedulerWait.release(); from SCHEDULER");
                e.printStackTrace();
            }

            //Every 16'th iteration marks the end of a period and the start of a new one: trigger 4 new threads
            //Threads are added to a queue and ordered according to time period manually.
            if (i%16 == 0)
            {
                //if a time period is complete but a thread is not yet finished...
                if (!isFinished)
                {
                    Object thread = q.peek();
                    System.out.println(thread.toString() + " has overrun!");
                    ++overrun_counter;
                }

                System.out.println("Inserting new threads to queue...");
                q.enqueue("t0");
                q.enqueue("t1");
                q.enqueue("t2");
                q.enqueue("t3");
            }

            //If a thread has finished during this period:
            if (isFinished)
            {
                /*
                Remove it from the queue. The thread has concluded.
                Unless it is the first iteration, since no threads have been started yet.
                isFinished marks the lacking of a running thread.
                */
                try {
                    if (i != 0)
                    {
                        q.dequeue();
                    }
                    Object thread = q.peek();
                    //A thread is about to begin, so set isFinished to false.
                    isFinished = false;
                    switch (thread.toString()) {
                        case "t0":
                            System.out.println("Thread t0 has begun");
                            t0.start();
                            break;
                        case "t1":
                            System.out.println("Thread t1 has begun");
                            t1.start();
                            break;
                        case "t2":
                            System.out.println("Thread t2 has begun");
                            t2.start();
                            break;
                        case "t3":
                            System.out.println("Thread t3 has begun");
                            t3.start();
                            break;
                        default:
                            System.out.println("ERROR: All case statements failed");
                    }
                } catch (Exception e) {
                    System.out.println("Something went wrong while modifying the queue or starting a thread!");
                    e.printStackTrace();
                }
            }
            try
            {
                //attempt to allow the thread some time to collect permit so it can compensate for overhead
                Thread.sleep(200);
                schedulerWait.acquire();
                System.out.println("T: schedulerWait: LOCKED");

            }catch(InterruptedException ie)
            {
                ie.printStackTrace();
                System.out.println("Something happened while attempting to acquire SchedulerWait permit");
            }//close catch
        }//close for

        System.out.println("Final tally: /n t0: " + t0_counter + " /n t1: " + t1_counter + " /n t2: " + t2_counter +
                            " /n t3: " + t3_counter + " /n Overrun: " + overrun_counter + " /n ");

    }//close main method
}//close class