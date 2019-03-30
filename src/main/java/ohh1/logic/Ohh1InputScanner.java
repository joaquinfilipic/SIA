package ohh1.logic;

import gps.SearchStrategy;
import ohh1.exception.RequestException;
import ohh1.model.CellColor;
import ohh1.model.Ohh1State;
import ohh1.model.Point;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

public class Ohh1InputScanner {

    public static Ohh1State scanInitialState(MultipartFile file) {

        Scanner sc = null;
        try {

            sc = new Scanner(file.getInputStream());

            // Scan the size of the board
            int size = sc.nextInt();

            if ((size % 2) == 1) {
                throw new RequestException(HttpStatus.BAD_REQUEST, "Size parameter must be an even positive number");
            }

            int[][] board = new int[size][size];

            List<List<Point>> fixedPointsByRow = new ArrayList<>();

            // Scan the board with the fixed points
            for (int i = 0; i < size; i++) {

                List<Point> fixedPoints = new ArrayList<>();

                for (int j = 0; j < size; j++) {
                    board[i][j] = sc.nextInt();
                    if (board[i][j] != CellColor.BLANK.getValue()) {
                        fixedPoints.add(new Point(i, j));
                    }
                }

                fixedPointsByRow.add(fixedPoints);
            }

            return new Ohh1State(board, fixedPointsByRow);

        } catch (IOException exception) {

            exception.printStackTrace();
            throw new RequestException(HttpStatus.BAD_REQUEST, "Incorrect or corrupt file used as input.");

        } catch (InputMismatchException exception) {

            exception.printStackTrace();
            throw new RequestException(HttpStatus.BAD_REQUEST,
                    "Token in board does not match the Integer regular expression, or is out of range.");

        } catch (NoSuchElementException exception) {

            exception.printStackTrace();
            throw new RequestException(HttpStatus.BAD_REQUEST,
                    "Found end of file while reading the board. Verify the size and board inputs.");
        } finally {

            if (sc != null) {
                sc.close();
            }

        }
    }

    public static SearchStrategy scanStrategy(String strategy) {

        switch (strategy.toLowerCase()) {

            case "dfs":
                return SearchStrategy.DFS;

            case "bfs":
                return SearchStrategy.BFS;

            case "iddfs":
                return SearchStrategy.IDDFS;

            case "astar":
                return SearchStrategy.ASTAR;

            case "greedy":
                return SearchStrategy.GREEDY;

            default:
                throw new RequestException(HttpStatus.BAD_REQUEST,
                        "Incorrect search algorithm provided. Options are: DFS, BFS, IDDFS, ASTAR and GREEDY.");

        }
    }
}
