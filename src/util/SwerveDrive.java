package util;

/*
 							0 degrees
                              |
                              |
                              |
                              |
                              |
       -90 degrees____________|______________ 90 degrees
                              |
                              |
                              |
                              |
                              |
*/
public class SwerveDrive {
	Vector directionVector = new Vector(0, 0);
	Vector[] wheelDirectionVectors = new Vector[4];
	Vector[] finalVectors = new Vector[4];

	public SwerveDrive() {
		for (int i = 0; i < 4; i++) {
			wheelDirectionVectors[i] = new Vector(0, 0);
			finalVectors[i] = new Vector(0, 0);
		}
	}

	/**
	 * i=0 -->front left, rear left,
	 * 
	 * @param x
	 * @param y
	 * @param rotation
	 * @return
	 */
	public Vector[] compute(double x, double y, double rotation) {
		directionVector.set(x, y);
		wheelDirectionVectors[0].set(rotation, 45, true);
		wheelDirectionVectors[1].set(rotation, 360 - 45, true);
		wheelDirectionVectors[2].set(rotation, 180 + 45, true);
		wheelDirectionVectors[3].set(rotation, 180 - 45, true);

		for (int i = 0; i < 4; i++) {
			finalVectors[i] = wheelDirectionVectors[i].add(directionVector);
		}
		double mag;
		if (finalVectors[0].r > 1) {
			mag = finalVectors[0].getMagnitude();
			for (int i = 0; i < 4; i++) {
				finalVectors[i] = finalVectors[i].multiplyScalar(1 / mag);
			}
		}

		if (finalVectors[1].r > 1) {
			mag = finalVectors[1].getMagnitude();
			for (int i = 0; i < 4; i++) {
				finalVectors[i] = finalVectors[i].multiplyScalar(1 / mag);
			}
		}
		if (finalVectors[2].r > 1) {
			mag = finalVectors[2].getMagnitude();
			for (int i = 0; i < 4; i++) {
				finalVectors[i] = finalVectors[i].multiplyScalar(1 / mag);
			}
		}
		if (finalVectors[3].r > 1) {
			mag = finalVectors[3].getMagnitude();
			for (int i = 0; i < 4; i++) {
				finalVectors[i] = finalVectors[i].multiplyScalar(1 / mag);
			}
		}

		return finalVectors;
	}

	public class Vector {
		double x;
		double y;
		double r;
		double theta;

		public Vector(double x, double y) {
			this.x = x;
			this.y = y;
			r = Math.hypot(x, y);
			theta = Math.toDegrees(Math.atan2(y, x));
		}

		/**
		 * @param r
		 * @param theta
		 * @param isPolar
		 */
		public Vector(double r, double theta, boolean isPolar) {
			this.r = r;
			this.theta = theta;
			this.x = r * Math.cos(Math.toRadians(theta));
			this.y = r * Math.sin(Math.toRadians(theta));
		}

		public Vector getEquivalentVector() {
			return new Vector(-r, (theta + 180) % 360, true);
		}

		public void set(double r, double theta, boolean isPolar) {
			this.r = r;
			this.theta = theta;
			this.x = r * Math.cos(Math.toRadians(theta));
			this.y = r * Math.sin(Math.toRadians(theta));
		}

		public void set(double x, double y) {
			this.x = x;
			this.y = y;
			r = Math.hypot(x, y);
			theta = Math.toDegrees(Math.atan2(y, x));
		}

		public double getAngleDegrees() {
			return Math.toDegrees(Math.atan2(y, x));
		}

		public double getMagnitude() {
			return Math.hypot(x, y);
		}

		public Vector multiplyScalar(double a) {
			return new Vector(x * a, y * a);
		}

		public Vector add(Vector v) {
			return new Vector(x + v.x, y + v.y);
		}

		public Vector getUnitVector() {
			return multiplyScalar(1 / getMagnitude());
		}
	}
}

