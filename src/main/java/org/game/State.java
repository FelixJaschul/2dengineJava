package org.game;

import io.github.libsdl4j.api.render.SDL_Renderer;
import io.github.libsdl4j.api.video.SDL_Window;

public class State
{
    private SDL_Window window;
    private SDL_Renderer renderer;
    private int[][] grid;
    private boolean running;
    private final Mouse mouse = new Mouse();

    public static class Mouse
    {
        private int x, y;

        public int getX() { return x; }
        public void setX(int x) { this.x = x; }

        public int getY() { return y; }
        public void setY(int y) { this.y = y; }
    }

    public Mouse getMouse() { return mouse; }

    public SDL_Window getWindow() { return window; }
    public void setWindow(SDL_Window window) { this.window = window; }

    public SDL_Renderer getRenderer() { return renderer; }
    public void setRenderer(SDL_Renderer renderer) { this.renderer = renderer; }

    public int[][] getGrid() { return grid; }
    public void setGrid(int[][] grid) { this.grid = grid; }

    public boolean isRunning() { return running; }
    public void setRunning(boolean running) { this.running = running; }
}
