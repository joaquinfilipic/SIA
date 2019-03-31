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

        verifyParams(searchStrategy, heuristic, iterativeDepth);

        log.info("Input correctly scanned");

        Problem problem = new Ohh1Problem(initialState);
        GPSEngine engine = generateGPSEngine(problem, searchStrategy, heuristic, iterativeDepth);

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
        System.out.println("Explosion counter with " + engine.getStrategy() + ": " + engine.getExplosionCounter());

        if (searchStrategy == SearchStrategy.GREEDY || searchStrategy == SearchStrategy.ASTAR)
            System.out.println(("Costo del nodo solución: " + engine.getSolutionNode().getCost()));

        return engine.getSolutionNode().getState().getRepresentation();
    }

    private void verifyParams(final SearchStrategy searchStrategy, final Integer heuristic,
                              final Integer iterativeDepth) {
        if ((searchStrategy == SearchStrategy.ASTAR || searchStrategy == SearchStrategy.GREEDY)
                && (heuristic == null || (heuristic != HeuristicEnum.FIRST.getValue() && heuristic != HeuristicEnum.SECOND.getValue()))) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "A Star and GREEDY required a valid heuristic param, " +
                    "heuristic ∈ {1, 2}");
        } else if (searchStrategy == SearchStrategy.IDDFS && (iterativeDepth == null || iterativeDepth <= 0)) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "IDDFS required a valid iterativeDepth param greater " +
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
}
