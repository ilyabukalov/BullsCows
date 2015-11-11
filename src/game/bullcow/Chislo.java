package game.bullcow;

import java.util.Collections;
import java.util.Vector;

public class Chislo {
	private Vector<Integer> mas;
	private int capacity;

	public static class InitializationException extends Exception {

		private static final long serialVersionUID = 5530419115212302366L;

		public String toString() {
			return "Initialization error";
		}
	}

	Chislo(String number) throws InitializationException {
		capacity = number.length();
		mas = new Vector<Integer>();

		for (int i = 0; i < capacity; i++) {
			char c = number.charAt(i);
			if (!Character.isDigit(c)) {
				throw new InitializationException();
			}
			mas.add(Integer.parseInt(Character.toString(c)));
		}
	}

	Vector<Integer> reprlist() {
		Vector<Integer> l = new Vector<Integer>(mas);
		Collections.sort(l);

		return l;
	}

	public String toString() {
		String strng = "";
		for (int i = 0; i < capacity; i++) {
			strng += mas.elementAt(i).toString();

		}
		return strng;
	}

	boolean boolrep() {
		for (int i = 0; i < capacity; i++) {
			for (int j = i + 1; j < capacity; j++) {
				if (mas.elementAt(i) == mas.elementAt(j))
					return false;
			}
		}
		return true;
	}

	boolean numberMatch(Vector<Answer> lsts) {
		int l = 0;
		for (int q = 0; q < lsts.size(); q++) {
			int k = 0;
			for (int i = 0; i < capacity; i++) {
				for (int j = 0; j < lsts.elementAt(q).number.capacity; j++) {
					if (mas.elementAt(i) == lsts.elementAt(q).number.mas
							.elementAt(j)) {
						k++;
					}
				}
			}
			if (k == lsts.elementAt(q).bull + lsts.elementAt(q).cow) {
				l++;
			}

		}
		if (l == lsts.size()) {
			return true;
		} else {
			return false;
		}
	}

	private Vector<Integer> listRepeated(Chislo a) { // выдаЄт список из
														// повтор€ющихс€
														// элементов

		Vector<Integer> ret = new Vector<Integer>();
		for (int i = 0; i < capacity; i++) {
			for (int j = 0; j < a.capacity; j++) {

				if (mas.elementAt(i) == mas.elementAt(j)) {
					ret.add(mas.elementAt(i));
				}
			}
		}

		return ret;
	}

	boolean checkBull(Vector<Answer> lsts) {
		int w = 0;
		for (int k = 0; k < lsts.size(); k++) {
			int q = 0;
			for (int i = 0; i < capacity; i++) {
				for (int j = 0; j < lsts.elementAt(k).number.capacity; j++) {
					if (mas.elementAt(i) == lsts.elementAt(k).number.mas
							.elementAt(j) && i == j) {
						q++;
					}
				}
			}

			if (q == lsts.elementAt(k).bull) {
				w++;
			}
		}
		if (w == lsts.size()) {
			return true;
		} else {
			return false;
		}
	}

	boolean checkList(Vector<Answer> lsts) {
		for (int i = 0; i < lsts.size(); i++) {
			for (int j = i + 1; j < lsts.size(); j++) {
				Answer ansI = lsts.elementAt(i);
				Answer ansJ = lsts.elementAt(j);
				if (ansI.bull + ansI.cow < ansJ.bull + ansJ.cow) {
					Vector<Integer> l = ansI.number.listRepeated(ansJ.number);
					int q = 0;
					for (int k = 0; k < mas.size(); k++) {
						if (l.contains(mas.elementAt(k))) {
							q++;
						}
					}

					if (q == l.size()) {
						return false;
					}
				}
			}
		}

		return true;
	}
}