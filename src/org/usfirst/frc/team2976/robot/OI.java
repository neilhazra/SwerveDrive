package org.usfirst.frc.team2976.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import org.usfirst.frc.team2976.robot.commands.ExampleCommand;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	private Joystick joystick;
	
	static Preferences myPrefs;
	private boolean usingXboxController;
	
	public enum Button {
		RBumper(6), LBumper(5), A(1), B(2), X(3), Y(4), RightJoystickBtn(10), LeftJoystickBtn(9);

		private final int number;
		Button(int number) {
			this.number = number;
		}

		public int getBtnNumber() {
			return number;
		}
	}
	public enum Axis {
		LX(0), LY(1), LTrigger(2), RTrigger(3), RX(4), RY(5);
		private final int number;

		Axis(int number) {
			this.number = number;
		}

		public int getAxisNumber() {
			return number;
		}
	}
	
	public OI() {
		joystick = new Joystick(0);
		
		usingXboxController = myPrefs.getBoolean("Using Xbox controller", true);
	}
	
	public double getXboxX() {
		return -joystick.getRawAxis(Axis.RX.getAxisNumber());
	}
	
	public double getXboxY() {
		return -joystick.getRawAxis(Axis.LY.getAxisNumber());
	}
	
	public double getJoystickX() {
		return joystick.getX();
	}
	
	public double getJoystickY() {
		return -joystick.getY();
	}
	
	public double getJoystickZ() {
		return joystick.getZ();
	}
	
	public double getJoystickThrottle() {
		return joystick.getThrottle();
	}

	public double getJoystickTwist() {
		return joystick.getTwist();
	}
	
	public double getXAxis() {
		return usingXboxController ? getXboxX() : getJoystickX();
	}

	public double getYAxis() {
		return usingXboxController ? -getXboxY() : getJoystickY();
	}
	
	public boolean getDiagonalButton() {
		return joystick.getRawButton(1);
	}
	
	public double getDiagonalRight() {
		if(usingXboxController) {
			return Robot.oi.joystick.getRawAxis(OI.Axis.RTrigger.getAxisNumber());
		}
		
		// else, using a joystick
		if(getDiagonalButton()) {
			return getXAxis();
		}
		return 0;
	}
	
	public double getDiagonalLeft() {
		if(usingXboxController) {
			return Robot.oi.joystick.getRawAxis(OI.Axis.LTrigger.getAxisNumber());
		}
		
		// else, using a joystick
		if(getDiagonalButton()) {
			return getXAxis();
		}
		return 0;
	}

}