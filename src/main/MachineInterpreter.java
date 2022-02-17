package main;

import main.metamodel.Machine;
import main.metamodel.State;
import main.metamodel.Transition;

import java.util.*;


public class MachineInterpreter {
	public Machine machine;


	public void run(Machine m) {
		this.machine = m;
		
	}

	public State getCurrentState() {
		return machine.current;
	}

	public void processEvent(String string) {

		List<Transition> transi = new ArrayList<>();

			for (Transition t : machine.current.getTransitions()) {
				if (t.getEvent().equals(string)) {
					transi.add(t);

				}
			}


			for (Transition trans : transi) {
				boolean bool = true;
				if(trans.isConditional()) {
					var conditional = machine.integers.get(trans.getConditionVariableName());
					if (trans.conditionalEqual) {
						System.out.println("HEEEEEEEEERE");
						bool = Objects.equals(conditional, trans.conditionCompareValue);
					}
					else if (trans.conditionalgreater) {
						bool = (conditional > trans.conditionCompareValue);
					}
					else if (trans.conditionalsmaller) {
						bool = (conditional < trans.conditionCompareValue);

					}

				}
				if (trans.hasSetOperation() && bool) {
					this.machine.integers.put((String) trans.operationVarName, trans.setInt);
				} else if (trans.hasIncrementOperation()) {
					this.machine.integers.put((String) trans.operationVarName, machine.integers.get(trans.operationVarName) + 1);
				} else if (trans.hasDecrementOperation()) {
					this.machine.integers.put((String) trans.operationVarName, machine.integers.get(trans.operationVarName) - 1);
				}

						System.out.println(bool);
						if(bool){

							 this.machine.current = trans.target;
							 return;

						}





				//this.machine.current = trans.getTarget();
				//this.machine.current = this.machine.states.stream().filter(state -> state.getName().equals(trans.target.getName())).findFirst().get();

				//return;
			}
			}
		


	public int getInteger(String string) {

		return machine.integers.get(string);
	}

}
