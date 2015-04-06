/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
	public enum PhilosophereState {Eating, Hungry, Thinking};
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
	 int philosophereTalkingId = 0;
	 int numberOfChopSticks;
	 int piNumberOfPhilosophers;
	 PhilosophereState philosophereSates[];
	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		// TODO: set appropriate number of chopsticks based on the # of philosophers
		this.piNumberOfPhilosophers = piNumberOfPhilosophers;
		philosophereSates = new PhilosophereState[piNumberOfPhilosophers];
		for (int i = 0; i < piNumberOfPhilosophers; i++) {
			philosophereSates[i] = PhilosophereState.Thinking;
	    }
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 * @throws InterruptedException 
	 */
	public synchronized void pickUp(final int piTID) throws InterruptedException
	{
		// Check if the left or right philosophers are eating, if they're then wait for them to be finish
		int index = piTID - 1;
		while (philosophereSates[Math.abs((index - 1)) % piNumberOfPhilosophers ] == PhilosophereState.Eating || 
				philosophereSates[(index + 1) % piNumberOfPhilosophers] == PhilosophereState.Eating)
		{
			philosophereSates[index] = PhilosophereState.Hungry;
			wait();
		}
		philosophereSates[index] = PhilosophereState.Eating; 		
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		philosophereSates[piTID - 1] = PhilosophereState.Thinking;
		notifyAll();
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 * @throws InterruptedException 
	 */
	public synchronized void requestTalk(final int piTID) throws InterruptedException
	{
		while(philosophereTalkingId != 0)
		{
			wait();
		}
		philosophereTalkingId = piTID;
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		philosophereTalkingId = 0;
		notifyAll();
	}
}

// EOF
