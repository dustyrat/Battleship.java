/* Written by: Dustin Ratcliffe
 * Created: 12.16.14
 * Last Edited: 2.28.15
 * Description: a battleship game with AI. 
 * Future Functions: add 2 player mode, improved AI, player place own ships
*/

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Battleship extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton PriGrid[][], TrackGrid[][], menuBut[];
	JLabel displayLabel, menuLab[];
	int Comp[] = new int[5], // used to store the comp ship's hits.  0 destroyer, 1 cruiser, 2 sub...
			Play[] = new int[5], // used to store the player ship's hits. 0 destroyer, 1 cruiser, 2 sub...
			hit[] = new int[2], // used to store the last hit by comp. 0 x, 1 y
			hits[][][] = new int[5][5][2]; // not yet used, stores the hit location of the ship. [0][][] destroyer, [1] cruiser, [2] sub... [][][0] x, [][][1] y
	boolean direction[] = new boolean[4]; // 0 up, 1 down, 2 left, 3 right
	Color aqua = new Color(0, 255, 255);

	public Battleship(){
		JPanel primary, tracking, display, menu;
		TrackGrid = new JButton[10][10];
		PriGrid = new JButton[10][10];
		menuBut = new JButton[5];
		primary = new JPanel();
		tracking = new JPanel();
		display = new JPanel();
		menu = new JPanel();
		menuLab = new JLabel[5];
		
		displayLabel = new JLabel("Battleship the Game", JLabel.CENTER);
		displayLabel.setFont(new Font("Serif", Font.PLAIN, 24));

		primary.setLayout(new GridLayout(10,10));
		primary.setPreferredSize(new Dimension(400, 400));
		primary.setMaximumSize(new Dimension(400, 400));
		
		tracking.setLayout(new GridLayout(10,10));
		tracking.setPreferredSize(new Dimension(400, 400));
		tracking.setMaximumSize(new Dimension(400, 400));
		
		display.setPreferredSize(new Dimension(200, 50));

		menu.setLayout(new GridLayout(5,1));
		menu.setPreferredSize(new Dimension(200,400));
		menu.setMaximumSize(new Dimension(200,400));
		
		// creates new JButtons and adds them to menu JPanel
		for (int x = 0; x < menuBut.length; x++){
			menuBut[x] = new JButton();
			menuLab[x] = new JLabel();
			menuLab[x].setFont(new Font("Serif", Font.PLAIN, 24));
			menuBut[x].add(menuLab[x]);
			menuBut[x].putClientProperty("choice", x);
			menuBut[x].putClientProperty("toggle", true);
			menuBut[x].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){					
					Menu(e);
				}
			});
			menu.add(menuBut[x]);
		}
		
		menuLab[0].setText("New Game");
		menuLab[4].setText("Quit");

		// menu buttons 1, 2 and 3 not yet implemented
		menuBut[1].setVisible(false);
		menuBut[2].setVisible(false);
		menuBut[3].setVisible(false);
		menuBut[1].setEnabled(false);
		menuBut[2].setEnabled(false);
		menuBut[3].setEnabled(false);
		
		
		/* creates a new JButton for each element in the 10x10 array
		 * sets the color to aqua, and adds properties ship (bool), 
		 * click (bool), and type (string). then adds the array to the primary JPanel
		 */
		for (int x = 0; x < PriGrid.length; x++){
			for (int y = 0; y < PriGrid[x].length; y++){
				PriGrid[x][y] = new JButton();
				PriGrid[x][y].setBackground(aqua);
				PriGrid[x][y].putClientProperty("ship", false);
				PriGrid[x][y].putClientProperty("click", false);
				PriGrid[x][y].putClientProperty("type", "");
				PriGrid[x][y].setEnabled(false);
				primary.add(PriGrid[x][y]);
			}
		}
		
		/* creates a new JButton for each element in the 10x10 array
		 * sets the color to aqua, and adds properties ship (bool), 
		 * click (bool), and type (string) and adds an ActionEvent. 
		 * then adds the array to the tracking JPanel
		 */
		for (int x = 0; x < TrackGrid.length; x++){
			for (int y = 0; y < TrackGrid[x].length; y++){
				TrackGrid[x][y] = new JButton();
				TrackGrid[x][y].setBackground(aqua);
				TrackGrid[x][y].putClientProperty("ship", false);
				TrackGrid[x][y].putClientProperty("click", false);
				TrackGrid[x][y].putClientProperty("type", "");
				TrackGrid[x][y].setEnabled(false);
				TrackGrid[x][y].addActionListener(new ActionListener(){
					/* when JButton is clicked, disables button, checks if the square for a ship
					 * if there is a ship increments comp array's element corresponding to the ship
					 * and changes the color to red if ship or white if no ship and displays hit or miss respectively
					 *  then calls method compMove
					 */
				public void actionPerformed(ActionEvent e){
			        JButton square = (JButton) e.getSource();
			        if (!(boolean)square.getClientProperty("click")){
			            square.putClientProperty("click", true); // comment out for testing
			            if ((boolean)square.getClientProperty("ship")){
				            	switch ((String)square.getClientProperty("type")){
				            	case "Carrier": Comp[4]++;
				            		break;
				            	case "Battleship": Comp[3]++;
				                	break;
				            	case "Sub": Comp[2]++;
				                	break;
				            	case "Cruiser": Comp[1]++;
					            	break;
				            	case "Destroyer": Comp[0]++;
				            		break;
			            	}
			            	square.setBackground(Color.RED);
			            	displayLabel.setText("HIT!");
			            	}
			            else{
			            	square.setBackground(Color.WHITE);
			            	displayLabel.setText("Miss");
			            	}
			            compMove(e);
			        }
			    }});
				tracking.add(TrackGrid[x][y]);
			}
		}

		display.add(displayLabel);
		add(display, BorderLayout.NORTH);
		add(primary, BorderLayout.EAST);
		add(tracking, BorderLayout.WEST);
		add(menu, BorderLayout.CENTER);
		clear();
	}
	
	public static void main(String[] args) {
		Battleship frame = new Battleship();
		frame.setTitle("Battleship");
		frame.setPreferredSize(new Dimension(1000, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
		}
	/* this method is the menu selection, button 0 is new game and button 4 is exit program
	 * buttons 1, 2, 3 are not implemented yet, plan on having them for player ship placement
	 * and game mode
	 */
	public void Menu(ActionEvent e){
		JButton button = (JButton) e.getSource();
		switch ((int)button.getClientProperty("choice")){
		case 0: 
			if ((boolean)button.getClientProperty("toggle")){
				System.out.print("New Game\n");
				clear();
				playShipPlace(PriGrid, 5, "Carrier");
				playShipPlace(PriGrid, 4, "Battleship");
				playShipPlace(PriGrid, 3, "Sub");
				playShipPlace(PriGrid, 3, "Cruiser");
				playShipPlace(PriGrid, 2, "Destroyer");
				
				compShipPlace(TrackGrid, 5, "Carrier");
				compShipPlace(TrackGrid, 4, "Battleship");
				compShipPlace(TrackGrid, 3, "Sub");
				compShipPlace(TrackGrid, 3, "Cruiser");
				compShipPlace(TrackGrid, 2, "Destroyer");

				// enables TrackGrid
				for (int x = 0; x < TrackGrid.length; x++){
					for (int y = 0; y < TrackGrid[x].length; y++){
						TrackGrid[x][y].setEnabled(true);
					}
				}
			}			
			//button.putClientProperty("toggle", !(boolean)button.getClientProperty("toggle"));
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4: System.exit(0);
			break;
		}
	}
	// chooses the location of the player's ships
	public static void playShipPlace(JButton[][] square, int size, String ship){
		int dir = 0, x = 0, y = 0;
		do{
			dir = (int)(Math.random()*2);
			switch (dir){
			case 0: // horizontally
					x = (int)(Math.random()*10);
					y = (int)(Math.random()*(10-size));
				break;
			case 1: // vertically
					x = (int)(Math.random()*(10-size));
					y = (int)(Math.random()*10);
				break;
			}
		}while (CheckForShip(square, size, dir, x, y));
		placeShip(square, x, y, dir, size, ship, Color.GRAY);
	}
	// chooses the location of the comp's ships
	public static void compShipPlace(JButton[][] square, int size, String ship){
		int dir, x = 0, y = 0;
		do{
			dir = (int)(Math.random()*2);
			switch (dir){
			case 0: // horizontally
					x = (int)(Math.random()*10);
					y = (int)(Math.random()*(10-size));
				break;
			case 1: // vertically
					x = (int)(Math.random()*(10-size));
					y = (int)(Math.random()*10);
				break;
			}
		}while (CheckForShip(square, size, dir, x, y));
		placeShip(square, x, y, dir, size, ship);
	}
	// places the ship on the given grid
	public static void placeShip(JButton[][] square, int x, int y, int dir, int size, String ship){
		switch (dir){
		case 0: // horizontally
			for (int count = 0; count < size; count++, y++){
				square[x][y].putClientProperty("ship", true);
				square[x][y].putClientProperty("type", ship);
			}
			break;
		case 1: // vertically
			for (int count = 0; count < size; count++, x++){
				square[x][y].putClientProperty("ship", true);
				square[x][y].putClientProperty("type", ship);
			}
			break;
		}
	}
	// places the ship on the given grid and sets the color of the JButton to the given color
	public static void placeShip(JButton[][] square, int x, int y, int dir, int size, String ship, Color color){
		switch (dir){
		case 0: // horizontally
			for (int count = 0; count < size; count++, y++){
				square[x][y].putClientProperty("ship", true);
				square[x][y].putClientProperty("type", ship);
				square[x][y].setBackground(color);
			}
			break;
		case 1: // vertically
			for (int count = 0; count < size; count++, x++){
				square[x][y].putClientProperty("ship", true);
				square[x][y].putClientProperty("type", ship);
				square[x][y].setBackground(color);
			}
			break;
		}
	}
	/* checks to see if a ship can be placed without overlapping
	 * returns true if a ship exists in any square
	 */
	public static boolean CheckForShip(JButton[][] square, int size, int dir, int x, int y){
		switch (dir){
		case 0: // horizontally
			for (int count = 0; count < size; count++, y++){
				if ((boolean) square[x][y].getClientProperty("ship")){
					return true;
				}
			}
			break;
		case 1: // vertically
			for (int count = 0; count < size; count++, x++){
				if ((boolean) square[x][y].getClientProperty("ship")){
					return true;
				}
			}
			break;
		}
		return false;
	}
	/* chooses the computer's move. randomly chooses a square if 
	 * first move or if last few moves wasn't a hit. if it hit, randomly choose
	 * a direction till there are no more hits
	 * future implementation: determine when hitting the same ship or multiple ships in a row
	 */
	public void compMove(ActionEvent e){
		// checks for win condition
		if (checkWin(e)){
			return;
		}
		int dir = -1, x = 0, y = 0, size;
		int a = 0; // use for testing
		do{ System.out.print("loop 1\n");
			/* up, down, left or right is true randomly choose a direction
			 * test the next square for a ship and if previous move
			 * if unable to find move sets direction to false
			 */
			if (direction[0] || direction[1] || direction[2] || direction[3]){
				do{ System.out.print("loop 2\n");
					x = hit[0];
					y = hit[1];
					dir = (int)(Math.random()*4);
					switch (dir){
					case 0: 
						if (direction[0]){
							x--; //Up
							System.out.print("up\n");
							if (x >= 0){
								while (x > 0 && (boolean)PriGrid[x][y].getClientProperty("click") && (boolean)PriGrid[x][y].getClientProperty("ship")){
									x--;
								}
								if ((boolean)PriGrid[x][y].getClientProperty("click")){
									direction[0] = false;
								}
								System.out.print(x + "," + y + "\n");
							}
							else {
								direction[0] = false;
								x = hit[0];
							}
						}
						break;
					case 1: 
						if (direction[1]){
							x++; //Down
							System.out.print("down\n");
							if (x < 10){
								while (x < 9 && (boolean)PriGrid[x][y].getClientProperty("click") && (boolean)PriGrid[x][y].getClientProperty("ship")){
									x++;
								}
								if ((boolean)PriGrid[x][y].getClientProperty("click")){
									direction[1] = false;
								}
								System.out.print(x + "," + y + "\n");
							}
							else {
								direction[1] = false;
								x = hit[0];
							}
						}
						break;
					case 2: 
						if (direction[2]){
							y--; //Left
							System.out.print("left\n");
							if (y >= 0){
								while (y > 0 && (boolean)PriGrid[x][y].getClientProperty("click") && (boolean)PriGrid[x][y].getClientProperty("ship")){
									y--;
								}
								if ((boolean)PriGrid[x][y].getClientProperty("click")){
									direction[2] = false;
								}
								System.out.print(x + "," + y + "\n");
							}
							else {
								direction[2] = false;
								y = hit[1];
							}
						}
						break;
					case 3: 
						if (direction[3]){
							y++; //Right
							System.out.print("right\n");
							if (y < 10){
								while (y < 9 && (boolean)PriGrid[x][y].getClientProperty("click") && (boolean)PriGrid[x][y].getClientProperty("ship")){
									y++;
								}
								if ((boolean)PriGrid[x][y].getClientProperty("click")){
									direction[3] = false;
								}
								System.out.print(x + "," + y + "\n");
							}
							else {
								direction[3] = false;
								y = hit[1];
							}
						}
						break;
					}
					// if all directions became false break loop
					if (!direction[0] && !direction[1] && !direction[2] && !direction[3]){
						break;
					}
				}while((x < 0 || x >= 10) || (y < 0 || y >= 10));
			}
			// if all directions are false, randomly choose a move
			if (!direction[0] && !direction[1] && !direction[2] && !direction[3]){
				dir = -1;

				if (Play[0] == 2 && Play[1] == 3 && Play[2] == 3 && Play[4] == 4){ // if Carrier is the only ship left
					size = 5;
				}
				else if (Play[0] == 2 && Play[1] == 3 && Play[2] == 3){ // if destroyer, sub, and cruiser are sunk
					size = 4;
				}
				else if (Play[0] == 2){ // if only destroyer is sunk
					size = 3;
				}else{
					size = 2;
				}
				
				do{
					x = (int)(Math.random()*10);
					y = (int)(Math.random()*10);
					direction[0] = true;
					direction[1] = true;
					direction[2] = true;
					direction[3] = true;
					// looks in all directions for space available for a ship of size
					for(int i = 0; i < size; i++){System.out.print("count: " + i + "\n");
						if (((x - i) >= 0 && ((boolean)PriGrid[x - i][y].getClientProperty("click") && !(boolean)PriGrid[x - i][y].getClientProperty("ship"))) || (x - i) < 0){
							direction[0] = false;
						}
						if (((x + i) < 10 && ((boolean)PriGrid[x + i][y].getClientProperty("click") && !(boolean)PriGrid[x + i][y].getClientProperty("ship"))) || (x + i) >= 10){
							direction[1] = false;
						}
						if (((y - i) >= 0 && ((boolean)PriGrid[x][y - i].getClientProperty("click") && !(boolean)PriGrid[x][y - i].getClientProperty("ship"))) || (y - i) < 0){
							direction[2] = false;
						}
						if (((y + i) < 10 && ((boolean)PriGrid[x][y + i].getClientProperty("click") && !(boolean)PriGrid[x][y + i].getClientProperty("ship"))) || (y + i) >= 10){
							direction[3] = false;
						}
					}
				}while(!direction[0] && !direction[1] && !direction[2] && !direction[3]);
			}
			// testing prevents inf loop
			if ( a > 100){
				a = 0;
				System.out.print("dir:" + dir + "\n");
				System.out.print("Up: " + direction[0] + "\n");
				System.out.print("Down: " + direction[1] + "\n");
				System.out.print("Left: " + direction[2] + "\n");
				System.out.print("Right: " + direction[3] + "\n");
				System.out.print(x + "," + y + "\n");
				pauseProg();
			}
			a++;
		}while((boolean)PriGrid[x][y].getClientProperty("click"));
		PriGrid[x][y].putClientProperty("click", true);
		// if ship is located in square
		if ((boolean)PriGrid[x][y].getClientProperty("ship")){
			PriGrid[x][y].setBackground(Color.RED);
			// store hit
			hit[0] = x;
			hit[1] = y;
			// determine direction of ship
			switch (dir){
			case 0: direction[0] = true;//Up
				direction[2] = false;
				direction[3] = false;
				break;
			case 1: direction[1] = true;//Down
				direction[2] = false;
				direction[3] = false;
				break;
			case 2: direction[2] = true;//Left
				direction[0] = false;
				direction[1] = false;
				break;
			case 3: direction[3] = true;//Right
				direction[0] = false;
				direction[1] = false;
				break;
			default: 
				direction[0] = true;
				direction[1] = true;
				direction[2] = true;
				direction[3] = true;
			}
			// increment score and store location of ship hit
			switch ((String)PriGrid[x][y].getClientProperty("type")){
        	case "Carrier": Play[4]++;
        		for (int i = 0; i < 5; i++){
        			if (hits[4][i][0] == -1){
        				hits[4][i][0] = x;
        				hits[4][i][1] = y;
        				break;
        			}
        		}	
        		break;
        	case "Battleship": Play[3]++;
	        	for (int i = 0; i < 4; i++){
	    			if (hits[3][i][0] == -1){
	    				hits[3][i][0] = x;
	    				hits[3][i][1] = y;
	    				break;
	    			}
	    		}
            	break;
        	case "Sub": Play[2]++;
	        	for (int i = 0; i < 3; i++){
	    			if (hits[2][i][0] == -1){
	    				hits[2][i][0] = x;
	    				hits[2][i][1] = y;
	    				break;
	    			}
	    		}
            	break;
        	case "Cruiser": Play[1]++;
	        	for (int i = 0; i < 3; i++){
	    			if (hits[1][i][0] == -1){
	    				hits[1][i][0] = x;
	    				hits[1][i][1] = y;
	    				break;
	    			}
	    		}
        		break;
        	case "Destroyer": Play[0]++;
	        	for (int i = 0; i < 2; i++){
	    			if (hits[0][i][0] == -1){
	    				hits[0][i][0] = x;
	    				hits[0][i][1] = y;
	    				break;
	    			}
	    		}
        		break;
        	}
		}
		// if miss set direction to false
		else{
			PriGrid[x][y].setBackground(Color.WHITE);
			switch (dir){
			case 0: direction[0] = false;//Up
				break;
			case 1: direction[1] = false;//Down
				break;
			case 2: direction[2] = false;//Left
				break;
			case 3: direction[3] = false;//Right
				break;
			default:
				direction[0] = false;
				direction[1] = false;
				direction[2] = false;
				direction[3] = false;
			}
		}
		// output for testing
		System.out.print("dir:" + dir + "\n");
		System.out.print("Up: " + direction[0] + "\n");
		System.out.print("Down: " + direction[1] + "\n");
		System.out.print("Left: " + direction[2] + "\n");
		System.out.print("Right: " + direction[3] + "\n");
		System.out.print(x + "," + y + "\n");
		// check for win condition
		checkWin(e);
	}
	// determines and displays winner
	public boolean checkWin(ActionEvent e){
		if (Comp[4] == 5 && Comp[3] == 4 && Comp[2] == 3 && Comp[1] == 3 && Comp[0] == 2){
        	displayLabel.setText("You won!");
        	for (int x = 0; x < TrackGrid.length; x++){
    			for (int y = 0; y < TrackGrid[x].length; y++){
    					TrackGrid[x][y].setEnabled(false);
    			}
    		}
        	return true;
        }
		if (Play[4] == 5 && Play[3] == 4 && Play[2] == 3 && Play[1] == 3 && Play[0] == 2){
        	displayLabel.setText("You lost");
        	
        	for (int x = 0; x < TrackGrid.length; x++){
    			for (int y = 0; y < TrackGrid[x].length; y++){
    					TrackGrid[x][y].setEnabled(false);
    			}
    		}
        	return true;
        }
		return false;
	}
	// resets the board and all variables
	public void clear(){
		for (int i = 0; i < Comp.length; i++){
			Comp[i] = 0;
			Play[i] = 0;
		}
		
		for (int i = 0; i < direction.length; i++){
			direction[i] = false;
		}
		
		for (int x = 0; x < hits.length; x++){
			for (int y = 0; y < hits[x].length; y++){
				for (int z = 0; z < hits[x][y].length; z++){
					hits[x][y][z] = -1;
				}
				
			}
		}

		for (int x = 0; x < PriGrid.length; x++){
			for (int y = 0; y < PriGrid[x].length; y++){
				PriGrid[x][y].setBackground(aqua);
				PriGrid[x][y].putClientProperty("ship", false);
				PriGrid[x][y].putClientProperty("click", false);
				PriGrid[x][y].putClientProperty("type", "");
				
				TrackGrid[x][y].setBackground(aqua);
				TrackGrid[x][y].putClientProperty("ship", false);
				TrackGrid[x][y].putClientProperty("click", false);
				TrackGrid[x][y].putClientProperty("type", "");
			}
		}
	}
	
	// This method pauses the program
	// used for testing
	@SuppressWarnings("resource")
	public static void pauseProg(){
		System.out.println("Press enter to continue...");
		Scanner keyboard = new Scanner(System.in);
		keyboard.nextLine();
	}
}
