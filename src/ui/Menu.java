package ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
			System.out.print("Enter Rick's username: ");
			String rick = br.readLine();
			System.out.print("Enter Morty's username: ");
			String morty = br.readLine();
			turn(true, rick, morty);
		} catch(NumberFormatException | CustomException e) {
			System.out.println("\nException: "+e.getMessage()+"\n");
			show();
		}
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

	private void turn(boolean player, String rick, String morty) throws IOException {
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
			if(stop) {
				boolean winner = dimension.getrSeeds()>dimension.getmSeeds() ? true : false;
				int seeds = dimension.getrSeeds()>dimension.getmSeeds() ? dimension.getrSeeds() : dimension.getmSeeds();
				String user = dimension.getrSeeds()>dimension.getmSeeds() ? rick : morty;
				endGame(winner, seeds, user);
				return;
			}
			turn(!player, rick, morty);
			break;
		case 2:
			System.out.print("\n"+dimension.print(true));
			turn(player, rick, morty);
			break;
		case 3: 
			System.out.print("\n"+dimension.print(false));
			turn(player, rick, morty);
			break;
		case 4:
			System.out.println("\nRick: "+dimension.getrSeeds()+" seeds");
			System.out.println("Morty: "+dimension.getmSeeds()+" seeds");
			turn(player, rick, morty);
			break;
		default:
			System.out.println("\nException: The number entered is invalid\n");
			turn(player, rick, morty);
		}
	}

	private void endGame(boolean winner, int seeds, String user) throws FileNotFoundException, IOException {
		System.out.println("\nRick: "+dimension.getrSeeds()+" seeds");
		System.out.println("Morty: "+dimension.getmSeeds()+" seeds");
		if(winner)
			System.out.println("\nRick wins collecting "+seeds+" seeds");
		else
			System.out.println("\nMorty wins collecting "+seeds+" seeds");
		int score = seeds*120;
		scores.addScore(user.trim(), score);
		System.out.println(scores.print());
		scores.exportScores();
	}
}
