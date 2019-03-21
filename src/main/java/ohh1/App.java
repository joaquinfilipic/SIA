package ohh1;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		
		// TODO: Delete this code
//		try {
//			PrintStream fileOut = new PrintStream("src/main/resources/console.txt");
//			System.setOut(fileOut);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}