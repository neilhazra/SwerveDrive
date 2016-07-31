package org.usfirst.frc.team2976.robot.commands;

import org.usfirst.frc.team2976.robot.OI;
import org.usfirst.frc.team2976.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.PIDMain;
import util.SwerveDrive;
import util.SwerveDrive.Vector;

public class DriveWithJoystick extends Command {
	boolean mode = true;
	long prevTime = 0;
	final long BUTTON_PAUSE = 500;

	public DriveWithJoystick() {
		requires(Robot.drivetrain);
	}

	protected void initialize() {
	}

	protected void execute() {

		double y = -0.5 * Robot.oi.joystick.getRawAxis(OI.Axis.LY.getAxisNumber());
		double x = -Robot.oi.joystick.getRawAxis(OI.Axis.RX.getAxisNumber());
		double rx = -Robot.oi.joystick.getRawAxis(OI.Axis.LX.getAxisNumber());

<<<<<<< HEAD
		if (Math.abs(x) < 0.05)
			x = 0;
		if (Math.abs(y) < 0.05)
			y = 0;
		if (Math.abs(rx) < 0.05)
			rx = 0;
=======
		// if the joystick values are less than the threshold, set the joystick
		// value to 0
		double threshold = 0.05;
		if (Math.abs(x) < threshold) {
			x = 0;
		}
		if (Math.abs(y) < threshold) {
			y = 0;
		}
		if (Math.abs(rx) < threshold) {
			rx = 0;
		}
>>>>>>> origin/master

		double diagonalright = Robot.oi.joystick.getRawAxis(OI.Axis.RTrigger.getAxisNumber());
		double diagonalleft = Robot.oi.joystick.getRawAxis(OI.Axis.LTrigger.getAxisNumber());

<<<<<<< HEAD
		if (!(diagonalright > 0.1 || diagonalleft > 0.1)) {
=======
		if (diagonalright <= 0.1 && diagonalleft <= 0.1) {
>>>>>>> origin/master
			double[] temp = map(x);
			Robot.drivetrain.turnDrive(temp[0], temp[1]);
			// Robot.drivetrain.turnDrive(93*x, -90*x);
		} else {
<<<<<<< HEAD
			if (diagonalright > diagonalleft) {
				if (y > 0) {
					Robot.drivetrain.turnDrive(120 * diagonalright, 100 * diagonalright);
				} else {
					Robot.drivetrain.turnDrive(-120 * diagonalright, -100 * diagonalright);
				}
			} else {
				if (y > 0) {
					Robot.drivetrain.turnDrive(-120 * diagonalleft, -100 * diagonalleft);
				} else {
					Robot.drivetrain.turnDrive(120 * diagonalright, 100 * diagonalright);
				}
			}
		}
=======
			if (diagonalright > diagonalleft) { // if the joystick controls are
												// to the right
				// turn right
				Robot.drivetrain.turnDrive(120 * diagonalright, 100 * diagonalright);
			} else { // if the joystick controls are to the left
				// turn left
				Robot.drivetrain.turnDrive(-120 * diagonalleft, -100 * diagonalleft);
			}
		}
		// drive the robot after making sure that the rotation of the front
		// motors and the rotation for the back motors are safe
>>>>>>> origin/master
		Robot.drivetrain.drive(y, y, y, y, constrain(Robot.drivetrain.getFrontOutput(), -0.5, 0.5),
				constrain(Robot.drivetrain.getBackOutput(), -0.33, 0.33));
	}

<<<<<<< HEAD
=======
	/**
	 * Uses a quadratic / cubic function to map x to an array that can be used
	 * to change the rotation of the front and back motors
	 * 
	 * @param x
	 * @return turn degree array with two values, the first for the front motors
	 *         rotation, second for the back motors rotation
	 */
>>>>>>> origin/master
	public double[] map(double x) {
		double[] turnDegree = new double[2];
		if (x > 0) {
			turnDegree[0] = 120 * Math.pow(x, 2);
		}
		if (x < 0) {
			turnDegree[0] = -120 * Math.pow(x, 2);
		}

		if (x > 0.5) {
			turnDegree[1] = -100 * 8 * Math.pow(x - 0.5, 3);
		}
		if (x < -0.5) {
			turnDegree[1] = -100 * 8 * Math.pow(x + 0.5, 3);
		}

<<<<<<< HEAD
=======
		// make sure that turnDegree is within safe bounds
>>>>>>> origin/master
		turnDegree[0] = constrain(turnDegree[0], -120, 120);
		turnDegree[1] = constrain(turnDegree[1], -100, 100);
		return turnDegree;
	}

<<<<<<< HEAD
=======
	/**
	 * @param x
	 *            number to constrain
	 * @param lowerLimit
	 * @param upperLimit
	 *            number larger than lowerLimit
	 * @return x that is in between lowerLimit and upperLimit
	 */
>>>>>>> origin/master
	public double constrain(double x, double lowerLimit, double upperLimit) {
		if (x > upperLimit) {
			x = upperLimit;
		}
		if (x < lowerLimit) {
			x = lowerLimit;
		}
		return x;
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.drivetrain.stop();
	}

	protected void interrupted() {
		end();
	}
}