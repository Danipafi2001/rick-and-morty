package model;

public class Place {
	private int num;
	private boolean seed;
	private String letter;
	
	private Place prev;
	private Place next;
	private Place portal;
	
	public Place(int n, boolean s) {
		num = n;
		seed = s;
	}

	public int getNum() {
		return num;
	}

	public boolean getSeed() {
		return seed;
	}

	public void setSeed(boolean s) {
		seed = s;
	}

	public Place getPrev() {
		return prev;
	}

	public void setPrev(Place p) {
		prev = p;
	}

	public Place getNext() {
		return next;
	}

	public void setNext(Place n) {
		next = n;
	}

	public Place getPortal() {
		return portal;
	}

	public void setPortal(Place p) {
		portal = p;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String l) {
		letter = l;
	}
}
