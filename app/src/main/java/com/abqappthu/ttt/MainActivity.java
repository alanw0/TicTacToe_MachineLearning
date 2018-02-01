package com.abqappthu.ttt;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    public GameController gameController = new GameController();
    public NeuralNetTrainer nnTrainer_X = new NeuralNetTrainer(Value.X);
    public NeuralNetTrainer nnTrainer_O = new NeuralNetTrainer(Value.O);
    public PlayerNN nnPlayer_X = new PlayerNN(Value.X, nnTrainer_X.nnet);
    public PlayerNN nnPlayer_O = new PlayerNN(Value.O, nnTrainer_O.nnet);
    public StateTable stateTable_X = new StateTable();
    public TableTrainer tTrainer_X = new TableTrainer(Value.X, stateTable_X);
    public StateTable stateTable_O = new StateTable();
    public TableTrainer tTrainer_O = new TableTrainer(Value.O, stateTable_O);
    public PlayerTable tPlayer_X = new PlayerTable(Value.X, stateTable_X);
    public PlayerTable tPlayer_O = new PlayerTable(Value.O, stateTable_O);
    public String tPlayer_O_label = "Random";
    public String tPlayer_X_label = "Random";
    Integer numGamesToPlay = 10000;
    private TextView nntLabel = null;
    private TextView nntView = null;
    private TextView blkLabelView = null;
    private TextView mwLabelView = null;
    private AnimatedView aview = null;
    private Button button1 = null;
    private Button playButton = null;
    private boolean stopTraining = false;
    private long totalNumGames = 0;

    private Handler threadLauncher = new Handler();

    private void run_playGames(Integer numGames) {
        boolean finished = false;
        tPlayer_O.setDiagView(null);
        tPlayer_X.setDiagView(null);
        while(!finished) {
            totalNumGames += gameController.playGames(numGames);
            nntView.post(new Runnable() {
                @Override
                public void run() {
                    nntView.setText("#games: "+Long.toString(totalNumGames));
                }
            });
            finished = gameController.isTraining ?
                    (tTrainer_O.doneTraining() && tTrainer_X.doneTraining()) : true;
        }
    }

    private Runnable setBlkLabelView = new Runnable() {
        @Override
        public void run() {
            blkLabelView.setText(" ");
        }
    };
    private Runnable setMWLabelView = new Runnable() {
        @Override
        public void run() {
            mwLabelView.setText(" ");
        }
    };

    private class train_machine_learning_players extends AsyncTask<Integer, Integer, Integer> {
        protected Integer doInBackground(Integer... param) {
            stopTraining = false;
            tPlayer_O.setTrainingThreshold(200000);
            tPlayer_X.setTrainingThreshold(200000);
            tTrainer_O.setNumWeightsThreshold(1300000);
            tTrainer_X.setNumWeightsThreshold(1300000);
            tPlayer_O_label = "Random";
            tPlayer_X_label = "Random";
            int counter = 0;
            while(!stopTraining) {
                if (counter == 0) {
                    gameController.setPlayers(tPlayer_O, tPlayer_O_label, tPlayer_X, tPlayer_X_label);
                    gameController.setTrainers(tTrainer_O, tTrainer_X);
                }
                if (counter > 0) {
                    tTrainer_O.setNumWeightsThreshold(600000);
                    tTrainer_X.setNumWeightsThreshold(600000);
                }
                if (counter%2==1) {
                    tPlayer_X_label = "TableLookup";
                    tPlayer_X.setTrainingThreshold(10000);
                    gameController.setPlayers(new PlayerRandom(Value.O), "Random", tPlayer_X, tPlayer_X_label);
                    gameController.setTrainers(tTrainer_O, null);
                }
                if (counter > 0 && counter%2==0) {
                    tPlayer_O_label = "TableLookup";
                    tPlayer_O.setTrainingThreshold(10000);
                    gameController.setPlayers(tPlayer_O, tPlayer_O_label, new PlayerRandom(Value.X), "Random");
                    gameController.setTrainers(null, tTrainer_X);

                }
                blkLabelView.post(setBlkLabelView);
                mwLabelView.post(setMWLabelView);

                run_playGames(numGamesToPlay);
                run_commitTraining();
                if (counter > 4) {
                    stopTraining = true;
                }
                ++counter;
            }
            return 0;
        }

        protected void onPostExecute(Integer result) {
            tPlayer_O.setTrainingThreshold(0);
            tPlayer_O_label = "TableLookup";
            tPlayer_X.setTrainingThreshold(0);
            tPlayer_X_label = "TableLookup";
            //nntLabel.setText("Done!");
            button1.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.VISIBLE);
        }
    }

    private void run_commitTraining() {
        tTrainer_X.commitTrainingData();
        tTrainer_O.commitTrainingData();
    }

    private Runnable display_gamestate = new Runnable() {
        @Override
        public void run() {
            aview.setGameState(gameController.gameState);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nntLabel = (TextView)findViewById(R.id.nntLabel);
        nntView = (TextView)findViewById(R.id.nnt);
        aview = (AnimatedView)findViewById(R.id.aview);
        blkLabelView = (TextView)findViewById(R.id.mbLabel);
        mwLabelView = (TextView)findViewById(R.id.mwLabel);
        button1 = (Button)findViewById(R.id.button1);
        playButton = (Button)findViewById(R.id.playButton);

        gameController.setOWinsView((TextView)findViewById(R.id.oWins));
        gameController.setXWinsView((TextView)findViewById(R.id.xWins));
        gameController.setDrawsView((TextView)findViewById(R.id.draws));
        gameController.setMBView((TextView)findViewById(R.id.mb));
        gameController.setMWView((TextView)findViewById(R.id.mw));
        gameController.setOPlayerTypeView((TextView)findViewById(R.id.OPlayerType));
        gameController.setXPlayerTypeView((TextView)findViewById(R.id.XPlayerType));

        gameController.setGameDisplayer(new GameDisplayer() {
            @Override
            public void display(GameState gameState) {
                threadLauncher.post(display_gamestate);
            }
        });

        setTrainingButtonHandler();
        tPlayer_O.setTrainingThreshold(200000);
        tPlayer_X.setTrainingThreshold(200000);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playButton.setVisibility(View.INVISIBLE);
                button1.setVisibility(View.INVISIBLE);
                int machinePlayerIndex = gameController.getMLPlayerIndex();

                if (machinePlayerIndex == 0) {
                    gameController.setPlayers(tPlayer_O, tPlayer_O_label, null, "You");
                    tPlayer_O.setDiagView(blkLabelView);
                }
                else {
                    gameController.setPlayers(null, "You", tPlayer_X, tPlayer_X_label);
                     tPlayer_X.setDiagView(blkLabelView);
                }

                aview.setTouchHandler(new TouchHandler() {
                    @Override
                    public void handleTouch(int whichTTTsquare) {
                        if (gameController.gameState.values[whichTTTsquare] == Value.sp) {
                            gameController.playGame(whichTTTsquare);
                        }
                        if (gameController.gameState.isGameOver()) {
                            playButton.setVisibility(View.VISIBLE);
                            button1.setVisibility(View.VISIBLE);
                            String result = "It was a draw";
                            if (gameController.gameState.isThereAWinner()) {
                                if (gameController.gameState.whoWon() == Value.O) {
                                    result = "O won the game";
                                }
                                else {
                                    result = "X won the game";
                                }
                            }
                            mwLabelView.setText(result);
                        }
                        else {
                            mwLabelView.setText("It's your move...");
                        }
                    }
                });

                gameController.gameState.clear();
                aview.setGameState(gameController.gameState);
                gameController.playGame(-1);
                mwLabelView.setText("It's your move...");
            }
        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void setTrainingButtonHandler() {
        totalNumGames = 0;
        button1.post(new Runnable() {
            @Override
            public void run() {
                button1.setVisibility(View.VISIBLE);
                button1.setText("Train AI Players");
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playButton.setVisibility(View.INVISIBLE);
                        button1.setVisibility(View.INVISIBLE);
                        blkLabelView.post(setBlkLabelView);
                        mwLabelView.post(setMWLabelView);
                        new train_machine_learning_players().execute();
                    }
                });
            }
        });
    }

    private void setStopTrainingButtonHandler() {
        button1.post(new Runnable() {
            @Override
            public void run() {
                button1.setVisibility(View.VISIBLE);
                button1.setText("Stop");
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stopTraining = true;
                    }
                });
            }
        });
    }
}
