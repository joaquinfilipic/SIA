package ohh1.presentation.controller;

import java.io.IOException;
import java.util.Scanner;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gps.GPSEngine;
import gps.SearchStrategy;
import gps.api.Problem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ohh1.exception.RequestException;
import ohh1.logic.Ohh1InputScanner;
import ohh1.logic.Ohh1Problem;

@RestController
@RequestMapping("/")
public class Ohh1Controller {
	
	private static final Logger log = LoggerFactory.getLogger(Ohh1Controller.class);

	@RequestMapping(
			path = "/resolve", 
			consumes = { "multipart/form-data" }, 
			method = RequestMethod.POST)
	public String resolveBoard(
			@RequestPart(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "strategy", required = false) String strategy
			) {
		
		int[][] board = Ohh1InputScanner.scanBoard(file);
		SearchStrategy searchStrategy = Ohh1InputScanner.scanStrategy(strategy);

		log.info("Input correctly scanned");
		
		Problem problem = new Ohh1Problem(board);
		GPSEngine engine = new GPSEngine(problem, searchStrategy, null);
		
		engine.findSolution();
		
		if (engine.isFailed()) {
			throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Engine failed to find solution");
		}
		if (engine.isFinished()) {
			log.info("Engine finished");
		}
		
		String solution = engine.getSolutionNode().getSolution();
		
		System.out.println(solution);
		
		return engine.getSolutionNode().getState().getRepresentation();
	}

}
