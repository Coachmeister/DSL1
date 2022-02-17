package main.metamodel;

public class Transition {

	public String event;
	public State target;
	public boolean set = false;
	public boolean increment = false;
	public boolean decrement = false;
	public boolean isconditional;
	public boolean conditionalEqual = false;
	public boolean conditionalgreater = false;
	public boolean conditionalsmaller = false;
	public Object operationVarName = null;
	public Object conditionVarName = null;
	public Integer conditionCompareValue;
	public Integer setInt;

	public Transition(String event) {
		this.event = event;

	}

	public Object getEvent() {
		return event;
	}

	public State getTarget() {

		return target;
	}

	public boolean hasSetOperation() {

		return set;
	}

	public boolean hasIncrementOperation() {

		return increment;
	}

	public boolean hasDecrementOperation() {

		return decrement;
	}

	public Object getOperationVariableName() {

		return operationVarName;
	}

	public boolean isConditional() {

		return (conditionalEqual || conditionalgreater || conditionalsmaller);
	}

	public Object getConditionVariableName() {

		return conditionVarName;
	}

	public Integer getConditionComparedValue() {

		return conditionCompareValue;
	}

	public boolean isConditionEqual() {

		return conditionalEqual;
	}

	public boolean isConditionGreaterThan() {

		return conditionalgreater;
	}

	public boolean isConditionLessThan() {

		return conditionalsmaller;
	}

	public boolean hasOperation() {

		return (set || increment || decrement);
	}

}
