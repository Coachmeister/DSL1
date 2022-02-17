package main.metamodel;

import java.util.*;

public class Machine {
	public List<State> states = new ArrayList<>();
	public State initialState;
	public Map<String, Integer> integers = new HashMap<>();
	public State current;

	public Machine() {

	}

	public List<State> getStates() {
		return states;
	}

	public State getInitialState() {
		return initialState;
	}

	public State getState(String string) {
		for (State s : states){
			if(s.getName() == string){
				return s;
			};
		};
		return null;
	}

	public int numberOfIntegers() {
		return integers.size();
	}

	public boolean hasInteger(String string) {
		return integers.containsKey(string);
	}

}
