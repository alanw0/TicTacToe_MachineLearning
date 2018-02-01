package com.abqappthu.ttt;

import android.widget.TextView;

public class GameController {
    public Player playerO = null;
    public Player playerX = null;
    public Player[] players = new Player[2];

    public GameState gameState = new GameState();
    public GameDisplayer gameDisplayer = null;
    public int oWins = 0;
    public int xWins = 0;
    private int draws = 0;
    public int mlPlayerIndex = -1;
    public TextView oWinsView = null;
    public TextView xWinsView = null;
    public TextView drawsView = null;
    public TextView mbView = null;
    public TextView mwView = null;
    public TextView OPlayerTypeView = null;
    public TextView XPlayerTypeView = null;

    public boolean isTraining = false;
    public Trainer trainer_O = null;
    public Trainer trainer_X = null;
    public GameStatesWithMoves[] fullGameStates = null;

    public int currentPlayerIndex = 0;
    private boolean humanIsPlaying = false;
    public int playerToGoFirstIndex = 0;

    public GameController() {
        initializeRandomPlayers();
        fullGameStates = new GameStatesWithMoves[2];
        fullGameStates[0] = new GameStatesWithMoves(players[0].getXorO());
        fullGameStates[1] = new GameStatesWithMoves(players[1].getXorO());
        currentPlayerIndex = 0;
    }

    private void initializeRandomPlayers() {
        setPlayers(new PlayerRandom(Value.O), "RND", new PlayerRandom(Value.X), "RND");
    }

    public void setPlayers(Player playerO, String labelO, Player playerX, String labelX) {
        this.playerO = playerO;
        this.playerX = playerX;
        players[0] = this.playerO;
        players[1] = this.playerX;
        setPlayerLabel(OPlayerTypeView, labelO);
        setPlayerLabel(XPlayerTypeView, labelX);
        if ((playerO == null || playerX == null) && !humanIsPlaying) {
            setHumanIsPlaying(true);
            resetWinCounts();
        }
        if (playerO != null) {
            mlPlayerIndex = 0;
        }
        if (playerX != null) {
            mlPlayerIndex = 1;
        }
    }

    public void setPlayerLabel(final TextView tv, final String label) {
        if (tv != null) {
            tv.post(new Runnable() {
                @Override
                public void run() {
                    tv.setText(label);
                }
            });
        }
    }

    private void setHumanIsPlaying(boolean flag) {
        if (humanIsPlaying == flag) {
            return;
        }
        humanIsPlaying = flag;
    }

    public int getMLPlayerIndex() { return mlPlayerIndex; }

    public void storeGameState(int playerIndex, GameState gameState, int position) {
        if (isTraining &&
            ((playerIndex==0 && trainer_O != null) || (playerIndex==1 && trainer_X != null))) {
            fullGameStates[playerIndex].addStateWithMove(gameState, position);
        }
    }

    public void setGameDisplayer(GameDisplayer gameDisplayer) {
        this.gameDisplayer = gameDisplayer;
    }
    public void setOWinsView(TextView tv) {oWinsView = tv;}
    public void setXWinsView(TextView tv) {xWinsView = tv;}
    public void setDrawsView(TextView tv) {drawsView = tv;}
    public void setMBView(TextView tv) {mbView = tv;}
    public void setMWView(TextView tv) {mwView = tv;}

    public void setOPlayerTypeView(TextView tv) {OPlayerTypeView = tv;}
    public void setXPlayerTypeView(TextView tv) {XPlayerTypeView = tv;}

    public void setTrainers(Trainer trainer_O, Trainer trainer_X) {
        this.trainer_O = trainer_O;
        this.trainer_X = trainer_X;
        if (trainer_O != null || trainer_X != null) {
            this.isTraining = true;
        }
        else {
            this.isTraining = false;
        }
    }

    public void playGame(int humanPos) {
        while(!gameState.isGameOver()) {
            int pos = -1;
            if (players[currentPlayerIndex] != null) {
                pos = players[currentPlayerIndex].getNextPosition(gameState);
            }
            else if (humanIsPlaying && humanPos == -1) {
                return;
            }
            else {
                pos = humanPos;
            }
            Value XorO = getPlayerXorO(currentPlayerIndex);
            storeGameState(currentPlayerIndex, gameState, pos);
            gameState.setValue(XorO, pos);
            currentPlayerIndex = currentPlayerIndex==0 ? 1 : 0;
            humanPos = -1;

            if (humanIsPlaying && gameDisplayer != null) {
                gameDisplayer.display(gameState);
            }
        }
        Value winner = gameState.whoWon();
        if (winner == Value.sp) {
            ++draws;
        }
        if (winner == Value.O) {
            ++oWins;
        }
        if (winner == Value.X) {
            ++xWins;
        }

        if (isTraining) {
            storeGameState(0, gameState, -1);
            storeGameState(1, gameState, -1);
            giveStatesToTrainer();
        }
        playerToGoFirstIndex = playerToGoFirstIndex==0 ? 1 : 0;
        currentPlayerIndex = playerToGoFirstIndex;

        fullGameStates[0].clear();
        fullGameStates[1].clear();

        if (humanIsPlaying) {
            showWinStats();
        }
    }

    private boolean isPlayer(Player player, Value XorO) {
        if (player != null && player.getXorO()==XorO) {
            return true;
        }
        return false;
    }

    private int getPlayerIndex(Value XorO) {
        if (isPlayer(players[0],XorO)) {
            return 0;
        }
        if (isPlayer(players[1],XorO)) {
            return 1;
        }
        return -1;
    }

    Value getPlayerXorO(int playerIndex) {
        if (players[playerIndex] == null) {
            int otherPlayer = 1 - playerIndex;
            return players[otherPlayer].getXorO()==Value.X ? Value.O : Value.X;
        }
        return players[playerIndex].getXorO();
    }

    private void giveStatesToTrainer() {
        if (isTraining) {
            if (trainer_X!=null && !trainer_X.doneTraining()) {
                int playerIndex = getPlayerIndex(Value.X);
                trainer_X.addGame(fullGameStates[playerIndex]);
            }
            if (trainer_O!=null && !trainer_O.doneTraining()) {
                int playerIndex = getPlayerIndex(Value.O);
                trainer_O.addGame(fullGameStates[playerIndex]);
            }
            if ((trainer_X==null || trainer_X.doneTraining()) && (trainer_O==null || trainer_O.doneTraining())) {
                isTraining = false;
            }
        }
    }

    public void turnOffHumanPlayer() {
        if (humanIsPlaying) {
            int humanPlayerIndex = players[0] == null ? 0 : 1;
            Value XorO = getPlayerXorO(humanPlayerIndex);
            players[humanPlayerIndex] = new PlayerRandom(XorO);
            if (XorO == Value.O) {
                playerO = players[humanPlayerIndex];
            }
            else {
                playerX = players[humanPlayerIndex];
            }
            humanIsPlaying = false;
            resetWinCounts();
        }
    }

    public int playGames(int numGames) {
        turnOffHumanPlayer();
        int num = numGames;
        int numGamesPlayed = 0;
        for(int i=0; i<num; ++i) {
            gameState.clear();
            playGame(-1);
            ++numGamesPlayed;
            if ((trainer_X==null || trainer_X.doneTraining()) && (trainer_O==null || trainer_O.doneTraining())) {
                break;
            }
        }
        if (gameDisplayer != null && humanIsPlaying) {
            gameDisplayer.display(gameState);
        }
        showWinStats();
        return numGamesPlayed;
    }

    private void resetWinCounts() {
        oWins = 0;
        xWins = 0;
        draws = 0;
        showWinStats();
    }

    private void showWinStats() {
        showStat(oWinsView, oWins);
        showStat(xWinsView, xWins);
        showStat(drawsView, draws);
    }

    private void showStat(final TextView tv, final int stat) {
        if (tv != null) {
            tv.post(new Runnable() {
                @Override
                public void run() {
                    tv.setText(Integer.toString(stat));
                }
            });
        }
    }
}
