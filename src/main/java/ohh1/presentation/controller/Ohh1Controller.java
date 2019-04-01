package ohh1.presentation.controller;

import gps.GPSEngine;
import gps.SearchStrategy;
import gps.api.Problem;
import ohh1.exception.RequestException;
import ohh1.logic.Ohh1Heuristic;
import ohh1.logic.Ohh1InputScanner;
import ohh1.logic.Ohh1Problem;
import ohh1.model.HeuristicEnum;
import ohh1.model.Ohh1State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/")
public class Ohh1Controller {

    private static final Logger log = LoggerFactory.getLogger(Ohh1Controller.class);

    @RequestMapping(
            path = "/resolve",
            consumes = {"multipart/form-data"},
            method = RequestMethod.POST)
    public String resolveBoard(
            @RequestPart(value = "file") MultipartFile file,
            @RequestParam(value = "strategy") String strategy,
            @RequestParam(value = "heuristic", required = false) Integer heuristic,
            @RequestParam(value = "iterativeDepth", required = false) Integer iterativeDepth
    ) {

        Ohh1State initialState = Ohh1InputScanner.scanInitialState(file);
        SearchStrategy searchStrategy = Ohh1InputScanner.scanStrategy(strategy);

        if (heuristic == null) {
            heuristic = 1;
        }

        if (iterativeDepth == null) {
            iterativeDepth = 2;
        }

        verifyParams(searchStrategy, heuristic, iterativeDepth);
        log.info("Input correctly scanned");

        Problem problem = new Ohh1Problem(initialState);
        GPSEngine engine = generateGPSEngine(problem, searchStrategy, heuristic, iterativeDepth);


        long startTime = System.nanoTime();
        engine.findSolution();
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;

        if (engine.isFailed()) {
            throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Engine failed to find solution");
        }
        if (engine.isFinished()) {
            log.info("Engine finished");
        }

        String solution = engine.getSolutionNode().getSolution();

        printResults(engine, searchStrategy, solution, startTime, heuristic, totalTime);

        return engine.getSolutionNode().getState().getRepresentation();
    }

    private void verifyParams(final SearchStrategy searchStrategy, final Integer heuristic, Integer iterativeDepth) {

        if ((searchStrategy == SearchStrategy.ASTAR || searchStrategy == SearchStrategy.GREEDY)
                &&  (heuristic != HeuristicEnum.FIRST.getValue() && heuristic != HeuristicEnum.SECOND.getValue())) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "A Star and GREEDY required a valid heuristic param, " +
                    "heuristic ∈ {1, 2}");
        } else if (searchStrategy == SearchStrategy.IDDFS && iterativeDepth <= 0) {
                throw new RequestException(HttpStatus.BAD_REQUEST, "IDDFS required an optional iterativeDepth param greater " +
                        "than zero");
        }
    }

    private GPSEngine generateGPSEngine(final Problem problem, final SearchStrategy searchStrategy,
                                        final Integer heuristic, final Integer iterativeDepth) {
        GPSEngine engine;

        if (searchStrategy == SearchStrategy.ASTAR || searchStrategy == SearchStrategy.GREEDY) {
            engine = new GPSEngine(problem, searchStrategy, new Ohh1Heuristic(heuristic));
        } else if (searchStrategy == SearchStrategy.IDDFS) {
            engine = new GPSEngine(problem, searchStrategy, null);
            engine.setIterativeDepth(iterativeDepth);
        } else {
            engine = new GPSEngine(problem, searchStrategy, null);
        }

        return engine;
    }

    private void printResults(GPSEngine engine, SearchStrategy searchStrategy, String solution, long startTime, Integer heuristic, long totalTime ){
        System.out.println();
        System.out.println("Solution: \n\n" + solution);
        System.out.println("Strategy: " + engine.getStrategy());

        if (searchStrategy == SearchStrategy.GREEDY || searchStrategy == SearchStrategy.ASTAR){
            System.out.println("Heuristic: " +  heuristic);
        }
        else if (searchStrategy == SearchStrategy.IDDFS){
            System.out.println("Iterative depth: " + engine.getIterativeDepth());
        }

        System.out.println(("Solution node cost: " + engine.getSolutionNode().getCost()));
        System.out.println("Explosion counter: "  + engine.getExplosionCounter());
        System.out.println("Solution node depth: " + engine.getSolutionNode().getDepth());
        System.out.println("Analize states: " + engine.getAnalyzedStates());
        System.out.println("Find solution: " + engine.isFinished());
        System.out.println("Execution time: " + totalTime + " ns");
    }
}
