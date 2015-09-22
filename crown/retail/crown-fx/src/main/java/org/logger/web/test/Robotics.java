/**
 * © Hrillekha - TechLords Inc
 */
package org.logger.web.test;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.util.Random;

import com.sun.glass.events.KeyEvent;

/**
 * @author G. Vaidhyanathan
 *
 */
public class Robotics {
	public static void main(String[] args) throws Exception {
		Random rnd = new Random();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		Robot robot = new Robot();
		robot.setAutoDelay(1000);
		while (true) {
			robot.mouseMove(rnd.nextInt(dim.width), rnd.nextInt(dim.height));
			robot.mouseWheel(rnd.nextInt(5));
			robot.mouseWheel(rnd.nextInt(5));
			
			for(int i = 0; i < rnd.nextInt(5); i++) {
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress(KeyEvent.VK_TAB);
			}
			robot.keyRelease(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_ALT);
			robot.mouseWheel(-rnd.nextInt(5));
			robot.mouseWheel(-rnd.nextInt(5));
		}
	}
}
