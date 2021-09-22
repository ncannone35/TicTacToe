import java.util.Scanner;


public class Board {
    //-1 : o  1: x
    static int player, computer;
    private static final int[][] board = new int[3][3];//initial state

    static void printBoard() {
        System.out.println("   a   b   c");
        for (int i = 0; i < 3; i++) {
            if (i > 0) {
                System.out.println("  ------------");
            }
            System.out.print((i + 1) + "  ");
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == -1) System.out.print("O ");
                else if (board[i][j] == 1) System.out.print("X ");
                else System.out.print("  ");
                if (j != 2) System.out.print("| ");
            }
            System.out.println();
        }
    }

    static boolean makeUserMove(String move, int user) {//user move (transition function)
        switch (move) {
            case "a1":
                if (board[0][0] == 0) {
                    board[0][0] = user;
                    return true;
                }
                break;
            case "a2":
                if (board[1][0] == 0) {
                    board[1][0] = user;
                    return true;
                }
                break;
            case "a3":
                if (board[2][0] == 0) {
                    board[2][0] = user;
                    return true;
                }
                break;
            case "b1":
                if (board[0][1] == 0) {
                    board[0][1] = user;
                    return true;
                }
                break;
            case "b2":
                if (board[1][1] == 0) {
                    board[1][1] = user;
                    return true;
                }
                break;
            case "b3":
                if (board[2][1] == 0) {
                    board[2][1] = user;
                    return true;
                }
                break;
            case "c1":
                if (board[0][2] == 0) {
                    board[0][2] = user;
                    return true;
                }
                break;
            case "c2":
                if (board[1][2] == 0) {
                    board[1][2] = user;
                    return true;
                }
                break;
            case "c3":
                if (board[2][2] == 0) {
                    board[2][2] = user;
                    return true;
                }
                break;
        }
        System.out.println("Illegal move, try again.");
        return false;
    }

    static boolean rowWin() {//checks for a row win
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0) {
                if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) return true;
            }
        }
        return false;
    }

    static boolean colWin() {//checks for a col win
        for (int i = 0; i < 3; i++) {
            if (board[0][i] != 0) {
                if (board[0][i] == board[1][i] && board[1][i] == board[2][i]) return true;
            }
        }
        return false;
    }

    static boolean diagWin() {//checks for a diagonal win
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) return true;
        else return board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0];
    }

    static boolean isWin() {//checks if board is a winner, GOAL STATE
        return rowWin() || colWin() || diagWin();
    }

    static boolean isTie() {//checks for ties, GOAL STATE
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) return false;
            }
        }
        return true;
    }

    static void minimax_move() {
        State best = minimax(true);
        board[best.x][best.y] = computer;
    }

    static void pruning_move(){
        State best = hminimax_prune(true, Integer.MIN_VALUE, Integer.MAX_VALUE);
        board[best.x][best.y] = computer;
    }

    static State minimax(boolean max) {
        State best = new State();
        if (isWin()) {//checks for wins
            if (max) {
                best.score = -1;
            } else best.score = 1;
            return best;
        } else if (isTie()) {//checks for ties
            best.score = 0;
            return best;
        }
        if (max) best.score = Integer.MIN_VALUE;
        else best.score = Integer.MAX_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {//loops through every state in the state space
                    if (max) board[i][j] = computer;
                    else board[i][j] = player;
                    State curr = minimax(!max);//switches MAX and MIN
                    if (max) {
                        if (curr.score > best.score) {//maximizes utility
                            best.score = curr.score;
                            best.x = i;
                            best.y = j;
                        }
                    } else {
                        if (curr.score < best.score) {//minimizes utility
                            best.score = curr.score;
                            best.x = i;
                            best.y = j;
                        }
                    }
                    board[i][j] = 0;
                }
            }
        }
        return best;
    }

    static int num_empties(){//for heuristic function
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(board[i][j] == 0) count++;
            }
        }return count;
    }

    /* heuristic fucntion
        h(x) = 1 * empty if max --> searches for fastest win
        h(x) = -1 * empty if min
     */
    static State hminimax_prune(boolean max, int alpha, int beta) {
        State best = new State();
        if (isWin()) {//checks for wins
            int empty = num_empties() + 1;//avoid heuristic adding to 0, would confuse wins with ties
            if (max) {
                best.score = -1*empty;//heuristic function (more empties the better/faster win)
            } else best.score = empty;
            return best;
        } else if (isTie()) {//checks for ties
            best.score = 0;
            return best;
        }
        if (max) best.score = Integer.MIN_VALUE;
        else best.score = Integer.MAX_VALUE;

        breakpoint:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {//loops through every state in the state space
                    if (max) board[i][j] = computer;
                    else board[i][j] = player;
                    State curr = hminimax_prune(!max, alpha, beta);//switches MAX and MIN
                    if (max) {
                        if (curr.score > best.score) {//maximizes utility
                            best.score = curr.score;
                            best.x = i;
                            best.y = j;
                        }
                        alpha = Math.max(alpha, curr.score);
                    } else {
                        if (curr.score < best.score) {//minimizes utility
                            best.score = curr.score;
                            best.x = i;
                            best.y = j;
                        }
                        beta = Math.min(beta, curr.score);
                    }
                    if(beta <= alpha) {
                        board[i][j] = 0;
                        break breakpoint;
                    }
                    board[i][j] = 0;
                }
            }
        }
        return best;
    }

     static void tic_tac_toe(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter '1' to play against the MINIMAX; Enter '2' to play against an H-MINIMAX with alpha beta pruning:");
        int game = scan.nextInt();
        boolean min;
        while(game != 1 && game != 2) {
            System.out.println("Invalid input, please try again:");
            game = scan.nextInt();
        }
         min = game == 1;
        System.out.println("Would you like to play 'X' or 'O' | 'X' starts:");
        String user;
        user = scan.nextLine();
        user = scan.nextLine();
        while (!user.equals("X") && !user.equals("O")) {
            System.out.println("Invalid input, please try again:");
            user = scan.nextLine();
        }
        if (user.equals("X")) {
            player = 1;
            computer = -1;
        } else {
            player = -1;
            computer = 1;
            if(min) minimax_move();
            else pruning_move();
        }
        printBoard();
        while (true) {
            boolean not_legal = false;
            System.out.println("Make your move, loser:");
            String input;
            while (!not_legal) {//runs til a legal move is found
                input = scan.nextLine();
                not_legal = makeUserMove(input, player);
            }
            printBoard();
            if (isTie()) {
                System.out.println("Tie!");
                return;
            } else if (isWin()) {
                System.out.println("Congratulations!  You Win");
                return;
            }
            System.out.println();
            System.out.println("Computing in progress...");
            if(min) minimax_move();
            else pruning_move();
            printBoard();
            if (isTie()) {
                System.out.println("Tie!");
                return;
            } else if (isWin()) {
                if(min) System.out.println("You really thought you could beat MINIMAX?");
                else System.out.println("You really thought you could beat H-MINIMAX with alpha beta pruning?");
                return;
            }
        }
    }
}
