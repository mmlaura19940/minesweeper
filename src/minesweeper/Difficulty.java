package minesweeper;

public enum Difficulty {
	EASY(8, 5), NORMAL(10, 30), HARD(12, 40);

	private final int size;
	private final int maxCountOfMines;

	private Difficulty(int size, int maxCountOfMines) {
		this.size = size;
		this.maxCountOfMines = maxCountOfMines;
	}

	public int getSize() {
		return size;
	}

	public int getMaxCountOfMines() {
		return maxCountOfMines;
	}

	public static Difficulty getByName(String name) throws IllegalArgumentException {
		if (name != null && !name.isEmpty()) {
			for (Difficulty candidate : values()) {
				if (candidate.name().equalsIgnoreCase(name)) {
					return candidate;
				}
			}
		}

		throw new IllegalArgumentException();

	}
}
