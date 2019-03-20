package ohh1.logic;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import gps.SearchStrategy;
import ohh1.exception.RequestException;

public class Ohh1InputScanner {

	public static int[][] scanBoard(MultipartFile file) {

		Scanner sc = null;
		try {

			sc = new Scanner(file.getInputStream());

			// Scan the size of the board
			int size = sc.nextInt();

			int[][] board = new int[size][size];

			// Scan the board
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					board[i][j] = sc.nextInt();
				}
			}

			sc.close();

			return board;

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
	
	//TODO: complete with scanHeuristic method.

}
