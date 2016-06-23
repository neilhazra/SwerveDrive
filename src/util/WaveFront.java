package util;

import java.util.ArrayList;

public class WaveFront {
	long timeout;
	// HexNode map[][];
	SquareNode[][] map;
	int width;
	int length;

	public WaveFront(int width, int length, long timeout) {
		this.width = width;
		this.length = length;
		for (int w = 0; w < width; w++) {
			for (int l = 0; l < length; l++) {
				map[w][l] = new SquareNode(w, l);
			}
		}
		this.timeout = timeout;
	}

	public void mergeStates(State[][] states) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < length; y++) {
				map[x][y].setState(states[x][y]);
			}
		}
	}

	public void mergeTags(Tag[][] tags) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < length; y++) {
				map[x][y].setTag(tags[x][y]);
			}
		}
	}

	public void propagateWave(SquareNode goal, SquareNode currentNode, int depth) {
		currentNode.setValue(depth);
		currentNode.setState(State.Numbered);
		if (goal.equals(currentNode)) {
			return;
		} else {
			for (int i = 0; i < currentNode.adjacents.size(); i++) {
				if (currentNode.adjacents.get(i).getValue() < depth) {
					currentNode.adjacents.remove(i);
				}
			}
			depth++;
			for (int i = 0; i < currentNode.adjacents.size(); i++) {
				propagateWave(goal, currentNode.adjacents.get(i), depth);
			}
		}
	}

	public class SquareNode {
		int x;
		int y;
		public int value;
		long time_checked;
		private State state;
		private Tag tag;
		SquareNode[] potentialAdjacents;
		ArrayList<SquareNode> adjacents = new ArrayList<SquareNode>();
		// SquareNode[] adjacents;

		SquareNode(int x, int y) {
			this.x = x;
			this.y = y;
			potentialAdjacents[1] = map[x][y + 1];
			potentialAdjacents[2] = map[x][y - 1];
			potentialAdjacents[3] = map[x + 1][y + 1];
			potentialAdjacents[4] = map[x + 1][y];
			potentialAdjacents[5] = map[x + 1][y - 1];
			potentialAdjacents[6] = map[x - 1][y + 1];
			potentialAdjacents[7] = map[x - 1][y];
			potentialAdjacents[8] = map[x - 1][y - 1];
		}

		boolean equals(SquareNode otherNode) {
			if (this.x == otherNode.x && this.y == otherNode.y) {
				return true;
			} else {
				return false;
			}
		}

		ArrayList<SquareNode> returnAdjacents() {
			for (int i = 0; i < 8; i++) {
				if (potentialAdjacents[i].getState() == State.Open) {
					adjacents.add(potentialAdjacents[i]);
				}
			}
			return adjacents;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		void setState(State state) {
			if (state == State.Blocked) {
				time_checked = System.currentTimeMillis();
			}
			this.state = state;
		}

		State getState() {
			if (state == State.Blocked && System.currentTimeMillis() - time_checked > WaveFront.this.timeout) {
				setState(State.Open);
			}
			return state;
		}

		void setTag(Tag tag) {
			this.tag = tag;
		}

		Tag getTag() {
			return tag;
		}
	}

	public enum State {
		Boundary, PermanentBlock, Blocked, Destination, Robot, Unreachable, Open, Numbered
		// Boundary a node that lies on the boundary of reachable robot area
		// Blocked a node that cannot be crossed temporarily
		// PermanentBlock, a node that can never be crossed
		// Destination, the robots target location
		// Robot, the robot's current location
		// Unreachable, any node that cannot be reached
	}

	public enum Tag {
		Favorable(1), // Good node to try
		Unfavorable(2), // Not a good node to try
		Avoid(3); // Don't travel unless must

		private final int value;

		public int getValue() {
			return value;
		}

		Tag(int value) {
			this.value = value;
		}
	}
}
