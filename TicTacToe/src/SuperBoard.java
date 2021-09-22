public class SuperBoard {
    static int player, computer;
    private static final Board[][] board = new Board[3][3];

    static void printGame(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Board.printBoard();
            }System.out.println();
        }
    }
}
