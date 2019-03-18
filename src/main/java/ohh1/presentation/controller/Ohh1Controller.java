package ohh1.presentation.controller;

import java.io.IOException;
import java.util.Scanner;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
public class Ohh1Controller {

	@RequestMapping(
			path = "/resolve",
			consumes = {"multipart/form-data"},
			produces = {"application/json"},
			method = RequestMethod.POST)
	public String resolveBoard(
			@RequestPart(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "size", required = true) int size) {

		Scanner sc = null;
		try {
			sc = new Scanner(file.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int[][] board = new int[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = sc.nextInt();
			}
		}

		System.out.println("Ohh1State: ");
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (board[i][j] >= 0) {
					System.out.print(" " + board[i][j]);
				}
				else {
					System.out.print(board[i][j]);
				}
			}
			System.out.println("");
		}

		return JSONObject.quote("El tablero fue procesado correctamente");
	}

}
