package assignment3;

import java.awt.Color;

public class BlobGoal extends Goal{
	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		Color[][] grid = board.flatten();

		int score = 0;
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid.length; j++) {
				int blobScore = undiscoveredBlobSize(
						i, j, grid, new boolean[grid.length][grid.length]);
				score = Math.max(score, blobScore);
			}
		return score;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal) 
		+ " blocks, anywhere within the block";
	}

	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		if (!(0 <= i && i < unitCells.length
				&& 0 <= j && j < unitCells.length
				&& unitCells[i][j].equals(targetGoal)
				&& !visited[i][j]))
			return 0;

		visited[i][j] = true;
		return 1 + undiscoveredBlobSize(i + 1, j, unitCells, visited)
				+ undiscoveredBlobSize(i - 1, j, unitCells, visited)
				+ undiscoveredBlobSize(i, j + 1, unitCells, visited)
				+ undiscoveredBlobSize(i, j - 1, unitCells, visited);
	}

}
