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
	boolean mode = false;
	long prevTime = 0;
	final long BUTTON_PAUSE = 500;
	public DriveWithJoystick() {
		requires(Robot.drivetrain);
	}
	protected void initialize() {
	}
	protected void execute() {

		double y = -Robot.oi.joystick.getRawAxis(OI.Axis.LY.getAxisNumber());
		double x = -Robot.oi.joystick.getRawAxis(OI.Axis.LX.getAxisNumber());
		double rx = -Robot.oi.joystick.getRawAxis(OI.Axis.RX.getAxisNumber());
		
		if (Math.abs(x) < 0.05)
			x = 0;
		if (Math.abs(y) < 0.05)
			y = 0;	
		if (Math.abs(rx) < 0.05)
			rx = 0;
		
		if (Robot.oi.joystick.getRawButton(OI.Button.X.getBtnNumber()) && (System.currentTimeMillis() - prevTime) > BUTTON_PAUSE) {
			mode= !mode;
			prevTime = System.currentTimeMillis();
		}
		if (mode) {
			//double[] temp = map(x);
			//Robot.drivetrain.turnDrive(temp[0], temp[1]);
			Robot.drivetrain.turnDrive(90*x, -90*x);
		} else {
			Robot.drivetrain.turnDrive(90*x, 90*x);
		}
		Robot.drivetrain.drive(y, y, y, y, constrain(Robot.	drivetrain.getFrontOutput(),-0.33,0.33), constrain(Robot.drivetrain.getBackOutput(),-0.33,0.33));
	}
	public double[] map(double x) {
		double[] turnDegree = new double[2];
		if (x > 0) {
			turnDegree[0] = Math.pow(x, 2);
		}
		if (x < 0) {
			turnDegree[0] = -Math.pow(x, 2);
		}
		
		if (x > 0.5) {
			turnDegree[1] = -90*8*Math.pow(x-0.5, 3);
		}
		if (x < -0.5) {
			turnDegree[1] = -90*8*Math.pow(x+0.5, 3);
		}
		
		turnDegree[0] = constrain(turnDegree[0],-90,90);
		turnDegree[1] = constrain(turnDegree[1],-90,90);
		return turnDegree;
	}

	public double constrain(double x, double lowerLimit, double upperLimit)	{
		if(x > upperLimit)	{
			x = upperLimit; 
		}
		if(x<lowerLimit)	{
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