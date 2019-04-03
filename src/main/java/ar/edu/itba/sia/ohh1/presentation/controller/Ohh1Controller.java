package ar.edu.itba.sia.ohh1.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ar.edu.itba.sia.gps.GPSEngine;
import ar.edu.itba.sia.gps.SearchStrategy;
import ar.edu.itba.sia.gps.api.Problem;
import ar.edu.itba.sia.ohh1.exception.RequestException;
import ar.edu.itba.sia.ohh1.logic.Ohh1Heuristic;
import ar.edu.itba.sia.ohh1.logic.Ohh1InputScanner;
import ar.edu.itba.sia.ohh1.logic.Ohh1Problem;
import ar.edu.itba.sia.ohh1.model.HeuristicEnum;
import ar.edu.itba.sia.ohh1.model.Ohh1State;

@RestController
@RequestMapping("/")
public class Ohh1Controller {

	private static final Logger log = LoggerFactory.getLogger(Ohh1Controller.class);

	@RequestMapping(
			path = "/resolve", 
			consumes = { "multipart/form-data" }, 
			method = RequestMethod.POST)
	public String resolveBoard(
			@RequestPart(value = "file") MultipartFile file,
			@RequestParam(value = "strategy") String strategy,
			@RequestParam(value = "heuristic", required = false) Integer heuristic) {

		Ohh1State initialState = Ohh1InputScanner.scanInitialState(file);
		SearchStrategy searchStrategy = Ohh1InputScanner.scanStrategy(strategy);

		if (heuristic == null) {
			heuristic = HeuristicEnum.FIRST.getValue();
		}

		verifyParams(searchStrategy, heuristic);
		log.info("Input correctly scanned");

		Problem problem = new Ohh1Problem(initialState);
		GPSEngine engine = generateGPSEngine(problem, searchStrategy, heuristic);

		long startTime = System.nanoTime();
		engine.findSolution();
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;

		if (engine.isFailed()) {
			throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR, "Engine failed to find solution");
		}
		if (engine.isFinished()) {
			log.info("Engine finished");
		}

		String solution = engine.getSolutionNode().getSolution();

		printResults(engine, searchStrategy, solution, startTime, heuristic, totalTime);

		return engine.getSolutionNode().getState().getRepresentation();
	}

	private void verifyParams(final SearchStrategy searchStrategy, final Integer heuristic) {

		if ((searchStrategy == SearchStrategy.ASTAR || searchStrategy == SearchStrategy.GREEDY)
				&& (heuristic != HeuristicEnum.FIRST.getValue() && heuristic != HeuristicEnum.SECOND.getValue())) {
			throw new RequestException(HttpStatus.BAD_REQUEST,
					"A Star and GREEDY required a valid heuristic param, " + "heuristic ∈ {1, 2}");
		}
	}

	private GPSEngine generateGPSEngine(final Problem problem, final SearchStrategy searchStrategy,
			final Integer heuristic) {
		GPSEngine engine;

		if (searchStrategy == SearchStrategy.ASTAR || searchStrategy == SearchStrategy.GREEDY) {
			engine = new GPSEngine(problem, searchStrategy, new Ohh1Heuristic(heuristic));
		} else if (searchStrategy == SearchStrategy.IDDFS) {
			engine = new GPSEngine(problem, searchStrategy, null);
		} else {
			engine = new GPSEngine(problem, searchStrategy, null);
		}

		return engine;
	}

	private void printResults(GPSEngine engine, SearchStrategy searchStrategy, String solution, long startTime,
			Integer heuristic, long totalTime) {
		System.out.println();
		System.out.println("Solution: \n\n" + solution);
		System.out.println("Strategy: " + engine.getStrategy());

		if (searchStrategy == SearchStrategy.GREEDY || searchStrategy == SearchStrategy.ASTAR) {
			System.out.println("Heuristic: " + heuristic);
		}

		System.out.println(("Solution node cost: " + engine.getSolutionNode().getCost()));
		System.out.println("Explosion counter: " + engine.getExplosionCounter());
		System.out.println("Solution node depth: " + engine.getSolutionNode().getDepth());
		System.out.println("Analize states: " + engine.getAnalyzedStates());
		System.out.println("Find solution: " + engine.isFinished());
		System.out.println("Execution time: " + totalTime + " ns");
	}
}
