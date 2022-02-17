package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.StateMachine;
import main.metamodel.Machine;
import main.metamodel.State;
import main.metamodel.Transition;

public class MachineStructureTest {
	private StateMachine stateMachine;
	
	@BeforeEach
	public void init() {
		stateMachine = new StateMachine();
	}
	
	@Test
	public void emptyMachine() {
		Machine m = stateMachine.build();
		
		assertTrue(m.getStates().isEmpty());
	}
	
	@Test
	public void states() {
		Machine m = stateMachine.
						state("state 1").
						state("state 2").
						state("state 3").
						build();
		List<State> states = m.getStates();
		assertEquals(3, m.getStates().size());
		assertTrue(states.stream().anyMatch(state -> state.getName().equals("state 1")));
		assertTrue(states.stream().anyMatch(state -> state.getName().equals("state 2")));
		assertTrue(states.stream().anyMatch(state -> state.getName().equals("state 3")));
	}
	
	@Test
	public void initialFirstState() {
		Machine m = stateMachine.
				state("state 1").initial().
				state("state 2").
				state("state 3").
				build();

		assertEquals("state 1", m.getInitialState().getName());
	}
	
	@Test
	public void initialState() {
		Machine m = stateMachine.
				state("state 1").
				state("state 2").initial().
				state("state 3").
				build();

		assertEquals("state 2", m.getInitialState().getName());		
	}
	
	@Test
	public void getState() {
		Machine m = stateMachine.
				state("state 1").
				state("state 2").initial().
				state("state 3").
				build();
		assertEquals("state 2", m.getState("state 2").getName());
	}
	
	@Test
	public void noTransitions() {
		Machine m = stateMachine.
				state("state 1").
				build();
		
		State state = m.getState("state 1");
		List<Transition> transitions = state.getTransitions();
		assertTrue(transitions.isEmpty());
	}
	
	@Test
	public void transitions() {
		Machine m = stateMachine.
					state("state 1").
						when("change to 2").to("state 2").
						when("change to 3").to("state 3").
					state("state 2").
						when("change to 3").to("state 3").
					state("state 3").
					build();
		State state = m.getState("state 1");
		List<Transition> transitions = state.getTransitions();
		assertEquals(2, transitions.size());
		assertTrue(transitions.stream().anyMatch(transition -> transition.getEvent().equals("change to 2")));
		assertEquals("state 2", state.getTransitionByEvent("change to 2").getTarget().getName());
		assertTrue(transitions.stream().anyMatch(transition -> transition.getEvent().equals("change to 3")));
		assertEquals("state 3", state.getTransitionByEvent("change to 3").getTarget().getName());
		
		state = m.getState("state 2");
		transitions = state.getTransitions();
		assertEquals(1, transitions.size());
		assertTrue(transitions.stream().anyMatch(transition -> transition.getEvent().equals("change to 3")));
		assertEquals("state 3", state.getTransitionByEvent("change to 3").getTarget().getName());
	}
	
	@Test
	public void noVariables() {
		Machine m = stateMachine.build();
		assertEquals(0, m.numberOfIntegers());
	}
	
	@Test
	public void addVariable() {
		Machine m = stateMachine.
					integer("var").
					build();
		assertEquals(1, m.numberOfIntegers());
		assertTrue(m.hasInteger("var"));
		assertFalse(m.hasInteger("var 2"));
	}
	
	@Test
	public void transitionSetVariable() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").
						when("SET").to("state 2").set("var", 42).
					state("state 2").
					build();
		Transition transition = m.getState("state 1").getTransitions().get(0);
		assertTrue(transition.hasSetOperation());
		assertFalse(transition.hasIncrementOperation());
		assertFalse(transition.hasDecrementOperation());
		assertEquals("var", transition.getOperationVariableName());
	}
	
	@Test
	public void transitionIncrementVariable() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").
						when("SET").to("state 2").increment("var").
					state("state 2").
					build();
		Transition transition = m.getState("state 1").getTransitions().get(0);
		assertFalse(transition.hasSetOperation());
		assertTrue(transition.hasIncrementOperation());
		assertFalse(transition.hasDecrementOperation());
		assertEquals("var", transition.getOperationVariableName());
	}
	
	@Test
	public void transitionDecrementVariable() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").
						when("SET").to("state 2").decrement("var").
					state("state 2").
					build();
		Transition transition = m.getState("state 1").getTransitions().get(0);
		assertFalse(transition.hasSetOperation());
		assertFalse(transition.hasIncrementOperation());
		assertTrue(transition.hasDecrementOperation());
		assertEquals("var", transition.getOperationVariableName());
	}

	@Test
	public void transitionIfVariableEqual() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").
						when("GO").to("state 2").ifEquals("var", 42).
					state("state 2").
					build();
		State state = m.getState("state 1");
		
		Transition transition = state.getTransitions().get(0);
		assertTrue(transition.isConditional());
		assertEquals("var", transition.getConditionVariableName());
		assertEquals(42, transition.getConditionComparedValue());
		assertTrue(transition.isConditionEqual());
		assertFalse(transition.isConditionGreaterThan());
		assertFalse(transition.isConditionLessThan());
	}
	
	@Test
	public void transitionIfVariableGreaterThan() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").
						when("GO").to("state 2").ifGreaterThan("var", 42).
					state("state 2").
					build();
		State state = m.getState("state 1");
		
		Transition transition = state.getTransitions().get(0);
		assertTrue(transition.isConditional());
		assertEquals("var", transition.getConditionVariableName());
		assertEquals(42, transition.getConditionComparedValue());
		assertFalse(transition.isConditionEqual());
		assertTrue(transition.isConditionGreaterThan());
		assertFalse(transition.isConditionLessThan());
	}
	
	@Test
	public void transitionIfVariableLessThan() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").
						when("GO").to("state 2").ifLessThan("var", 42).
					state("state 2").
					build();
		State state = m.getState("state 1");
		
		Transition transition = state.getTransitions().get(0);
		assertTrue(transition.isConditional());
		assertEquals("var", transition.getConditionVariableName());
		assertEquals(42, transition.getConditionComparedValue());
		assertFalse(transition.isConditionEqual());
		assertFalse(transition.isConditionGreaterThan());
		assertTrue(transition.isConditionLessThan());
	}

	@Test
	public void transitionIfVariableEqualsAndSet() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").
						when("GO").to("state 2").set("var", 10).ifEquals("var", 42).
					state("state 2").
					build();
		State state = m.getState("state 1");
		
		Transition transition = state.getTransitions().get(0);
		assertTrue(transition.isConditional());
		assertTrue(transition.hasSetOperation());
	}
	
	@Test
	public void transitionIfVariableGreaterAndIncrement() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").
						when("GO").to("state 2").increment("var").ifGreaterThan("var", 42).
					state("state 2").
					build();
		State state = m.getState("state 1");
		
		Transition transition = state.getTransitions().get(0);
		assertTrue(transition.isConditional());
		assertTrue(transition.hasOperation());
	}

	@Test
	public void transitionIfVariableLessAndDecrement() {
		Machine m = stateMachine.
					integer("var").
					state("state 1").
						when("GO").to("state 2").decrement("var").ifLessThan("var", 42).
					state("state 2").
					build();
		State state = m.getState("state 1");
		
		Transition transition = state.getTransitions().get(0);
		assertTrue(transition.isConditional());
		assertTrue(transition.hasOperation());
	}
}
