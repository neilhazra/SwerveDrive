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
	PIDMain frontSteering;
	PIDMain backSteering;
	SwerveDrive swerveDrive;
	double frontWeightedAverage;
	double backWeightedAverage;

	Vector[] temp;
	// TODO
	double kp = 0.0001;
	double ki = 0;
	double kd = 0;

	public DriveWithJoystick() {
		requires(Robot.drivetrain);

		frontSteering = new PIDMain(Robot.drivetrain.frontPIDSource, 0, 100, kp, ki, kd);
		backSteering = new PIDMain(Robot.drivetrain.backPIDSource, 0, 100, kp, ki, kd);
		swerveDrive = new SwerveDrive();
	
	}

	protected void initialize() {
		frontSteering.setSetpoint(0);
		backSteering.setSetpoint(0);
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
		double[] temp = map(x);
		Robot.drivetrain.turnDrive(temp[0], temp[1]);
		Robot.drivetrain.drive(y, y, y, y, Robot.drivetrain.getFrontOutput(), Robot.drivetrain.getBackOutput());
	}
	public double[] map(double x) {
		double[] power = new double[2];
		if (x > 0) {
			power[0] = Math.pow(x, 2);
			power[1] = -8 * Math.pow((x - 0.5), 3);
		}
		if (x < 0) {
			power[0] = -Math.pow(x, 2);
			power[1] = 8 * Math.pow((x - 0.5), 3);
		}
		power[0] *= 90;
		power[1] *= 90;
		power[0] = constrain(power[0],-90,90);
		power[1] = constrain(power[1],-90,90);
		return power;
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