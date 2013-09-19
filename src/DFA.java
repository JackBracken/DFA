/*
 *  @author Jack Bracken
 *  DFA to recognise the regular expression (0|1 0* 1 0* 1)*
 *  using enums.
 */

interface State {
	public State next(Token word);
}

interface FinalState extends State {
}

class Token {
	private String input;
	private int current;

	public Token(String input) {
		this.input = input;
	}

	char read() {
		return input.charAt(current++);
	}

	boolean hasNext() {
		return current < input.length();
	}
}

enum AcceptingState implements FinalState {
	Accept {
		@Override
		public State next(Token word) {
			return Accept;
		}
	};
}

enum FailingState implements FinalState {
	Fail {
		@Override
		public State next(Token word) {
			return Fail;
		}
	};
}

enum States implements State {
	A {
		@Override
		public State next(Token word) {
			if (word.hasNext()) {
				switch (word.read()) {
				case '0':
					return A2;
				case '1':
					return B;
				default:
					return FailingState.Fail;
				}
			}
			return AcceptingState.Accept;
		}
	},
	
	A2 {
		@Override
		public State next(Token word) {
			if (word.hasNext()) {
				switch (word.read()) {
				case '0':
					return A2;
				default:
					return FailingState.Fail;
				}
			}
			return AcceptingState.Accept;
		}
		
	},

	B {
		@Override
		public State next(Token word) {
			if (word.hasNext()) {
				switch (word.read()) {
				case '0':
					return B;
				case '1':
					return C;
				default:
					return FailingState.Fail;
				}
			}
			return FailingState.Fail;
		}
	},
	C {
		@Override
		public State next(Token word) {
			if (word.hasNext()) {
				switch (word.read()) {
				case '0':
					return C;
				case '1':
					return A;
				default:
					return FailingState.Fail;
				}
			}
			return FailingState.Fail;
		}
	};

	public abstract State next(Token word);
}

public class DFA {
	public static void main(String[] args) {
		State s;
		Token in = new Token("111");
		for (s = States.A; !(s instanceof FinalState); s = s.next(in)) {}
		System.out.println(s);
	}
}