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
	//TODO
	double kp = 0;
	double ki = 0;
	double kd = 0;
	
	public DriveWithJoystick() {
		requires(Robot.drivetrain);
		
		frontSteering = new PIDMain(Robot.drivetrain.frontPIDSource,0,100,kp,ki,kd);
		backSteering = new PIDMain(Robot.drivetrain.backPIDSource,0,100,kp,ki,kd);
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
		
		if (Math.abs(x)<0.05)	x = 0;
		if (Math.abs(y)<0.05)	y = 0;
		if (Math.abs(rx)<0.05)	rx = 0;
		
		if(x==0&&y==0&&rx==0)	{
			y=0.0000000001;
		}
		
		temp = swerveDrive.compute(y, x, rx);
		//temp = swerveDrive.compute(0, 0, 1);
		
		frontWeightedAverage = (temp[0].getAngleDegrees()*temp[0].getMagnitude()+temp[2].getAngleDegrees()*temp[2].getMagnitude())/(temp[0].getMagnitude()+temp[2].getMagnitude());
		backWeightedAverage = (temp[1].getAngleDegrees()*temp[1].getMagnitude()+temp[3].getAngleDegrees()*temp[3].getMagnitude())/(temp[1].getMagnitude()+temp[3].getMagnitude()); 

		SmartDashboard.putNumber("Y", y);
		SmartDashboard.putNumber("X", x);
		SmartDashboard.putNumber("RotateX",rx);
		
		SmartDashboard.putNumber("front", frontWeightedAverage);
		SmartDashboard.putNumber("back", backWeightedAverage);
		
		Robot.drivetrain.turnDrive(frontWeightedAverage, backWeightedAverage);
		Robot.drivetrain.drive(temp[0].getMagnitude(), temp[2].getMagnitude(), temp[1].getMagnitude(), temp[3].getMagnitude(),Robot.drivetrain.getFrontOutput(),Robot.drivetrain.getBackOutput());	
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