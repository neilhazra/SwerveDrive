package org.usfirst.frc.team2976.robot.subsystems;
import org.usfirst.frc.team2976.robot.commands.DriveWithJoystick;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.PIDMain;
import util.PIDSource;

/**
 * The DriveTrain subsystem controls the robot's chassis and reads in
 * information about it's speed and position.
 */
public class DriveTrain extends Subsystem {
	// Subsystem devices
	private SpeedController frontLeftCIM, frontRightCIM;
	private SpeedController backLeftCIM, backRightCIM;
	private SpeedController frontTurn,  backTurn;	
	private Encoder frontEncoder, backEncoder;
	public PIDSource frontPIDSource, backPIDSource;
	public PIDMain frontPID, backPID;
	
	public DriveTrain() {
		frontLeftCIM = new Jaguar(3);
		frontRightCIM = new Jaguar(1);
		backLeftCIM = new Jaguar(4);
		backRightCIM = new Jaguar(0);
		frontTurn = new CANTalon(1);
		backTurn = new CANTalon(2);
		
		frontEncoder = new Encoder(1, 0);
		backEncoder = new Encoder(2	,3);
		
		frontPIDSource = new PIDSource()	{
			public double getInput() {
				SmartDashboard.putNumber("InputFront", frontEncoder.get());
				return frontEncoder.get() ;	
			}		
		};
		backPIDSource = new PIDSource(){	
			public double getInput() {
				SmartDashboard.putNumber("InputBack", backEncoder.get());
				return backEncoder.get();
			}		
		};
		frontPID = new PIDMain(frontPIDSource, 0, 100, 0.006, 0.001, 0.0);
		backPID = new PIDMain(backPIDSource, 0, 100, 0.006, 0.001, 0.0);
	}
	public void initDefaultCommand() {
		setDefaultCommand(new DriveWithJoystick());
	}
	/**
	 * @param frontLeft
	 * @param frontRight
	 * @param backLeft
	 * @param backRight 	
	 */
	public void drive(double frontLeft, double frontRight, double backLeft,double backRight,double x, double y) {
		frontLeftCIM.set(-frontLeft);
		frontRightCIM.set(frontRight);
		backLeftCIM.set(-backLeft);
		backRightCIM.set(-backRight);
		frontTurn.set(x);
		backTurn.set(y);
		}
	public void turnDrive(double x, double y)	{
		frontPID.setSetpoint(x);
		backPID.setSetpoint(y);
	}
	public double getFrontOutput()	{
		return frontPID.getOutput();
	}
	public double getBackOutput()	{
		return backPID.getOutput();
	}
	public void stop() {
		drive(0,0,0,0,0,0);
	}
}