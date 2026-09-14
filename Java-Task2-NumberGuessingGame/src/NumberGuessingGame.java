import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int round = 1;

        while (true) {

            int secretNumber = random.nextInt(100) + 1;

            int maxAttempts = 7;
            int attempts = 0;
            boolean correct = false;

            System.out.println("\n==============================");
            System.out.println("     NUMBER GUESSING GAME");
            System.out.println("==============================");

            System.out.println("Round " + round);
            System.out.println("Guess a number between 1 and 100.");
            System.out.println("You have " + maxAttempts + " attempts.");

            while (attempts < maxAttempts) {

                System.out.print("\nEnter your guess: ");

                int guess = scanner.nextInt();

                attempts++;

                System.out.println(
                        "Attempt: " + attempts + "/" + maxAttempts
                );

                if (guess > secretNumber) {

                    System.out.println("Too High!");

                } else if (guess < secretNumber) {

                    System.out.println("Too Low!");

                } else {

                    System.out.println("Correct!");
                    System.out.println(
                            "Round " + round +
                                    " — guessed in " + attempts +
                                    " attempts."
                    );

                    correct = true;
                    break;
                }
            }

            if (!correct) {

                System.out.println("\nYou Lost!");

                System.out.println(
                        "The correct number was: " + secretNumber
                );
            }

            System.out.print("\nPlay Again? (yes/no): ");

            String answer = scanner.next();

            if (answer.equalsIgnoreCase("no")) {

                System.out.println("\nThanks for playing!");
                break;
            }

            round++;
        }

        scanner.close();
    }
}