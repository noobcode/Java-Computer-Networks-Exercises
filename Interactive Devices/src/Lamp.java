import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.media.CannotRealizeException;
import javax.media.NoPlayerException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Lamp{

	private static final int EXIT_ON_CLOSE = 0;

	public static void main(String[] args) throws IOException, InterruptedException, NoPlayerException, CannotRealizeException {
		
		// directories
		String restDir = "./images/rest/";
		String motivationalDir = "./images/motivational/";
		String restcolorDir = "./images/restcolor/";
		String workcolorDir = "./images/workcolor/";
		String reminderDir = "./images/reminder/";
		// image file extension
		String ext = ".png";
		int N = 1;
		
		Vector<BufferedImage> im_rest = new Vector<BufferedImage>(N);
		Vector<BufferedImage> im_motivational = new Vector<BufferedImage>(N);
		Vector<BufferedImage> im_restcolor = new Vector<BufferedImage>(N);
		Vector<BufferedImage> im_workcolor = new Vector<BufferedImage>(N);
	    Vector<BufferedImage> im_reminder = new Vector<BufferedImage>(N);
		  
		  int mode = 0;
		  
		  // 1. load images
		  for( int i = 0; i < N; i++){
		    String x = String.valueOf(i);
		    String n_motivational = motivationalDir + "motivational_" + x + ext;
		    String n_reminder = reminderDir + "reminder_" + x + ext;
		    String n_rest = restDir + "rest_" + x + ext;
		    String n_restcolor = restcolorDir + "restcolor_" + x + ext;
		    String n_workcolor = workcolorDir + "workcolor_" + x + ext;

		    im_rest.add(ImageIO.read(new File(n_rest)));
		    im_motivational.add(ImageIO.read(new File(n_motivational)));
		    im_restcolor.add(ImageIO.read(new File(n_restcolor)));
		    im_workcolor.add(ImageIO.read(new File(n_workcolor)));
		    im_reminder.add(ImageIO.read(new File(n_reminder)));
		  }
		  
		  if(mode == 0){
		    // presentation
		    tomatoTechnique(im_rest, im_motivational, im_restcolor, im_workcolor, im_reminder);
		  } else {
		    // demo
		    showAll(im_rest, im_motivational, im_restcolor, im_workcolor, im_reminder);
		  }
		  System.exit(0);
	}
	
	// presentation: 1 minute work, 1 minute rest, 1 minute work
	static void tomatoTechnique(Vector<BufferedImage> im_rest, Vector<BufferedImage> im_motivational, Vector<BufferedImage> im_restcolor, Vector<BufferedImage> im_workcolor, Vector<BufferedImage> im_reminder) throws InterruptedException, NoPlayerException, CannotRealizeException, MalformedURLException, IOException {
		int motivational  = 10 * 1000; // seconds
		int workcolor = 50 * 1000; 
		int rest = 10 * 1000; 
		int restcolor = 50 * 1000;
		int reminder = 10 * 1000;
		int N = im_rest.size();
		Random r = new Random(System.currentTimeMillis());
		// Use a label to display the image
		JFrame frame = new JFrame();
		JLabel lblimage = null;
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setUndecorated(true);
		
		do {
			// motivational
			lblimage = new JLabel(new ImageIcon(im_motivational.get(r.nextInt(N))));
			frame.getContentPane().add(lblimage, BorderLayout.CENTER);
			frame.setVisible(true);
			Thread.sleep(motivational);
		    frame.remove(lblimage);
		    
		    //work
		    lblimage = new JLabel(new ImageIcon(im_workcolor.get(r.nextInt(N))));
		    frame.getContentPane().add(lblimage, BorderLayout.CENTER);
		    frame.repaint();
		    frame.setVisible(true);
		    Thread.sleep(workcolor);
		    frame.remove(lblimage);
		    
		    // play sound
		    SoundPlayer p = new SoundPlayer("./sounds/break.wav");
		    p.setDaemon(true);
		    p.run();
		    
		    p = new SoundPlayer("./sounds/song_fade_" + r.nextInt(11) + ".wav");
		    p.setDaemon(true);
		    p.run();
		    
		    // rest
		    lblimage = new JLabel(new ImageIcon(im_rest.get(r.nextInt(N))));
			frame.getContentPane().add(lblimage, BorderLayout.CENTER);
			frame.repaint();
			frame.setVisible(true);
		    Thread.sleep(rest);
		    frame.remove(lblimage);
		    
		    // rest color
		    lblimage = new JLabel(new ImageIcon(im_restcolor.get(r.nextInt(N))));
			frame.getContentPane().add(lblimage, BorderLayout.CENTER);
			frame.repaint();
			frame.setVisible(true);
			Thread.sleep(restcolor);
			frame.remove(lblimage);
			
			
			// play sound
		    p = new SoundPlayer("./sounds/work.wav");
		    p.setDaemon(true);
		    p.run();
			
			// motivational
			lblimage = new JLabel(new ImageIcon(im_motivational.get(r.nextInt(N))));
			frame.getContentPane().add(lblimage, BorderLayout.CENTER);
			frame.setVisible(true);
			Thread.sleep(motivational);
			frame.remove(lblimage);
					    	
		    //work
		    lblimage = new JLabel(new ImageIcon(im_workcolor.get(r.nextInt(N))));
		    frame.getContentPane().add(lblimage, BorderLayout.CENTER);
		    frame.repaint();
		    frame.setVisible(true);
		    Thread.sleep(workcolor);
		    frame.remove(lblimage);
		
		    // play sound
		    p = new SoundPlayer("./sounds/break.wav");
		    p.setDaemon(true);
		    p.run();
		    
		} while(false);
		
		// reminder
	    lblimage = new JLabel(new ImageIcon(im_reminder.get(r.nextInt(N))));
		frame.getContentPane().add(lblimage, BorderLayout.CENTER);
		frame.repaint();
		frame.setVisible(true);
	    Thread.sleep(reminder);
		
		frame.dispose();
	
	}

	// demo: show all kind of projections
	static void showAll(Vector<BufferedImage> im_rest, Vector<BufferedImage> im_motivational, Vector<BufferedImage> im_restcolor, Vector<BufferedImage> im_workcolor, Vector<BufferedImage> im_reminder) throws InterruptedException {
		int demotime = (int)(60 / (float)(im_rest.size() * 5)) * 1000;
		System.out.println();
		// Use a label to display the image
		JFrame frame = new JFrame();
		JLabel lblimage = null;
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setUndecorated(true);
		for(int i = 0; i < im_rest.size(); i++){
			// motivational
			lblimage = new JLabel(new ImageIcon(im_motivational.get(i)));
			frame.getContentPane().add(lblimage, BorderLayout.CENTER);
			frame.setVisible(true);
			Thread.sleep(demotime);
			frame.remove(lblimage);
			
			//work
			lblimage = new JLabel(new ImageIcon(im_workcolor.get(i)));
			frame.getContentPane().add(lblimage, BorderLayout.CENTER);
			frame.repaint();
			frame.setVisible(true);
			Thread.sleep(demotime);
			frame.remove(lblimage);
					    
			// rest
			lblimage = new JLabel(new ImageIcon(im_rest.get(i)));
			frame.getContentPane().add(lblimage, BorderLayout.CENTER);
			frame.repaint();
			frame.setVisible(true);
			Thread.sleep(demotime);
			frame.remove(lblimage);
			    
			// rest color
			lblimage = new JLabel(new ImageIcon(im_restcolor.get(i)));
			frame.getContentPane().add(lblimage, BorderLayout.CENTER);
			frame.repaint();
			frame.setVisible(true);
			Thread.sleep(demotime);
			frame.remove(lblimage);
						
			// reminder
			lblimage = new JLabel(new ImageIcon(im_reminder.get(i)));
			frame.getContentPane().add(lblimage, BorderLayout.CENTER);
			frame.repaint();
			frame.setVisible(true);
			Thread.sleep(demotime);
			frame.dispose();
			
		}
	}	

}