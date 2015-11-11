package game.bullcow;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class BullCow {
	private byte n;
	private Vector<Answer> lsts;
	private Chislo lastAsk;

	public BullCow(byte digitNumber) {
		n = digitNumber;
		lsts = new Vector<Answer>();
	}

	public BullCow() {
		this((byte) 4);
	}
	
	public void reset() {
		lsts.clear();
	}

	private Chislo randomNumber() throws Chislo.InitializationException {
		Chislo a;
		Random rand = new Random();
		while (true) {
			double degree = Math.pow(10, n - 1);
			a = new Chislo(Integer.toString(rand.nextInt(9 * (int) degree)
					+ (int) degree));
			if (lsts.isEmpty()) {
				if (a.boolrep()) {
					break;
				}
			} else {
				if (a.boolrep() && a.numberMatch(lsts) && a.checkList(lsts)) {
					break;
				}
			}
		}
		return a;
	}

	private Vector<Chislo> filterBull(Vector<Answer> lsts,
			Vector<String> listBull) throws Chislo.InitializationException {
		Vector<Chislo> l = new Vector<Chislo>();
		for (int j = 0; j < listBull.size(); j++) {
			Chislo a = new Chislo(listBull.elementAt(j));
			if (a.checkBull(lsts)) {
				l.add(a);
			}
		}
		return l;
	}

	private Vector<String> genList(Vector<Integer> lst) {
		Vector<String> gen = new Vector<String>();

		String strTemp = "";
		for (Integer elem : lst) {
			strTemp += elem.toString();
		}
		gen.add(strTemp);

		while (true) {
			int maxi = 0;
			int minc = Collections.max(lst); // максимальный элемент
			int a = -1;
			for (int i = 0; i < lst.size(); i++) {
				for (int j = i + 1; j < lst.size(); j++) {
					if (lst.elementAt(i) < lst.elementAt(j)) {
						if (maxi <= i) {
							maxi = i;
							a = lst.elementAt(maxi);
						}
					}
				}
			}
			if (a == -1) {
				break;
			}
			for (int i = maxi + 1; i < lst.size(); i++) {
				if (lst.elementAt(i) > a) {
					if (lst.elementAt(i) < minc) {
						minc = lst.elementAt(i);
					}
				}
			}
			int mini = lst.indexOf(minc);
			int q = lst.elementAt(maxi);
			lst.setElementAt(lst.elementAt(mini).intValue(), maxi);
			lst.setElementAt(q, mini);
			List<Integer> left = lst.subList(0, maxi + 1);

			List<Integer> right = lst.subList(maxi + 1, lst.size());
			Collections.sort(right);

			@SuppressWarnings("unchecked")
			Vector<Integer> lstTemp = (Vector<Integer>) lst.clone();
			lstTemp.clear();
			strTemp = "";
			for (int elem : left) {

				strTemp += Integer.toString(elem);
				lstTemp.add(elem);
			}
			for (int elem : right) {
				strTemp += Integer.toString(elem);
				lstTemp.add(elem);
			}
			gen.add(strTemp);
			lst = lstTemp;
		}

		return gen;
	}

	public String getNumber() {
		if (lastAsk == null) {
			return "";
		}
		return lastAsk.toString();
	}

	public boolean askNew() {
		try {
			if (lsts.isEmpty()) {
				Chislo a = randomNumber();
				lastAsk = a;
				return false;
			} else {
				if (lsts.elementAt(0).bull + lsts.elementAt(0).cow == n) {
					Vector<Chislo> lst = filterBull(lsts,
							genList(lsts.elementAt(0).number.reprlist()));
					if (lst.size() == 1) {
						lastAsk = lst.elementAt(0);
						return true;
					}
					Random rnd = new Random();
					Chislo a = lst.elementAt(rnd.nextInt(lst.size()));
					lastAsk = a;
					return false;
				} else {
					Chislo a = randomNumber();
					lastAsk = a;
					return false;
				}
			}
		} catch (Chislo.InitializationException e1) {
			e1.printStackTrace();
			System.out.println(e1);
		}
		return false;
	}

	public boolean answer(byte bull, byte cow) {
		if (lastAsk != null) {
			Answer ans = new Answer(lastAsk, bull, cow);

			lsts.add(0, ans);
			lastAsk = null;
			return true;
		}
		return false;
	}
}