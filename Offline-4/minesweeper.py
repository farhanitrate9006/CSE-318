import random


class Minesweeper():
    """
    Minesweeper game representation
    """

    def __init__(self, height=8, width=8, mines=8):

        # Set initial width, height, and number of mines
        self.height = height
        self.width = width
        self.mines = set()

        # Initialize an empty field with no mines
        self.board = []
        for i in range(self.height):
            row = []
            for j in range(self.width):
                row.append(False)
            self.board.append(row)

        # Add mines randomly
        while len(self.mines) != mines:
            i = random.randrange(height)
            j = random.randrange(width)
            if not self.board[i][j]:
                self.mines.add((i, j))
                self.board[i][j] = True

        # At first, player has found no mines
        self.mines_found = set()

    def print(self):
        """
        Prints a text-based representation
        of where mines are located.
        """
        for i in range(self.height):
            print("--" * self.width + "-")
            for j in range(self.width):
                if self.board[i][j]:
                    print("|X", end="")
                else:
                    print("| ", end="")
            print("|")
        print("--" * self.width + "-")

    def is_mine(self, cell):
        i, j = cell
        return self.board[i][j]

    def nearby_mines(self, cell):
        """
        Returns the number of mines that are
        within one row and column of a given cell,
        not including the cell itself.
        """

        # Keep count of nearby mines
        count = 0

        # Loop over all cells except diagonals within one row and column
        neighbourHeight = [cell[0] - 1, cell[0] + 1]
        for height in neighbourHeight:
            if 0 <= height < self.height:
                if self.board[height][cell[1]]:
                    count += 1

        neighbourWidth = [cell[1] - 1, cell[1] + 1]
        for width in neighbourWidth:
            if 0 <= width < self.width:
                if self.board[cell[0]][width]:
                    count += 1

        return count

    def won(self):
        """
        Checks if all mines have been flagged.
        """
        return self.mines_found == self.mines


class Sentence():
    """
    Logical statement about a Minesweeper game
    A sentence consists of a set of board cells,
    and a count of the number of those cells which are mines.
    """

    def __init__(self, cells, count):
        self.cells = set(cells)
        self.count = count

    def __eq__(self, other):
        return self.cells == other.cells and self.count == other.count

    def __str__(self):
        return f"{self.cells} = {self.count}"

    def known_mines(self):
        """
        Returns the set of all cells in self.cells known to be mines.
        """
        if len(self.cells) == self.count and self.count != 0:
            return self.cells
        return set()

    def known_safes(self):
        """
        Returns the set of all cells in self.cells known to be safe.
        """
        if self.count == 0:
            return self.cells
        return set()

    def mark_mine(self, cell):
        """
        Updates internal knowledge representation given the fact that
        a cell is known to be a mine.
        """
        if cell in self.cells:
            self.cells.remove(cell)
            self.count -= 1

    def mark_safe(self, cell):
        """
        Updates internal knowledge representation given the fact that
        a cell is known to be safe.
        """
        if cell in self.cells:
            self.cells.remove(cell)


class MinesweeperAI():
    """
    Minesweeper game player
    """

    def __init__(self, height=8, width=8):

        # Set initial height and width
        self.height = height
        self.width = width

        # Keep track of which cells have been clicked on
        self.moves_made = set()

        # Keep track of cells known to be safe or mines
        self.mines = set()
        self.safes = set()

        # List of sentences about the game known to be true
        self.knowledge = []

    def mark_mine(self, cell):
        """
        Marks a cell as a mine, and updates all knowledge
        to mark that cell as a mine as well.
        """
        self.mines.add(cell)
        for sentence in self.knowledge:
            sentence.mark_mine(cell)

    def mark_safe(self, cell):
        """
        Marks a cell as safe, and updates all knowledge
        to mark that cell as safe as well.
        """
        self.safes.add(cell)
        for sentence in self.knowledge:
            sentence.mark_safe(cell)

    def add_knowledge(self, cell, count):
        """
        Called when the Minesweeper board tells us, for a given
        safe cell, how many neighboring cells have mines in them.

        This function should:
            1) mark the cell as a move that has been made
            2) mark the cell as safe
            3) add a new sentence to the AI's knowledge base
               based on the value of `cell` and `count`
            4) mark any additional cells as safe or as mines
               if it can be concluded based on the AI's knowledge base
            5) add any new sentences to the AI's knowledge base
               if they can be inferred from existing knowledge
        """
        # Part-1 #
        self.moves_made.add(cell)
        # Part-2 #
        self.mark_safe(cell)

        # Part-3 #
        undeterminedCells = set()
        mineCount = 0

        # for up and down neighbour
        neighbourHeight = [cell[0] - 1, cell[0] + 1]
        for height in neighbourHeight:
            # ignoring already determined cells #
            if (height, cell[1]) in self.safes:
                continue
            if (height, cell[1]) in self.mines:
                mineCount += 1
                continue

            # adding cell as undetermined if it is in range
            if 0 <= height < self.height:
                undeterminedCells.add((height, cell[1]))

        # for left and right neighbour
        neighbourWidth = [cell[1] - 1, cell[1] + 1]
        for width in neighbourWidth:
            # ignoring already determined cells #
            if (cell[0], width) in self.safes:
                continue
            if (cell[0], width) in self.mines:
                mineCount += 1
                continue

            # adding cell as undetermined if it is in range
            if 0 <= width < self.width:
                undeterminedCells.add((cell[0], width))

        newSentence = Sentence(undeterminedCells, count - mineCount)
        self.knowledge.append(newSentence)

        # for sentence in self.knowledge:
        #     if sentence.known_safes():
        #         for cell in sentence.known_safes().copy():
        #             self.mark_safe(cell)
        #     if sentence.known_mines():
        #         for cell in sentence.known_mines().copy():
        #             self.mark_mine(cell)

        # Part 4-5 #
        knowledge_changed = True

        # this loop will iterate as long as we are getting new information
        while knowledge_changed:
            knowledge_changed = False

            # mark any additional cells as safe or as mine
            for sentence in self.knowledge:
                if sentence.known_safes():
                    knowledge_changed = True
                    for cell in sentence.known_safes().copy():
                        self.mark_safe(cell)
                if sentence.known_mines():
                    knowledge_changed = True
                    for cell in sentence.known_mines().copy():
                        self.mark_mine(cell)

            # try to infer new sentences from the current ones
            for sentence_1 in self.knowledge:
                for sentence_2 in self.knowledge:

                    # Ignore when sentences are identical
                    if sentence_1.cells == sentence_2.cells:
                        continue

                    # Create a new sentence if 1 is subset of 2, and not in KB
                    if sentence_1.cells.issubset(sentence_2.cells):
                        new_sentence_cells = sentence_2.cells - sentence_1.cells
                        new_sentence_count = sentence_2.count - sentence_1.count
                        new_sentence = Sentence(new_sentence_cells, new_sentence_count)

                        # Add to knowledge if not already in KB
                        if new_sentence not in self.knowledge:
                            knowledge_changed = True
                            self.knowledge.append(new_sentence)

        # Print out AI current knowledge to terminal
        print('Known Mines: ', self.mines)
        print('Safe Moves Remaining: ', self.safes - self.moves_made)
        print('========================')

    def make_safe_move(self):
        """
        Returns a safe cell to choose on the Minesweeper board.
        The move must be known to be safe, and not already a move
        that has been made.

        This function may use the knowledge in self.mines, self.safes
        and self.moves_made, but should not modify any of those values.
        """
        for cell in self.safes:
            if cell not in self.moves_made:
                return cell
        return None

    def make_random_move(self):
        """
        Returns a move to make on the Minesweeper board.
        Should choose randomly among cells that:
            1) have not already been chosen, and
            2) are not known to be mines
        """
        probableSafeMoves = []

        for i in range(self.width):
            for j in range(self.height):
                if (i, j) not in self.moves_made and (i, j) not in self.mines:
                    probableSafeMoves.append((i, j))

        if len(probableSafeMoves) == 0:
            return None
        return random.choice(probableSafeMoves)