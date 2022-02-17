package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.MachineInterpreter;
import main.StateMachine;
import main.metamodel.Machine;

public class MachineInterpreterTest {
	private StateMachine stateMachine;
	private MachineInterpreter interpreter;
	
	@BeforeEach
	public void init() {
		stateMachine = new StateMachine();
		interpreter = new MachineInterpreter();
	}
	
	
	@Test
	public void startInitState() {
		Machine m = stateMachine.
					state("state 1").initial().
					state("state 2").
					build();
		interpreter.run(m);
		assertEquals("state 1", interpreter.getCurrentState().getName());
	}
	
	@Test
	public void eventNoTransition() {
		Machine m = stateMachine.
				state("state 1").initial().
					when("FIRE").to("state 2").
				state("state 2").
				build();
		interpreter.run(m);
		interpreter.processEvent("to 2");
		assertEquals("state 1", interpreter.getCurrentState().getName());
	}
	
	@Test
	public void eventTransition() {
		Machine m = stateMachine.
				state("state 1").initial().
					when("FIRE").to("state 2").
				state("state 2").
				build();
		interpreter.run(m);
		interpreter.processEvent("FIRE");
		assertEquals("state 2", interpreter.getCurrentState().getName());
	}
	
	@Test
	public void listOfEvents() {
		Machine m = stateMachine.
				state("state 1").initial().
					when("ON").to("state 2").
				state("state 2").
					when("GO").to("state 3").
				state("state 3").
				build();
		interpreter.run(m);
		interpreter.processEvent("ON");
		interpreter.processEvent("GO");
		assertEquals("state 3", interpreter.getCurrentState().getName());
	}
	
	@Test
	public void chooseTransition() {
		Machine m = stateMachine.
				state("state 1").initial().
					when("FIRE2").to("state 2").
					when("FIRE3").to("state 3").
					when("FIRE4").to("state 4").
				state("state 2").
				state("state 3").
				state("state 4").
				build();
		interpreter.run(m);
		interpreter.processEvent("FIRE3");
		assertEquals("state 3", interpreter.getCurrentState().getName());
	}
	
	@Test
	public void initVariable() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").initial().
					build();
		interpreter.run(m);
		assertEquals(0, interpreter.getInteger("var"));
	}
	
	@Test
	public void transitionSetVariable() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").initial().
						when("SET").to("state 2").set("var", 42).
					state("state 2").
					build();
		interpreter.run(m);
		interpreter.processEvent("SET");
		assertEquals(42, interpreter.getInteger("var"));
	}
	
	@Test
	public void transitionIncrementVariable() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").initial().
						when("SET").to("state 2").increment("var").
					state("state 2").
					build();
		interpreter.run(m);
		interpreter.processEvent("SET");
		assertEquals(1, interpreter.getInteger("var"));
	}
	
	@Test
	public void transitionDecrementVariable() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").initial().
						when("SET").to("state 2").decrement("var").
					state("state 2").
					build();
		interpreter.run(m);
		interpreter.processEvent("SET");
		assertEquals(-1, interpreter.getInteger("var"));
	}
	
	@Test
	public void transitionIfVariableEqual() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").initial().
						when("GO").to("state 2").set("var", 42).
					state("state 2").
						when("GO").to("state 3").ifEquals("var", 42).
					state("state 3").
					build();
		interpreter.run(m);
		interpreter.processEvent("GO");
		interpreter.processEvent("GO");
		assertEquals("state 3", interpreter.getCurrentState().getName());
	}

	@Test
	public void transitionIfVariableNotEqual() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").initial().
						when("GO").to("state 2").set("var", 42).
					state("state 2").
						when("GO").to("state 3").ifEquals("var", 40).
					state("state 3").
					build();
		interpreter.run(m);
		interpreter.processEvent("GO");
		interpreter.processEvent("GO");
		assertEquals("state 2", interpreter.getCurrentState().getName());
	}
	
	@Test
	public void transitionIfVariableGreaterThan() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").initial().
						when("GO").to("state 2").set("var", 42).
					state("state 2").
						when("GO").to("state 3").ifGreaterThan("var", 40).
					state("state 3").
					build();
		interpreter.run(m);
		interpreter.processEvent("GO");
		interpreter.processEvent("GO");
		assertEquals("state 3", interpreter.getCurrentState().getName());
	}

	@Test
	public void transitionIfVariableNotGreaterThan() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").initial().
						when("GO").to("state 2").set("var", 42).
					state("state 2").
						when("GO").to("state 3").ifGreaterThan("var", 42).
					state("state 3").
					build();
		interpreter.run(m);
		interpreter.processEvent("GO");
		interpreter.processEvent("GO");
		assertEquals("state 2", interpreter.getCurrentState().getName());
	}
	
	@Test
	public void transitionIfVariableLessThan() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").initial().
						when("GO").to("state 2").set("var", 42).
					state("state 2").
						when("GO").to("state 3").ifLessThan("var", 45).
					state("state 3").
					build();
		interpreter.run(m);
		interpreter.processEvent("GO");
		interpreter.processEvent("GO");
		assertEquals("state 3", interpreter.getCurrentState().getName());
	}

	@Test
	public void transitionIfVariableNotLessThan() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").initial().
						when("GO").to("state 2").set("var", 42).
					state("state 2").
						when("GO").to("state 3").ifLessThan("var", 42).
					state("state 3").
					build();
		interpreter.run(m);
		interpreter.processEvent("GO");
		interpreter.processEvent("GO");
		assertEquals("state 2", interpreter.getCurrentState().getName());
	}
	
	@Test
	public void transitionIfVariableEqualsAndSet() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").initial().
						when("GO").to("state 2").set("var", 42).ifEquals("var", 0).
					state("state 2").
					build();
		interpreter.run(m);
		interpreter.processEvent("GO");
		assertEquals(42, interpreter.getInteger("var"));
	}
	
	@Test
	public void transitionIfVariableGreaterAndIncrement() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").initial().
						when("GO").to("state 2").increment("var").ifGreaterThan("var", -1).
					state("state 2").
					build();
		interpreter.run(m);
		interpreter.processEvent("GO");
		assertEquals(1, interpreter.getInteger("var"));
	}

	@Test
	public void transitionIfVariableLessAndDecrement() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").initial().
						when("GO").to("state 2").decrement("var").ifLessThan("var", 1).
					state("state 2").
					build();
		interpreter.run(m);
		interpreter.processEvent("GO");
		assertEquals(-1, interpreter.getInteger("var"));
	}
	
	@Test
	public void transitionOrder() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").initial().
						when("GO").to("state 2").increment("var").
						when("GO").to("state 2").decrement("var").
					state("state 2").
					build();
		interpreter.run(m);
		interpreter.processEvent("GO");
		assertEquals(1, interpreter.getInteger("var"));
	}
}
