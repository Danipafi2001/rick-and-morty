package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Scores {

	private ArrayList<User> scores;

	public Scores() throws FileNotFoundException, ClassNotFoundException, IOException {
		scores = new ArrayList<>();
		importScores();
	}

	public void addScore(String n, int s) {
		boolean stop = false;
		for(int i = 0; i<scores.size() && !stop; i++) {
			if(n.equals(scores.get(i).getName())) {
				scores.get(i).plusScore(s);
				stop = true;
			}
		}
		if(!stop) {
			User temp = new User(n);
			temp.plusScore(s);
			scores.add(temp);
		}
	}

	public String print() {
		String msg = "\nTOP 5 SCORES\n\n";
		msg += String.format("%-20s", "USERNAME") + " " + String.format("%-20s", "SCORE") + "\n\n";
		for(int i = 0; i<scores.size(); i++) {
			msg += scores.get(i).toString() + "\n";
		}
		return msg;
	}

	private void importScores() {
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/scores.rym"));
			Object temp = ois.readObject();
			while(temp != null) {
				User user = (User) temp;
				scores.add(user);
				temp = ois.readObject();
			}
			ois.close();
		} catch(IOException | ClassNotFoundException e) {

		}
	}

	public void exportScores() throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/scores.rym"));
		for(int i=0; i<scores.size(); i++) {
			oos.writeObject(scores.get(i));
		}
		oos.close();
	}
}
