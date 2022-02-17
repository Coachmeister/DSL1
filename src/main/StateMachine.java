package main;

import main.metamodel.Machine;
import main.metamodel.State;
import main.metamodel.Transition;

import java.util.*;

public class StateMachine {
	public Machine machine = new Machine();
	public Transition currentTrans = null;
	public State currentState = null;


	public Machine build() {
		//if (machine.initialState != null) {
		//	machine.initialState = machine.states.get(0);

		//}
		machine.current = machine.initialState;
		return machine;
	}

	public StateMachine state(String string) {

		currentState = getState(string);
		//machine.states.add(currentState);

		return this;

	}

	public StateMachine initial() {
		machine.initialState = currentState;
		return this;
	}

	public StateMachine when(String string) {
		currentTrans = new Transition(string);
		currentState.trans.add(currentTrans);
		return this;
	}

	public State getState(String string){
		for(State s : machine.states){
			if(s.getName() == string){
				return s;
			}
		}
		State state = new State(string);
		machine.states.add(state);
		return state;
	}

	public StateMachine to(String string) {

		currentTrans.target = getState(string);
		return this;
	}

	public StateMachine integer(String string) {
		machine.integers.put(string, 0);

		return this;
	}

	public StateMachine set(String string, int i) {
		currentTrans.set = true;
		currentTrans.setInt = i;
		currentTrans.operationVarName = string;
		return this;
	}

	public StateMachine increment(String string) {
		currentTrans.increment = true;
		currentTrans.operationVarName = string;
		return this;
	}

	public StateMachine decrement(String string) {
		currentTrans.decrement = true;
		currentTrans.operationVarName = string;
		return this;
	}

	public StateMachine ifEquals(String string, int i) {
		currentTrans.conditionVarName = string;
		currentTrans.conditionalEqual = true;
		currentTrans.conditionCompareValue = i;
		return this;
	}

	public StateMachine ifGreaterThan(String string, int i) {
		currentTrans.conditionVarName = string;
		currentTrans.conditionalgreater = true;
		currentTrans.conditionCompareValue = i;
		return this;
	}

	public StateMachine ifLessThan(String string, int i) {
		currentTrans.conditionVarName = string;
		currentTrans.conditionalsmaller = true;
		currentTrans.conditionCompareValue = i;
		return this;
	}

}
