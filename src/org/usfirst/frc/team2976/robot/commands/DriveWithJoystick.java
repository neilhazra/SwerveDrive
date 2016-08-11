package org.usfirst.frc.team2976.robot.commands;

import org.usfirst.frc.team2976.robot.OI;
import org.usfirst.frc.team2976.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveWithJoystick extends Command {
	double threshold = 0.05;
	double currentHeading = 0;

	public DriveWithJoystick() {
		requires(Robot.drivetrain);
	}

	protected void initialize() {
	}

	protected void execute() {
		double y = Robot.oi.getYAxis();
		double x = Robot.oi.getXAxis();
//		double rx = -Robot.oi.joystick.getRawAxis(OI.Axis.LX.getAxisNumber());
//		double rx = 0;
		double diagonalright = Robot.oi.getDiagonalRight();
		double diagonalleft = Robot.oi.getDiagonalLeft();
		
		x = Math.abs(x) < threshold ? 0 : x;
		y = Math.abs(y) < threshold ? 0 : y;
//		rx = Math.abs(rx) < threshold ? 0 : rx;

		if (diagonalright <= 0.1 && diagonalleft <= 0.1) {
			double[] temp = map(x);
			Robot.drivetrain.swerveDrive(y, temp[0], temp[1]);
			currentHeading = Robot.drivetrain.getGyro();
			SmartDashboard.putNumber("Current Heading", currentHeading);
			Robot.drivetrain.gyroPID.resetPID();
		} else {
			SmartDashboard.putNumber("Current Heading Error", Robot.drivetrain.gyroPID.getError());
			//Robot.drivetrain.swerveDrive(y, x * 120 * diagonalright, x * 100 * diagonalright);
			Robot.drivetrain.diagonalDrive(y, currentHeading,x * 120 * diagonalright, x * 90 * diagonalright);
		}
	}

	/**
	 * Uses a quadratic / cubic function to map x to an array that can be used
	 * to change the rotation of the front and back motors
	 * 
	 * TODO: Create loop-up table to speed things up a bit TODO: Less sensitive
	 * turn at lower speeds
	 * 
	 * @param x
	 * @return turn degree array with two values, the first for the front motors
	 *         rotation, second for the back motors rotation
	 */
	public double[] map(double x) {
		double[] turnDegree = new double[2];
		turnDegree[0] = Math.signum(x) * 120 * Math.pow(x, 2);
		if (x > 0.5) {
			turnDegree[1] = -100 * 8 * Math.pow(x - 0.5, 3);
		} else if (x < -0.5) {
			turnDegree[1] = -100 * 8 * Math.pow(x + 0.5, 3);
		}
		// constrain to make sure motor does not turn the wheels to far
		turnDegree[0] = constrain(turnDegree[0], -120, 120);
		turnDegree[1] = constrain(turnDegree[1], -90, 90);
		return turnDegree;
	}

	public double constrain(double x, double lowerLimit, double upperLimit) {
		x = x > upperLimit ? upperLimit : x;
		x = x < lowerLimit ? lowerLimit : x;
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