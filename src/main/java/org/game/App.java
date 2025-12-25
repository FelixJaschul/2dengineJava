package org.game;

import io.github.libsdl4j.api.event.*;
import io.github.libsdl4j.api.render.*;
import io.github.libsdl4j.api.video.*;
import io.github.libsdl4j.api.rect.*;
import com.sun.jna.ptr.IntByReference;

import static io.github.libsdl4j.api.Sdl.*;
import static io.github.libsdl4j.api.event.SDL_EventType.*;
import static io.github.libsdl4j.api.event.SdlEvents.*;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.*;
import static io.github.libsdl4j.api.mouse.SdlMouse.*;
import static io.github.libsdl4j.api.render.SDL_RendererFlags.*;
import static io.github.libsdl4j.api.render.SdlRender.*;
import static io.github.libsdl4j.api.timer.SdlTimer.*;
import static io.github.libsdl4j.api.video.SDL_WindowFlags.*;
import static io.github.libsdl4j.api.video.SdlVideo.*;
import static io.github.libsdl4j.api.video.SdlVideoConst.*;

public class App
{
    State state = new State();
    final int WIDTH      = 80;
    final int HEIGHT     = 40;
    final int CELL_SIZE  = 40;

    void init()
    {
        state.setGrid(new int[HEIGHT][WIDTH]);
        state.setWindow(SDL_CreateWindow("ENGINE", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, 800, 600, SDL_WINDOW_SHOWN));
        state.setRenderer(SDL_CreateRenderer(state.getWindow(), -1, SDL_RENDERER_ACCELERATED));
        state.setRunning(true);

        int[][] grid = state.getGrid();
        for (int x = 0;  x <= 15; x++) grid[3][x]   = 1;
        for (int y = 3;  y <= 7;  y++) grid[y][15]  = 1;
        for (int x = 15; x >= 4;  x--) grid[7][x]   = 1;
        for (int y = 7;  y <= 11; y++) grid[y][4]   = 1;
        for (int x = 4;  x <= 19; x++) grid[11][x]  = 1;
        state.setGrid(grid);
    }

    void deinit()
    {
        SDL_DestroyRenderer(state.getRenderer());
        SDL_DestroyWindow(state.getWindow());
        SDL_Quit();
    }

    void frame()
    {
        IntByReference mx = new IntByReference();
        IntByReference my = new IntByReference();
        SDL_GetMouseState(mx, my);
        state.getMouse().setX(mx.getValue() / CELL_SIZE);
        state.getMouse().setY(my.getValue() / CELL_SIZE);
    }

    void render()
    {
        for (int y = 0; y < HEIGHT; y++)
        {
            for (int x = 0; x < WIDTH; x++)
            {
                SDL_Rect cell = new SDL_Rect();
                cell.x = x * CELL_SIZE;
                cell.y = y * CELL_SIZE;
                cell.w = CELL_SIZE;
                cell.h = CELL_SIZE;

                if ((x + y) % 2 == 1)
                {   // Background
                    SDL_SetRenderDrawColor(state.getRenderer(), (byte) 20, (byte) 20, (byte) 20, (byte) 255);
                    SDL_RenderFillRect(state.getRenderer(), cell);
                } else
                {   // Background
                    SDL_SetRenderDrawColor(state.getRenderer(), (byte) 0, (byte) 0, (byte) 0, (byte) 255);
                    SDL_RenderFillRect(state.getRenderer(), cell);
                }

                if (x == state.getMouse().getX() && y == state.getMouse().getY())
                {   // Hover effect
                    SDL_SetRenderDrawColor(state.getRenderer(), (byte) 0, (byte) 255, (byte) 0, (byte) 255);
                    SDL_RenderDrawRect(state.getRenderer(), cell);
                }

                if (state.getGrid()[y][x] == 1)
                {   // Path cell
                    SDL_SetRenderDrawColor(state.getRenderer(), (byte) 180, (byte) 150, (byte) 100, (byte) 255);
                    SDL_RenderFillRect(state.getRenderer(), cell);
                    // Darker outline
                    SDL_SetRenderDrawColor(state.getRenderer(), (byte) 100, (byte) 80, (byte) 50, (byte) 255);
                    SDL_RenderDrawRect(state.getRenderer(), cell);
                }
                if (state.getGrid()[y][x] == 2)
                {   // Red cell
                    SDL_SetRenderDrawColor(state.getRenderer(), (byte) 255, (byte) 0, (byte) 0, (byte) 255);
                    SDL_RenderDrawRect(state.getRenderer(), cell);
                }
            }
        }
    }

    void handle_events()
    {
        SDL_Event ev = new SDL_Event();
        while (SDL_PollEvent(ev) != 0)
        {
            switch (ev.type)
            {
                case SDL_QUIT:
                    state.setRunning(false);
                    break;

                case SDL_MOUSEBUTTONDOWN:
                    if (state.getGrid()[state.getMouse().getY()][state.getMouse().getX()] != 1)
                    {
                        state.getGrid()[state.getMouse().getY()][state.getMouse().getX()] = 2;
                    }
                    break;

                case SDL_KEYDOWN:
                    if (ev.key.keysym.sym == SDLK_ESCAPE) state.setRunning(false);
                    break;
            }
        }
    }

    void run()
    {
        init();
        while (state.isRunning())
        {
            handle_events();

            render();
            SDL_RenderPresent(state.getRenderer());
            frame();
        }
        deinit();
    }

    static void main()
    {
        App app = new App();
        app.run();
    }
}
