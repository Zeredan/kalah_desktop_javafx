package com.last_battle.kalah_server.game;

import com.last_battle.kalah_server.model.GameState;
import java.util.List;

public class GameLogic {

    public interface StateCallback {
        void onStateUpdated(GameState state);
    }

    public static void makeMovePlayer1(GameState state, int holeIndex, StateCallback callback) {
        if (state.isMakingMove()) return;
        
        synchronized (state) {
            state.setMakingMove(true);
        }

        new Thread(() -> {
            try {
                executeMovePlayer1(state, holeIndex, callback);
            } finally {
                synchronized (state) {
                    state.setMakingMove(false);
                    callback.onStateUpdated(state);
                }
            }
        }).start();
    }

    public static void makeMovePlayer2(GameState state, int holeIndex, StateCallback callback) {
        if (state.isMakingMove()) return;
        
        synchronized (state) {
            state.setMakingMove(true);
        }

        new Thread(() -> {
            try {
                executeMovePlayer2(state, holeIndex, callback);
            } finally {
                synchronized (state) {
                    state.setMakingMove(false);
                    callback.onStateUpdated(state);
                }
            }
        }).start();
    }

    private static void executeMovePlayer1(GameState state, int holeIndex, StateCallback callback) {
        List<Integer> p1Holes = state.getPlayer1Holes();
        List<Integer> p2Holes = state.getPlayer2Holes();
        int holesCount = state.getHolesCount();

        if (p1Holes.get(holeIndex) == 0) return;

        int stones = p1Holes.get(holeIndex);
        p1Holes.set(holeIndex, 0);

        int currentIndex = holeIndex;
        boolean isPlayer1 = true;
        boolean isLastInKalah = false;
        boolean isLastInEmpty = false;

        for (int i = 0; i < stones; i++) {
            if (isPlayer1) {
                if (currentIndex < holesCount - 1) {
                    currentIndex++;
                    p1Holes.set(currentIndex, p1Holes.get(currentIndex) + 1);
                } else {
                    state.setPlayer1Kalah(state.getPlayer1Kalah() + 1);
                    currentIndex++;
                    isPlayer1 = false;
                }
            } else {
                if (currentIndex > 0) {
                    currentIndex--;
                    p2Holes.set(currentIndex, p2Holes.get(currentIndex) + 1);
                } else {
                    currentIndex = 0;
                    p1Holes.set(currentIndex, p1Holes.get(currentIndex) + 1);
                    isPlayer1 = true;
                }
            }

            callback.onStateUpdated(state.deepCopy());
            sleep(500);

            if (i == stones - 1) {
                isLastInKalah = (!isPlayer1 && currentIndex == holesCount);
                isLastInEmpty = (isPlayer1 && currentIndex < holesCount && p1Holes.get(currentIndex) == 1);
            }
        }

        if (isLastInEmpty && currentIndex < holesCount) {
            state.setPlayer1Kalah(state.getPlayer1Kalah() + p2Holes.get(currentIndex) + 1);
            p1Holes.set(currentIndex, 0);
            p2Holes.set(currentIndex, 0);
            callback.onStateUpdated(state.deepCopy());
        }

        state.setCurrentPlayerInd(isLastInKalah ? 1 : 2);
        callback.onStateUpdated(state.deepCopy());
        checkEnding(state, callback);
    }

    private static void executeMovePlayer2(GameState state, int holeIndex, StateCallback callback) {
        List<Integer> p1Holes = state.getPlayer1Holes();
        List<Integer> p2Holes = state.getPlayer2Holes();
        int holesCount = state.getHolesCount();

        if (p2Holes.get(holeIndex) == 0) return;

        int stones = p2Holes.get(holeIndex);
        p2Holes.set(holeIndex, 0);

        int currentIndex = holeIndex;
        boolean isPlayer1 = false;
        boolean isLastInKalah = false;
        boolean isLastInEmpty = false;

        for (int i = 0; i < stones; i++) {
            if (isPlayer1) {
                if (currentIndex < holesCount - 1) {
                    currentIndex++;
                    p1Holes.set(currentIndex, p1Holes.get(currentIndex) + 1);
                } else {
                    currentIndex = holesCount - 1;
                    p2Holes.set(currentIndex, p2Holes.get(currentIndex) + 1);
                    isPlayer1 = false;
                }
            } else {
                if (currentIndex > 0) {
                    currentIndex--;
                    p2Holes.set(currentIndex, p2Holes.get(currentIndex) + 1);
                } else {
                    currentIndex = -1;
                    state.setPlayer2Kalah(state.getPlayer2Kalah() + 1);
                    isPlayer1 = true;
                }
            }

            callback.onStateUpdated(state.deepCopy());
            sleep(500);

            if (i == stones - 1) {
                isLastInKalah = (isPlayer1 && currentIndex == -1);
                isLastInEmpty = (!isPlayer1 && currentIndex >= 0 && p2Holes.get(currentIndex) == 1);
            }
        }

        if (isLastInEmpty && currentIndex >= 0) {
            state.setPlayer2Kalah(state.getPlayer2Kalah() + p1Holes.get(currentIndex) + 1);
            p1Holes.set(currentIndex, 0);
            p2Holes.set(currentIndex, 0);
            callback.onStateUpdated(state.deepCopy());
        }

        state.setCurrentPlayerInd(isLastInKalah ? 2 : 1);
        callback.onStateUpdated(state.deepCopy());
        checkEnding(state, callback);
    }

    private static void checkEnding(GameState state, StateCallback callback) {
        List<Integer> p1Holes = state.getPlayer1Holes();
        List<Integer> p2Holes = state.getPlayer2Holes();
        int holesCount = state.getHolesCount();
        int initialStones = state.getInitialStonesCount();
        int totalStones = holesCount * initialStones * 2;
        int halfStones = totalStones / 2;

        // Досрочная победа
        if (state.getPlayer1Kalah() > halfStones) {
            state.setStatus("PLAYER1_WIN");
            callback.onStateUpdated(state);
            return;
        }
        if (state.getPlayer2Kalah() > halfStones) {
            state.setStatus("PLAYER2_WIN");
            callback.onStateUpdated(state);
            return;
        }

        // Стандартная проверка окончания
        boolean p1Empty = p1Holes.stream().allMatch(v -> v == 0);
        boolean p2Empty = p2Holes.stream().allMatch(v -> v == 0);

        if (p1Empty || p2Empty) {
            if (p1Empty) {
                int sum = p2Holes.stream().mapToInt(Integer::intValue).sum();
                state.setPlayer2Kalah(state.getPlayer2Kalah() + sum);
                p2Holes.replaceAll(v -> 0);
            } else {
                int sum = p1Holes.stream().mapToInt(Integer::intValue).sum();
                state.setPlayer1Kalah(state.getPlayer1Kalah() + sum);
                p1Holes.replaceAll(v -> 0);
            }

            if (state.getPlayer1Kalah() > state.getPlayer2Kalah()) {
                state.setStatus("PLAYER1_WIN");
            } else if (state.getPlayer1Kalah() < state.getPlayer2Kalah()) {
                state.setStatus("PLAYER2_WIN");
            } else {
                state.setStatus("DRAW");
            }
            callback.onStateUpdated(state);
        }
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}