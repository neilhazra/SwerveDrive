package org.usfirst.frc.team2976.robot.subsystems;

import org.usfirst.frc.team2976.robot.Robot;
import org.usfirst.frc.team2976.robot.RobotMap;
import org.usfirst.frc.team2976.robot.commands.DriveWithJoystick;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.PIDMain;
import util.PIDSource;

public class DriveTrain extends Subsystem {
	// Subsystem devices
	private SpeedController frontLeftCIM, frontRightCIM;
	private SpeedController backLeftCIM, backRightCIM;
	private SpeedController frontTurn, backTurn;
	private Encoder frontEncoder, backEncoder;
	private AnalogGyro gyro;
	public PIDSource frontPIDSource, backPIDSource, gyroPIDSource;
	public PIDMain frontPID, backPID, gyroPID;

	public DriveTrain() {
		frontLeftCIM = new Jaguar(RobotMap.frontLeftCIMPort);
		frontRightCIM = new Jaguar(RobotMap.frontRightCIMPort);
		backLeftCIM = new Jaguar(RobotMap.backLeftCIMPort);
		backRightCIM = new Jaguar(RobotMap.backRightCIMPort);
		frontTurn = new CANTalon(RobotMap.frontTurnMotorPort);
		backTurn = new CANTalon(RobotMap.backTurnMotorPort);

		frontEncoder = new Encoder(RobotMap.frontEncoderAPort, RobotMap.frontEncoderBPort);
		backEncoder = new Encoder(RobotMap.backEncoderAPort, RobotMap.backEncoderBPort);

		gyro = new AnalogGyro(RobotMap.gyroPort);

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
		gyroPIDSource = new PIDSource() {
			public double getInput() {
				// SmartDashboard.putNumber("GyroInput", gyro.getAngle());
				return gyro.getAngle();
			}
		};

		frontPID = new PIDMain(frontPIDSource, 0, 150, 0.004, 0.0008, 0.0);// controls
																			// front
																			// motor
																			// angle
		backPID = new PIDMain(backPIDSource, 0, 150, 0.004, 0.00075, 0.0);// controls
																			// back
																			// motor
																			// angle
		gyroPID = new PIDMain(gyroPIDSource, 0, 150, 0.0005, 0.000, 0.0);// PID
																			// control
																			// for
																			// driving
																			// diagonal
		gyroPID.setOutputLimits(-0.2, 0.2);
		backPID.setOutputLimits(-0.75, 0.75);
		frontPID.setOutputLimits(-0.75, 0.75);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new DriveWithJoystick());
	}

	/**
	 * Sets the power for each of the motors. <b>Each of the parameters should
	 * be between -1.0 and 1.0</b>
	 * 
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
	private void drive(double frontLeft, double frontRight, double backLeft, double backRight, double frontRotation,
			double backRotation) {
		frontLeftCIM.set(-frontLeft);
		frontRightCIM.set(frontRight);
		backLeftCIM.set(-backLeft);
		backRightCIM.set(-backRight);
		frontTurn.set(frontRotation);
		backTurn.set(backRotation);
		SmartDashboard.putNumber("Back-front", backLeft-frontRight);
		
	}

	/**
	 * @param frontSetpoint
	 *            sets the desired target value of front motors PID
	 * @param backSetpoint
	 *            sets the desired target value of the back motors PID
	 */
	private void turnDrive(double frontSetpoint, double backSetpoint) {
		frontPID.setSetpoint(frontSetpoint);
		backPID.setSetpoint(backSetpoint);
	}

	/**
	 * PID Control for going diagonal
	 * 
	 * @param setpoint
	 *            the current heading of the robot set the setpoint to this
	 *            value to ensure the robot continues to travel straight
	 * @param y
	 *            the raw power from the joystick
	 */
	public void diagonalDrive(double y, double currentHeading, double frontSetpoint, double backSetpoint) {
		turnDrive(frontSetpoint, backSetpoint);
		gyroPID.setSetpoint(currentHeading);
		double turnCorrection = 0;
		if (Math.abs(y) > 0) {
			turnCorrection = Math.abs(gyroPID.getOutput());
			SmartDashboard.putNumber("outputgyro", turnCorrection);
		}
		if (frontSetpoint < 0) {
			if (gyroPID.getError() < 0) {
				drive(y + turnCorrection, y + turnCorrection, y, y, getFrontOutput(), getBackOutput());
			} else {
				drive(y, y, y + turnCorrection, y + turnCorrection, getFrontOutput(), getBackOutput());
			}
		}	else {
			if (gyroPID.getError()  > 0) {
				drive(y + turnCorrection, y + turnCorrection, y, y, getFrontOutput(), getBackOutput());
			} else {
				drive(y, y, y + turnCorrection, y + turnCorrection, getFrontOutput(), getBackOutput());
			}
		}
	}

	/**
	 * @param y
	 *            the raw power from the joystick
	 */
	public void swerveDrive(double y, double frontSetpoint, double backSetpoint) {
		turnDrive(frontSetpoint, backSetpoint);
		drive(y, y, y, y, getFrontOutput(), getBackOutput());
	}

	public void stop() {
		drive(0, 0, 0, 0, 0, 0);
	}

	public double getGyro() {
		return gyroPID.getInput();
	}

	public double constrain(double x, double lowerLimit, double upperLimit) {
		x = x > upperLimit ? upperLimit : x;
		x = x < lowerLimit ? lowerLimit : x;
		return x;
	}

	public double getFrontOutput() {
		return frontPID.getOutput();
	}

	public double getBackOutput() {
		return backPID.getOutput();
	}

}