import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SlidingPuzzle extends JFrame implements ActionListener
{
	boolean gameover;
	int row = 3,
		col = 3,
		cellsize = 100,
		state = (row * col) - 1,
		seed = 0,
		hr,mm,ss;

	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	LinkedList<ImageIcon> shuffler = new LinkedList<ImageIcon>();  
	Random rand = new Random();
	JPanel panel = new JPanel();

	ImageIcon img[] = new ImageIcon[row * col];
	JButton cell[] = new JButton[row * col],
			shflBtn = new JButton("New Game");;
	JLabel time = new JLabel("Time: 00h:00m:00s"),
			credits = new JLabel("</> with <3 by Nikko Amante");
	String imageList[] = new String[row * col];

	Timer timer = new Timer(1000 , new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			ss++;
			if (ss >= 60) {
				mm++; ss = 0;
			}
			if (mm >= 60) {
				hr++; mm = 0; ss = 0;
			}
			time.setText("Time: " + ((hr < 9) ? "0" : "") + hr + "h:" + 
									((mm < 9) ? "0" : "") + mm + "m:" + 
									((mm < 9) ? "0" : "") + ss + "s");
		}
	});

	SlidingPuzzle() {
		for(int i=0; i < (row * col); i++) {
			imageList[i] = "/img/" + ( i + 1) + ".jpg";
			if (getClass().getResource(imageList[i]) != null) {
				img[i] = new ImageIcon(getClass().getResource(imageList[i]));
				Image resizer = img[i].getImage();
				img[i] = new ImageIcon(resizer.getScaledInstance(cellsize, cellsize, java.awt.Image.SCALE_SMOOTH));
			}
			shuffler.add(img[i]);
			cell[i] = new JButton(img[i]);
			cell[i].setBackground(new Color(200,200,200));
			cell[i].setBorderPainted(true);
			cell[i].addActionListener(this);
			panel.add(cell[i]);
			System.out.println(imageList[i]);
		}
		cell[(row * col) - 1].setEnabled(false);
		shuffler.remove(img[(row * col) - 1]);
		
		panel.setBounds(20, 40, cellsize * col, cellsize * row);
		panel.setLayout(new GridLayout(row, col, 0, 0));
		panel.setBorder(new LineBorder(new Color(150,150,150), 2));
		add(panel);

		shflBtn.setBounds(20, 10, (cellsize * col) / 2, 20);
		shflBtn.addActionListener(this);
		shflBtn.setFocusPainted(false);
		shflBtn.setContentAreaFilled(false);
		add(shflBtn);

		time.setBounds(((cellsize * col) / 2) + ((cellsize * col) / 2) / 2, 10, cellsize * col, 20);
		add(time);

		credits.setBounds(((cellsize * col) / 2) - 65, (cellsize * row) + 45, cellsize * col, 20);
		add(credits);

		// shflBtn.doClick();
		gameover = false;
		timer.setRepeats(true);

		setLayout(null);
		setTitle(this.getClass().getCanonicalName());
		setSize((cellsize * col) + 55, (cellsize * row) + 115);
		setVisible(true);
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==shflBtn) {
			timer.stop();
			seed++;
			Collections.shuffle(shuffler, new Random(seed));
			for(int a = 0; a < (row * col)-1; a++) {
				cell[a].setEnabled(true);
				cell[a].setIcon(shuffler.get(a));
				cell[a].setBorderPainted(true);
			}
			state = (row * col) - 1;
			cell[state].setIcon(img[(row * col) - 1]);
			cell[state].setEnabled(false);
			
			gameover = false;
			hr = 0;
			mm = 0;
			ss = 0;
			time.setText("Time: 00h:00m:00s");
		}
		
		for(int a = 0; a < (row * col); a++) {
			if (e.getSource()==cell[a])
			{
				if (gameover) return;
				if (!timer.isRunning()) timer.start();
				for(int b = 1; b <= row; b++) {
					if ((state == ((row * b) - 1) && (a == (row * b))) ||
					 (state == ((row * b)) && (a == ((row * b) - 1)))) return;
				}
				if ((state == a + 1 || state == a - 1 || state == a + row || state == a - row)) {
					cell[state].setIcon(cell[a].getIcon());
					cell[state].setEnabled(true);
					state = a;
					cell[a].setIcon(img[(row * col) - 1]);
					cell[a].setEnabled(false);
				}
			}
		}

		int counter = 0;
		for(int i=0; i < (row * col); i++) {
			if (cell[i].getIcon() == img[i]) 
				counter++;
			else 
				break;
		}
		if (counter == row * col) {
			timer.stop();
			for(int a = 0; a < (row * col)-1; a++)
				cell[a].setBorderPainted(false);
			gameover = true;
			JOptionPane.showMessageDialog(this, "Congratulations you won!!");
		}
	}
	
	public static void main(String[] args) {
		new SlidingPuzzle();
	}
}
