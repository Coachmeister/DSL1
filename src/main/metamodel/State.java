package main.metamodel;


import java.util.ArrayList;
import java.util.List;

public class State {

	public String name;
	public List<Transition> trans = new ArrayList<>();

	public State(String name) {
		this.name = name;
		this.trans = new ArrayList<>();
	}

	public Object getName() {

		return name;
	}

	public List<Transition> getTransitions() {
		return trans;
	}

	public Transition getTransitionByEvent(String string) {
		for (Transition t : trans){
			if (t.getEvent() == string){
				return t;

			}
		}
		return null;
	}

}
