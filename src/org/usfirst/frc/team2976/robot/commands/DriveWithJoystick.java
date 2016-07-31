package org.usfirst.frc.team2976.robot.commands;

import org.usfirst.frc.team2976.robot.OI;
import org.usfirst.frc.team2976.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.PIDMain;
import util.SwerveDrive;
import util.SwerveDrive.Vector;

/**
 *
 */
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

		if (Math.abs(x) < 0.05)
			x = 0;
		if (Math.abs(y) < 0.05)
			y = 0;
		if (Math.abs(rx) < 0.05)
			rx = 0;

		double diagonalright = Robot.oi.joystick.getRawAxis(OI.Axis.RTrigger.getAxisNumber());
		double diagonalleft = Robot.oi.joystick.getRawAxis(OI.Axis.LTrigger.getAxisNumber());

		if (!(diagonalright > 0.1 || diagonalleft > 0.1)) {
			double[] temp = map(x);
			Robot.drivetrain.turnDrive(temp[0], temp[1]);
			// Robot.drivetrain.turnDrive(93*x, -90*x);
		} else {
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
		Robot.drivetrain.drive(y, y, y, y, constrain(Robot.drivetrain.getFrontOutput(), -0.5, 0.5),
				constrain(Robot.drivetrain.getBackOutput(), -0.33, 0.33));
	}

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

		turnDegree[0] = constrain(turnDegree[0], -120, 120);
		turnDegree[1] = constrain(turnDegree[1], -100, 100);
		return turnDegree;
	}

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