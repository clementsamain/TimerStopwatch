package states;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import states.Context;
import states.stopwatch.*;
import states.timer.*;

public class TestScenarios {

	Context c;
	
    @Before
    public void setup() {
    	c = new Context();
     	//before each test, reset the timer values to avoid interference between tests:
    	AbstractTimer.resetInitialValues();
    	AbstractStopwatch.resetInitialValues();
    }
    
  //This is more a kind of integration test than a real unit test	
  @Test
  public void completeScenario() {
	  assertEquals(IdleTimer.Instance(),c.currentState);
	  assertEquals(0,AbstractTimer.getMemTimer());


	  c.right(); // start incrementing the memTimer variable
	  c.tick();
	  assertSame(SetTimer.Instance(),c.currentState);
	  assertEquals(1,AbstractTimer.getMemTimer());
	  assertEquals(0,AbstractTimer.getTimer());

	  c.left(); // reset the timer
	  assertEquals(0,AbstractTimer.getMemTimer());
	  assertEquals(0,AbstractTimer.getTimer());
	  c.tick();
	  assertSame(SetTimer.Instance(),c.currentState);
	  assertEquals(1,AbstractTimer.getMemTimer());
	  assertEquals(0,AbstractTimer.getTimer());

	  c.up();
	  c.tick();
	  assertEquals(7,AbstractTimer.getMemTimer());
	  assertEquals(0,AbstractTimer.getTimer());

	  c.tick();
	  assertEquals(8,AbstractTimer.getMemTimer());
	  assertEquals(0,AbstractTimer.getTimer());

	  c.right(); // stop incrementing the memTimer variable
	  c.tick();
	  assertEquals(8,AbstractTimer.getMemTimer());
	  assertEquals(0,AbstractTimer.getTimer());
	  
	  c.up(); // start running the timer
	  assertEquals("value of timer ", 8, AbstractTimer.getTimer());
	  c.tick();
	  assertEquals("value of memTimer ", 8, AbstractTimer.getMemTimer());
	  assertEquals("value of timer ", 7, AbstractTimer.getTimer());

	  c.right();
	  c.tick();
	  assertEquals("value of memTimer ", 8, AbstractTimer.getMemTimer());
	  assertEquals("value of timer ", 7, AbstractTimer.getTimer());
	  c.up(); // start running the timer
	  c.tick();
	  
	  
	  c.up(); // pause the timer
	  c.tick();
	  assertSame(PausedTimer.Instance(), c.currentState);
	  assertEquals("value of memTimer ", 8, AbstractTimer.getMemTimer());
	  assertEquals("value of timer ", 7, AbstractTimer.getTimer());

	  
	  c.left(); // go to stopwatch mode
	  c.tick();
	  assertSame(ResetStopwatch.Instance(), c.currentState);
	  assertEquals("value of totalTime ", 0, AbstractStopwatch.getTotalTime());
	  assertEquals("value of lapTime ", 0, AbstractStopwatch.getLapTime());
	  
	  c.up(); //start running the stopwatch
	  c.tick();
	  assertSame(RunningStopwatch.Instance(), c.currentState);
	  assertEquals("value of totalTime ", 1, AbstractStopwatch.getTotalTime());
	  assertEquals("value of lapTime ", 0, AbstractStopwatch.getLapTime());

	  c.right(); // reset
	  c.tick();
	  assertEquals("value of totalTime ", 0, AbstractStopwatch.getTotalTime());
	  c.up(); //start running the stopwatch
	  c.tick();

	  c.up(); // record stopwatch laptime
	  c.tick();
	  assertSame(LaptimeStopwatch.Instance(), c.currentState);
	  assertEquals("value of totalTime ", 2, AbstractStopwatch.getTotalTime());
	  assertEquals("value of lapTime ", 1, AbstractStopwatch.getLapTime());

	  //next part of tp
	  c.up();
	  c.tick();
	  assertSame(RunningStopwatch.Instance(), c.currentState);
	  c.up();
	  c.tick();
	  assertSame(c.getUpText(), "unsplit");
	  assertEquals(c.getDisplayText(), "lapTime = 3");

	  c.left(); // go back to timer mode (remembering history state)
	  c.tick();
	  assertSame(PausedTimer.Instance(), c.currentState);
	  assertEquals("value of memTimer ", 8, AbstractTimer.getMemTimer());
	  assertEquals("value of timer ", 7, AbstractTimer.getTimer());
	  
	  c.up(); // continue running timer
	  assertSame(RunningTimer.Instance(), c.currentState);
	  c.tick();
	  c.tick();
	  c.tick();
	  c.tick();
	  c.tick();
	  c.tick();
	  c.tick();
	  c.tick();
	  //automatic switch to ringing timer since timer has reached 0...
	  assertSame(RingingTimer.Instance(), c.currentState);
	  assertEquals("value of memTimer ", 8, AbstractTimer.getMemTimer());
	  assertEquals("value of timer ", 0, AbstractTimer.getTimer());	 
	  
	  c.right(); // return to idle timer state
	  c.tick();
	  assertSame(IdleTimer.Instance(), c.currentState);
	  assertEquals("value of memTimer ", 8, AbstractTimer.getMemTimer());
	  assertEquals("value of timer ", 0, AbstractTimer.getTimer());



	  }

}
