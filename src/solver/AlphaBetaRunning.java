package solver;

import java.util.Random;

public class AlphaBetaRunning {

    public static void main(String[] args) {
        Solver solver = new Solver();
        Random rand = new Random();

        solver.setBoard();
        solver.displayBoard();

        System.out.println("Who's gonna move first? (1)Computer (2)User: ");

        int choice = solver.scan.nextInt();

        if(choice == 1){
            Points p = new Points(rand.nextInt(3),rand.nextInt(3));
            solver.placeAMove(p,"X");
            solver.displayBoard();
        }

        while (! solver.isGameOver()){
            System.out.println("Your move: ");
            Points userMove = new Points(solver.scan.nextInt(),solver.scan.nextInt());
            solver.placeAMove(userMove,"O");
            solver.displayBoard();

            if(solver.isGameOver())
                break;

            solver.alphaBetaMinimax(Integer.MIN_VALUE,Integer.MAX_VALUE,0,"X");

            solver.placeAMove(solver.getBestMove(),"X");
            solver.displayBoard();

        }
        if (solver.isXWin()) {
            System.out.println("Unfortunately, you lost!");
        } else if (solver.isOWin()) {
            System.out.println("You win!");
        } else {
            System.out.println("It's a draw!");
        }

    }
}
