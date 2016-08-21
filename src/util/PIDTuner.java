package util;

import java.util.Timer;
import java.util.TimerTask;

public class PIDTuner {
	private Timer PIDTuner;
	protected PIDSource pidsource;
	protected double input;
	protected double prevInput;
	protected double[] smallBuffer;
	protected double lastMin;
	protected double min[];
	public double period[];
	int i = 0;
	int k = 0;
	int j = 0;

	public PIDTuner(Object pidsource) {
		this.pidsource = (PIDSource) pidsource;
		PIDTuner = new Timer();
		// Schedule super quickly so we can accurately time the ocsillations
		PIDTuner.scheduleAtFixedRate(new Calculate(), 0, 10);
		smallBuffer = new double[3];
		min = new double[2];
		period = new double[10];
	}

	public double averagePeriod() {
		long cumulative = 0;
		for (int i = 0; i < 10; i++) {
			cumulative += period[i];
		}
		return cumulative / 10;
	}

	private class Calculate extends TimerTask {
		public void run() {
			input = pidsource.getInput();
			addValue(input - prevInput, i, smallBuffer.length, smallBuffer);
			if (k > 3) {
				if (Math.abs(smallBuffer[0]) > Math.abs(smallBuffer[1])
						&& Math.abs(smallBuffer[2]) > Math.abs(smallBuffer[1])) {

				} else {
					min[k % 2] = System.currentTimeMillis();
					k++;
				}
			}
			addValue(Math.abs(2 * (min[1] - min[0])), j, period.length, period);

			prevInput = input;
			i++;
			j++;
		}

		void addValue(double value, int k, int length, double[] buffer) {
			k = k % length;
			buffer[k] = value;
		}
	}
}
