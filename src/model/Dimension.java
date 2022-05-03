package model;

import java.util.Random;

public class Dimension {

	private String format;
	private int cols, rows, seeds, rSeeds, mSeeds;
	private Place head, rick, morty;

	public Dimension(int n, int m, int q) {
		cols = n;
		rows = m;
		seeds = q;
		rSeeds = 0;
		mSeeds = 0;
		head = new Place(1, false);
		head.setNext(head);
		head.setPrev(head);
		fill(n*m);
		format = "%-"+(Integer.toString(head.getPrev().getNum()).length()+2)+"s ";
		putSeeds(head, q);
	}

	private void putSeeds(Place current, int seeds) {
		if((!current.getSeed()) && (seeds>0)) {
			Random random = new Random();
			if(random.nextBoolean() && random.nextBoolean() && random.nextBoolean()) {
				current.setSeed(true);
				seeds--;
			}
		}
		if(seeds>0) {
			putSeeds(current.getNext(), seeds);
		}
	}

	private void fill(int s) {
		int n = head.getPrev().getNum()+1;
		if(n<=s) {
			Place temp = new Place(n, false);
			temp.setPrev(head.getPrev());
			head.getPrev().setNext(temp);
			temp.setNext(head);
			head.setPrev(temp);
			fill(s);
		}
	}

	public void putPortals(int portals) {
		putPortals(head, portals);
	}

	private void putPortals(Place current, int portals) {
		if(current.getPortal()==null && (portals>0)) {
			Random random = new Random();
			if(random.nextBoolean()) {
				Place temp = getRandomPlace(current);
				if(temp.getPortal()==null && !current.equals(temp)) {
					current.setPortal(temp);
					temp.setPortal(current);
					current.setLetter((char)('A'+portals-1)+"");
					temp.setLetter((char)('A'+portals-1)+"");
					portals--;
				}
			}
		}
		if(portals>0) {
			putPortals(current.getNext(), portals);
		}
	}

	public void putRickMorty() {
		putRickMorty(head);
	}

	private void putRickMorty(Place current) {
		rick = getRandomPlace(current);
		morty = getRandomPlace(rick);
		if(rick.equals(morty) || rick.getSeed() || morty.getSeed()) {
			putRickMorty(morty);
		}
	}

	private Place getRandomPlace(Place current) {
		Random random = new Random();
		if(random.nextBoolean() && random.nextBoolean() && random.nextBoolean()) {
			return current;
		}
		return getRandomPlace(current.getNext());
	}

	public int throwDice() {
		Random random = new Random();
		int n = random.nextInt(6)+1;
		return n;
	}
	
	public boolean turn(boolean player, boolean advance, int n) {
		Place temp = null;
		if(player)
			temp = rick;
		else
			temp = morty;
		temp = movePlaces(temp, advance, n);
		int seeds = 0;
		if(temp.getSeed()) {
			temp.setSeed(false);
			seeds++;
		}
		if(temp.getPortal()!=null)
			temp = temp.getPortal();
		if(temp.getSeed()){
			temp.setSeed(false);
			seeds++;
		}
		if(player) {
			rick = temp;
			rSeeds += seeds;
		}
		else {
			morty = temp;
			mSeeds += seeds;
		}
		this.seeds -= seeds;
		if(this.seeds == 0)
			return true;
		else
			return false;
	}

	private Place movePlaces(Place temp, boolean advance, int n) {
		if(advance)
			temp = temp.getNext();
		else
			temp = temp.getPrev();
		n--;
		if(n>0)
			return movePlaces(temp, advance, n);
		else
			return temp;
	}

	public String print(boolean print) {
		return print("", "", head, false, print);
	}

	private String print(String m, String t, Place place, boolean reverse, boolean print) {
		String n = "";

		if(print) {
			if(place.equals(rick) && rick.equals(morty))
				n += "X";
			else if(place.equals(rick))
				n += "R";
			else if(place.equals(morty))
				n += "M";
			else if(place.getSeed())
				n += "*";
			else
				n += place.getNum();
			n = String.format(format, "[" + n + "]");
		} else {
			if(place.getPortal()!=null)
				n += place.getLetter();
			else
				n += " ";
			n = "[" + n + "] ";
		}

		if(reverse)
			t =  n + t;
		else
			m += n;

		if(place.getNum()%cols == 0) {
			m += t + "\n";
			t = "";
			reverse = !reverse;
		}

		if(place.getNum()==cols*rows)
			return m;
		else
			return print(m, t, place.getNext(), reverse, print);
	}

	public int getmSeeds() {
		return mSeeds;
	}

	public int getrSeeds() {
		return rSeeds;
	}
}