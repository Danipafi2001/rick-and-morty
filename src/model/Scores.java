package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Scores {

	private ArrayList<User> scores;

	public Scores() throws FileNotFoundException, ClassNotFoundException, IOException {
		scores = new ArrayList<>();
		importScores();
	}

	public void addScore(String n, int s) throws FileNotFoundException, IOException {
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
		orderByScore();
		exportScores();
	}

	private void orderByScore() {
		boolean inversion = true;
		for (int i = 0; i < scores.size() && inversion; i++) {
 			inversion = false;
 	    	for (int j = 1; j < scores.size() - i; j++) {
 	    		if (scores.get(j).compareTo(scores.get(j-1)) > 0) {
 	    			User temp = scores.get(j);
 	    			scores.set(j, scores.get(j-1));
 	    			scores.set(j-1, temp);
 	    			inversion = true;
 	    		}
 	    	}
 	    }
	}

	public String print() throws FileNotFoundException {
		String msg = "\nTOP 5 SCORES\n\n";
		msg += String.format("%-20s", "USERNAME") + " " + String.format("%-20s", "SCORE") + "\n\n";
		for(int i = 0; i<scores.size() && i<5; i++) {
			msg += scores.get(i).toString() + "\n";
		}
		saveScores();
		return msg;
	}

	private void saveScores() throws FileNotFoundException {
		PrintWriter writer = new PrintWriter("data/scores.txt");
		for(int i = 0; i<scores.size(); i++) {
			writer.println(scores.get(i).save());
		}
		writer.close();
	}

	private void importScores() {
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/scores.ram"));
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
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/scores.ram"));
		for(int i=0; i<scores.size(); i++) {
			oos.writeObject(scores.get(i));
		}
		oos.close();
	}
	
	public void emptyScores() {
		scores = new ArrayList<>();
	}
}
