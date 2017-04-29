PROJECT 3

Project Requirements

• Build a Rate Monotonic Scheduler with four threads

• Scheduler details:

• Threads T0 through T3

• Thread T0 has a period of 1 unit

• Thread T1 has a period of 2 units

• Thread T2 has a period of 4 units

• Thread T3 has a period of 16 units

• A unit shall be anywhere from 10-100 ms (depending on operating
system options)

• Each thread will execute the same doWork method but run it different
amount of times:

• Thread T0 executes doWork 1 time

• Thread T1 executes doWork 2 times

• Thread T2 executes doWork 4 times

• Thread T3 executes doWork 16 times

Project Requirements

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

Project Requirements

• The following test cases shall be demonstrated

• Nominal case with no overruns

• Failed case where the doWork function is called as many times as
required to lead to an overrun condition in T0 – what happens to
other threads?

• Failed case where T2 has an overrun condition – what happens to
other threads?

• All results are printed out at the completion of the run to not effect
the timing

• Bonus points: provide average execution time spent in
each thread for each major frame – again provided at end of the run

Project Requirements
• doWork function will do the following:

• Will multiply the content of each cell of a 10x10 matrix starting with 
column 0 followed by 5,then,1 followed by 6, etc

1 3 5 7 9 2 4 6 8 10
--------------------

1 1 1 1 1 1 1 1 1 1

1 1 1 1 1 1 1 1 1 1

1 1 1 1 1 1 1 1 1 1

1 1 1 1 1 1 1 1 1 1

1 1 1 1 1 1 1 1 1 1

1 1 1 1 1 1 1 1 1 1

1 1 1 1 1 1 1 1 1 1

1 1 1 1 1 1 1 1 1 1

1 1 1 1 1 1 1 1 1 1

1 1 1 1 1 1 1 1 1 1

Traverse and multiply
in this direction

Hints

• Remember that you have a scheduler that is orchestrating
everything else – separate thread

• Priorities are essential

• Semaphores needed for synchronization

• May need mutex to protect shared data between scheduler and
threads – remember priority inversion

• You need to use processor affinity on all your threads (including
the scheduler)

• For the overrun conditions, you should keep scheduling the
thread that has missed its deadline even after it is in an overrun
condition

• Do not skip scheduling for that execution period

• You can initially use a sleep( ) or similar function to set the
timing on your scheduler until you work out the synchronization
with the other threads and then replace with a timer

Project Artifacts

• Demonstrate by outputting the counters for each thread
that shows how many times each one ran and how many
times an overrun occurred per thread

• Can be printed to the screen or sent to a file

Students must turn in the following:

• Source code

• Executable

• Output of the program

• A brief design description that explains the design, how were the
threads synchronized and dispatched
