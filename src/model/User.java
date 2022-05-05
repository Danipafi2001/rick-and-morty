package model;

import java.io.Serializable;

public class User implements Comparable<User>, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int score;
	
	public User(String n) {
		name = n;
		score = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public void plusScore(int s) {
		score = score + s;
	}
	
	public String toString() {
		return String.format("%-20s", name) + " " + String.format("%-20s", score);
	}
	
	public String save() {
		return name+","+score;
	}

	@Override
	public int compareTo(User user) {
		int output = score - user.score;
		if(output == 0)
			output = user.name.compareTo(name);
		return output;
	}
}
