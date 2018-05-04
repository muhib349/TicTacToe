package solver;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solver {
    private List<Points> availablePoints;
    public Scanner scan = new Scanner(System.in);
    private String[][] board = new String[3][3];
    private List<PointsAndScores> rootsChildrenScore = new ArrayList<PointsAndScores>();


    public int evaluateBoard(){
        int score =0;

        //check all rows;
        for(int i=0 ;i<3 ;i++){
            int blank =0;
            int X = 0;
            int O = 0;
            for(int j=0 ;j<3 ;j++){
                if(board[i][j] == "-")
                    blank++;
                else if(board[i][j] == "X")
                    X++;
                else
                    O++;
            }
            score += getScore(X,O);
        }


        //check all columns;
        for(int j=0;j<3;j++){
            int blank =0;
            int X = 0;
            int O = 0;
            for(int i=0;i<3;i++){
                if(board[i][j] == "-")
                    blank++;
                else if(board[i][j] == "X")
                    X++;
                else
                    O++;
            }
            score += getScore(X,O);
        }

        int blank=0;
        int X=0;
        int O=0;
        //first diagonal
        for(int i=0, j=0 ;i<3 ;++i,++j){
            if(board[i][j] == "X")
                X++;
            else if (board[i][j] == "O")
                O++;
            else
                blank++;
        }

        score += getScore(X,O);

        blank =0;
        X=0;
        O=0;

        //second diagonal
        for(int i=2, j=0 ;i>-1 ;--i,++j){
            if(board[i][j] == "X")
                X++;
            else if (board[i][j] == "O")
                O++;
            else
                blank++;
        }

        score += getScore(X,O);


        return score;
    }

    private int getScore(int X, int O){
        int value;
        if (X == 3) {
            value = 100;
        } else if (X == 2 && O == 0) {
            value = 10;
        } else if (X == 1 && O == 0) {
            value = 1;
        } else if (O == 3) {
            value = -100;
        } else if (O == 2 && X == 0) {
            value = -10;
        } else if (O == 1 && X == 0) {
            value = -1;
        } else {
            value = 0;
        }
        return value;
    }

    public boolean isGameOver(){
        return (isXWin() || isOWin() || getAvailableStates().isEmpty());
    }

    public boolean isXWin(){

        //diagonal check
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == "X") || (board[0][2] == board[1][1] && board[0][2] == board[2][0] &&  board[0][2] == "X")) {
            return true;
        }

        //row or column check
        for (int i = 0; i < 3; ++i) {
            if (((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == "X")
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == "X"))) {
                return true;
            }
        }

     return false;
    }

    //diagonal check
    public boolean isOWin(){
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == "O") || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == "O")) {
            return true;
        }

        //row and column check
        for (int i = 0; i < 3; ++i) {
            if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == "O")
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == "O")) {
                return true;
            }
        }
        return false;
    }

    public List<Points> getAvailableStates(){
        availablePoints = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (board[i][j] == "-") {
                    availablePoints.add(new Points(i, j));
                }
            }
        }
        return availablePoints;
    }


    public int alphaBetaMinimax(int alpha, int beta,int depth,String turn){

        if(beta<=alpha){
            if(turn == "X")
                return Integer.MAX_VALUE;
            else
                return Integer.MIN_VALUE;
        }

        if(isGameOver())
            return evaluateBoard();

        List<Points> pointsAvailable = getAvailableStates();

        if(pointsAvailable.isEmpty())
            return 0;

        if(depth==0)
            rootsChildrenScore.clear();

        int maxValue = Integer.MIN_VALUE, minValue = Integer.MAX_VALUE;

        for(int i=0;i<pointsAvailable.size(); ++i){
            Points point = pointsAvailable.get(i);

            int currentScore = 0;

            if(turn == "X"){
                placeAMove(point, "X");
                currentScore = alphaBetaMinimax(alpha, beta, depth+1, "O");
                maxValue = Math.max(maxValue, currentScore);

                //Set alpha
                alpha = Math.max(currentScore, alpha);

                if(depth == 0)
                    rootsChildrenScore.add(new PointsAndScores(currentScore, point));
            }else if(turn == "O"){
                placeAMove(point, "O");
                currentScore = alphaBetaMinimax(alpha, beta, depth+1, "X");
                minValue = Math.min(minValue, currentScore);

                //Set beta
                beta = Math.min(currentScore, beta);
            }
            //reset board
            board[point.getX()][point.getY()] = "-";

            //If a pruning has been done, don't evaluate the rest of the sibling states
            if(currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE)
                break;
        }
        return turn == "X" ? maxValue : minValue;
    }

    public void placeAMove(Points point, String player) {
        board[point.getX()][point.getY()] = player;
    }

    public Points getBestMove() {
        int MAX = -100000;
        int best = -1;

        for (int i = 0; i < rootsChildrenScore.size(); ++i) {
            if (MAX < rootsChildrenScore.get(i).getScore()) {
                MAX = rootsChildrenScore.get(i).getScore();
                best = i;
            }
        }
        return rootsChildrenScore.get(best).getPoint();
    }

    public void displayBoard(){
        System.out.println();

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void setBoard() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                board[i][j] = "-";
            }
        }
    }
}
