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
	private SpeedController frontTurn, backTurn;
	private Encoder frontEncoder, backEncoder;
	public PIDSource frontPIDSource, backPIDSource;
	public PIDMain frontPID, backPID;

	// frontTurn and backTurn set the rotation of the front and back motors
	// respectively
	// frontLeftCIM, frontRightCIM, backLeftCIM, and backRightCIM set the power
	// of their respective motor

	public DriveTrain() {
		// tells the robot what the ports are for each motor
		/*
		 * TODO: move the port numbers into RobotMap.java WHERE THEY'RE SUPPOSED
		 * TO BE and get the port numbers from there and use this space for
		 * initializing the objects
		 */
		frontLeftCIM = new Jaguar(3);
		frontRightCIM = new Jaguar(1);
		backLeftCIM = new Jaguar(4);
		backRightCIM = new Jaguar(0);
		frontTurn = new CANTalon(1);
		backTurn = new CANTalon(2);

		frontEncoder = new Encoder(1, 0); // encoder for frontTurn CANTalon
		backEncoder = new Encoder(2, 3); // encoder for backTurn CANTalon

		frontPIDSource = new PIDSource() {
			public double getInput() {
				SmartDashboard.putNumber("InputFront", frontEncoder.get());
				return frontEncoder.get();
			}
		};
		backPIDSource = new PIDSource() {
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

	/** Sets the power for each of the motors. <b>Each of the parameters should be between -1.0 and 1.0</b>
	 * @param frontLeft
	 *            power for motor
	 * @param frontRight
	 *            power for motor
	 * @param backLeft
	 *            power for motor
	 * @param backRight
	 *            power for motor
	 * @param frontRotation
	 *            speed for the motor that rotates the front motors
	 * @param backRotation
	 *            speed for the motor that rotates the back motors
	 */
	public void drive(double frontLeft, double frontRight, double backLeft, double backRight, double frontRotation,
			double backRotation) {
		frontLeftCIM.set(-frontLeft);
		frontRightCIM.set(frontRight);
		backLeftCIM.set(-backLeft);
		backRightCIM.set(-backRight);
		frontTurn.set(frontRotation);
		backTurn.set(backRotation);
	}

	/**
	 * @param frontSetpoint
	 *            sets the desired target value of front motors PID
	 * @param backSetpoint
	 *            sets the desired target value of the back motors PID
	 */
	public void turnDrive(double frontSetpoint, double backSetpoint) {
		frontPID.setSetpoint(frontSetpoint);
		backPID.setSetpoint(backSetpoint);
	}

	public double getFrontOutput() {
		return frontPID.getOutput();
	}

	public double getBackOutput() {
		return backPID.getOutput();
	}

	public void stop() {
		drive(0, 0, 0, 0, 0, 0);
	}
}