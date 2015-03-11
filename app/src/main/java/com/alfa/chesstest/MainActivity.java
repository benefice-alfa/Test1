package com.alfa.chesstest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private Socket socket;


    private static final int SERVERPORT = 7387;
    private static final String SERVER = "xinuc.org";

    private final String[] columns = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private final String[] rows = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private HashMap<String, Integer> mapPawn;
    private GridLayout boardLayout;
    private RelativeLayout parentPawn;
    private List<BoardView> listBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boardLayout = (GridLayout) findViewById(R.id.boardLayout);
        parentPawn = (RelativeLayout) findViewById(R.id.parentPawn);
        listBoard = new ArrayList<>();
        //mapping
        mapPawn = new HashMap<>();
        mapPawn.put("b", R.mipmap.b_black);
        mapPawn.put("B", R.mipmap.b_white);
        mapPawn.put("K", R.mipmap.k_white);
        mapPawn.put("k", R.mipmap.k_black);
        mapPawn.put("Q", R.mipmap.q_white);
        mapPawn.put("q", R.mipmap.q_black);
        mapPawn.put("N", R.mipmap.n_white);
        mapPawn.put("n", R.mipmap.n_black);
        mapPawn.put("R", R.mipmap.r_white);
        mapPawn.put("r", R.mipmap.r_black);
        int size = 8;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                BoardView board = new BoardView(this, j, i);
                boardLayout.addView(board);
                listBoard.add(board);
            }
        }
        //start the connection.
        new Thread(new ClientThread()).start();
    }

    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                //start using server and port
                InetAddress serverAddr = InetAddress.getByName(SERVER);
                socket = new Socket(serverAddr, SERVERPORT);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                //getting the response
                InputStream in = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = "";

                while ((line = br.readLine()) != null) {
                    final String[] position = line.split(" ");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //setting the pawn and also placing them.
                            parentPawn.removeAllViews();
                            for (String pos : position) {
                                int imgResource = mapPawn.get(pos.charAt(0) + "");
                                int x = findColumnIndex(pos.charAt(1) + "", false);
                                int y = findColumnIndex(pos.charAt(2) + "", true);
                                BoardView board = listBoard.get((y * 8) + x);
                                int[] onScreenLocation = new int[2];
                                board.getLocationOnScreen(onScreenLocation);
                                TextView txt = new TextView(MainActivity.this);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(90, 90);
                                txt.setLayoutParams(params);
                                txt.setBackgroundResource(imgResource);
                                txt.setTranslationX(onScreenLocation[0]);
                                txt.setTranslationY(onScreenLocation[1]-162);
                                parentPawn.addView(txt);
                            }
                        }
                    });
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            // close the socket upon exiting
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //parsing response to index
    private int findColumnIndex(String name, boolean row) {
        int idx = -1;
        int size = row ? rows.length : columns.length;
        for (int i = 0; i < size; i++) {
            String column = row ? rows[i] : columns[i];
            if (name.equals(column)) {
                idx = i;
                break;
            }
        }
        return idx;
    }
}
