package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSockets;

class GameScreen implements Screen {
    private WebSocket socket;
    private String address = "localhost";
    private int port = 8888;
    private float lastSendTime = 0f;
    private static final float SEND_INTERVAL = 1.0f; // Send data every 1 second
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private String message = "Enviando información";

    public GameScreen() {
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            address = "10.0.2.2";
        }
        socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(address, port));
        socket.setSendGracefully(false);
        socket.addListener(new MyWSListener());
        socket.connect();
        socket.send("Enviar dades");

        // Create SpriteBatch and BitmapFont
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2); // Scale the font up for visibility
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Send data at regular intervals
        lastSendTime += delta;
        if (lastSendTime >= SEND_INTERVAL) {
            lastSendTime -= SEND_INTERVAL;
            socket.send("Enviar dades");
        }

        // Render text
        spriteBatch.begin();
        font.draw(spriteBatch, message, 100, 150); // Change position as needed
        font.getData().setScale(7); // Aumentar la escala a 3 para hacerlo más grande

        spriteBatch.end();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
        socket.close(); // Don't forget to close the socket
    }

    class MyWSListener implements WebSocketListener {
        @Override
        public boolean onOpen(WebSocket webSocket) {
            System.out.println("Opening...");
            return false;
        }

        @Override
        public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
            System.out.println("Closing...");
            return false;
        }

        @Override
        public boolean onMessage(WebSocket webSocket, String packet) {
            System.out.println("Message: " + packet);
            return false;
        }

        @Override
        public boolean onMessage(WebSocket webSocket, byte[] packet) {
            System.out.println("Message (byte[]): " + new String(packet));
            return false;
        }

        @Override
        public boolean onError(WebSocket webSocket, Throwable error) {
            System.out.println("ERROR:" + error.toString());
            return false;
        }
    }
}
