package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import exception.CustomException;
import model.Dimension;
import model.Scores;

public class Menu {

	public static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	private Dimension dimension;
	private Scores scores;

	public Menu() throws FileNotFoundException, ClassNotFoundException, IOException {
		scores = new Scores();
	}

	public void show() throws NumberFormatException, IOException, InterruptedException {
		int n = 0, m = 0, q = 0;
		try {
			System.out.println("Welcome!");
			System.out.println("1. Upload scores from file: \"data/scores.txt\"");
			System.out.println("   Note: This will delete all scores on system");
			System.out.println("2. Play without upload scores");
			System.out.println("   Note: This will preserve all scores on system");
			int option = Integer.parseInt(br.readLine());
			switch(option) {
			case 1:
				boolean ok = loadScores();
				if(!ok)
					return;

				break;
			case 2:
				break;
			default:
				throw new CustomException("Wubba lubba dub dub");
			}
			System.out.println("\nLet's Play!");
			System.out.print("Enter the number of columns: ");
			n = Integer.parseInt(br.readLine());
			if(n<=0)
				throw new CustomException("The number of columns can't be less than 1");
			System.out.print("Enter the number of rows: ");
			m = Integer.parseInt(br.readLine());
			if(m<=0)
				throw new CustomException("The number of rows can't be less than 1");
			if(n*m < 3)
				throw new CustomException("The number of places can't be less than 3");
			System.out.print("Enter the number of seeds: ");
			q = Integer.parseInt(br.readLine());
			if(q<=0)
				throw new CustomException("The number of seeds can't be less than 1");
			if(q>(n*m-2))
				throw new CustomException("The number of seeds can't be more than number of places minus 2");
			if(q % 2 == 0)
				throw new CustomException("The number of seeds can't be even");
			dimension = new Dimension(n, m, q);
			enterPortals(n, m);
			dimension.putRickMorty();
			String rick = enterName("Rick", null);
			String morty = enterName("Morty", rick);
			turn(true, rick, morty, 0, 0);
		} catch(NumberFormatException | CustomException e) {
			System.out.println("\nException: "+e.getMessage()+"\n");
			show();
		}
	}

	private String enterName(String player, String other) throws IOException {
		System.out.print("Enter "+player+"'s username: ");
		String name = br.readLine();
		try {
			if(name.contains(","))
				throw new CustomException("The name can't contains the character \",\"");
			if(other!=null && name.equals(other))
				throw new CustomException("The names can't be the same");
		} catch(CustomException e) {
			System.out.println("\nException: "+e.getMessage()+"\n");
			name = enterName(player, other);
		}
		return name;
	}

	private void enterPortals(int n, int m) throws NumberFormatException, IOException {
		System.out.print("Enter the number of portals: ");
		int p = Integer.parseInt(br.readLine());
		if(p>=0.5*n*m || p<0) {
			System.out.println("\nException: The number of portals can't be more than half number of places or less than 0\n");
			enterPortals(n, m);
		} else
			dimension.putPortals(p);
	}

	private void turn(boolean player, String rick, String morty, long rTime, long mTime) throws IOException {
		long time = System.currentTimeMillis();
		String name = "";
		if(player)
			name = "Rick's";
		else
			name = "Morty's";
		System.out.println("\nIt's "+name+" turn! What do you want to do?");
		System.out.println("1. Throw dice");
		System.out.println("2. See board");
		System.out.println("3. See portals");
		System.out.println("4. Score");
		int n = Integer.parseInt(br.readLine());
		switch(n) {
		case 1: 
			int dice = dimension.throwDice();
			System.out.println("\nYou got "+dice);
			System.out.println("1. Advance");
			System.out.println("2. Go back");
			n = Integer.parseInt(br.readLine());
			boolean stop = false;
			if(n==1)
				stop = dimension.turn(player, true, dice);
			else
				stop = dimension.turn(player, false, dice);
			time = System.currentTimeMillis()-time;
			if(player)
				rTime += time;
			else
				mTime += time;
			if(stop) {
				boolean winner = dimension.getrSeeds()>dimension.getmSeeds() ? true : false;
				int seeds = dimension.getrSeeds()>dimension.getmSeeds() ? dimension.getrSeeds() : dimension.getmSeeds();
				String user = dimension.getrSeeds()>dimension.getmSeeds() ? rick : morty;
				endGame(winner, seeds, user, rTime, mTime);
				return;
			}
			turn(!player, rick, morty, rTime, mTime);
			break;
		case 2:
			System.out.print("\n"+dimension.print(true));
			turn(player, rick, morty, rTime, mTime);
			break;
		case 3: 
			System.out.print("\n"+dimension.print(false));
			turn(player, rick, morty, rTime, mTime);
			break;
		case 4:
			System.out.println("\nRick: "+dimension.getrSeeds()+" seeds");
			System.out.println("Morty: "+dimension.getmSeeds()+" seeds");
			turn(player, rick, morty, rTime, mTime);
			break;
		default:
			System.out.println("\nException: The number entered is invalid\n");
			turn(player, rick, morty, rTime, mTime);
		}
	}

	private void endGame(boolean winner, int seeds, String user, long rTime, long mTime) throws FileNotFoundException, IOException {
		System.out.println("\nRick: "+dimension.getrSeeds()+" seeds");
		System.out.println("Morty: "+dimension.getmSeeds()+" seeds");
		if(winner)
			System.out.println("\nRick wins collecting "+seeds+" seeds");
		else
			System.out.println("\nMorty wins collecting "+seeds+" seeds");
		int score = seeds*120;
		if(winner)
			score -= (rTime/1000);
		else
			score -= (mTime/1000);
		scores.addScore(user.trim(), score);
		System.out.print(scores.print());
	}

	public boolean loadScores() throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File("data/scores.txt")));
		} catch(FileNotFoundException e) {
			System.out.println("\nException: File not found\n");
			return false;
		}
		String temp = br.readLine();
		if(temp==null || temp.equals("")) {
			System.out.println("\nException: The file is empty\n");
			br.close();
			return false;
		}
		scores.emptyScores();
		try {
			while(temp!=null) {
				String[] attributes = temp.split(",");
				if(attributes.length!=2) {
					br.close();
					throw new Exception();
				}
				scores.addScore(attributes[0], Integer.parseInt(attributes[1]));
				temp = br.readLine();
			}
		} catch(Exception e) {
			System.out.println("\nException: Syntax error on file, remeber that (username,score)\n");
			scores.emptyScores();
			scores.exportScores();
			br.close();
			return false;
		}
		scores.exportScores();
		br.close();
		return true;
	}
}
