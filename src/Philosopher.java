import common.BaseThread;

/**
 * Class Philosopher.
 * Outlines main subrutines of our virtual philosopher.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;
	
	/**
	 * Added this instance variable to the class. It will be used to determine the number of times that each philosopher thread will 
	 * execute its run method 
	 */
	private int DINING_STEPS = 1;
	
	/**
	 * Mutator method for the DINING_STEPS
	 * @param DINING_STEPS
	 */
	public void setDINING_STEPS(int DINING_STEPS) {
		this.DINING_STEPS = DINING_STEPS;
	}
	
	/**
	 * Accessor method for the DINING_STEPS
	 * @return
	 */
	public int getDINING_STEPS() {
		return DINING_STEPS;
	}

	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		try
		{
			System.out.println("Philosopher with TID " + this.iTID + " has started eating" );
			yield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			yield();
			System.out.println("Philosopher with TID " + this.iTID + " has finished eating" );
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 * @throws InterruptedException 
	 */
	public void think() 
	{
		try{
			System.out.println("Philosopher with TID " + this.iTID + " has started thinking" );
			yield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			yield();
			System.out.println("Philosopher with TID " + this.iTID + " has finished thinking" );
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		System.out.println("Philosopher with TID " + this.iTID + " has started talking" );
		yield();
		saySomething();
		yield();
		System.out.println("Philosopher with TID " + this.iTID + " has finished talking" );
	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run()
	{
		for (int i = 0; i < getDINING_STEPS(); i++) { // Added a for loop to run the logic DINING_STEPS number of times per philosopher. In the case of this assignment, it should be 10 times. 
			try {
				try {
					DiningPhilosophers.soMonitor.pickUp(getTID());
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	
			eat();
	
			DiningPhilosophers.soMonitor.putDown(getTID());
	
			think();
	
			/*
			 * TODO:
			 * A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			 */
			try {
				DiningPhilosophers.soMonitor.requestTalk(getTID());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(DiningPhilosophers.soMonitor.philosophereTalkingId == getTID()) // Check if the current philosopher is allowed to talk
			{
				// Some monitor ops down here...
				talk();
				DiningPhilosophers.soMonitor.endTalk(getTID());
			}
			yield();
		}
	} // run()

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};

		System.out.println
		(
			"Philosopher " + getTID() + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
	}
}

// EOF
