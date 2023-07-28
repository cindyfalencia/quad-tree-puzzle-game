package assignment3;

import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		Color[][] grid = board.flatten();
		int score = 0, L = grid.length - 1;
		for (int i = 0; i < grid.length; i++) {
			score += (grid[0][i].equals(targetGoal) ? 1 : 0)
					+ (grid[L][i].equals(targetGoal) ? 1 : 0)
					+ (grid[i][0].equals(targetGoal) ? 1 : 0)
					+ (grid[i][L].equals(targetGoal) ? 1 : 0);
		}
		return score;
	}

	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal)
				+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
