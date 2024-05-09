package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

class GameScreen implements Screen {
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private Stage stage;
    private String message = "Presiona el botón";

    public GameScreen() {
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(3); // Increase the font scale for better visibility

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.WHITE;

        TextButton button = new TextButton("Obtener IP", textButtonStyle);

        // Calculate button position and size
        float buttonWidth = 300;
        float buttonHeight = 100;
        float buttonX = (Gdx.graphics.getWidth() - buttonWidth) / 2;
        float buttonY = (Gdx.graphics.getHeight() - buttonHeight) / 2;

        button.setPosition(buttonX, buttonY);
        button.setSize(buttonWidth, buttonHeight);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                HttpRequest httpRequest = new HttpRequest(Net.HttpMethods.GET);
                httpRequest.setUrl("https://api.ipify.org?format=json");

                Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(HttpResponse httpResponse) {
                        JsonReader jsonReader = new JsonReader();
                        JsonValue json = jsonReader.parse(httpResponse.getResultAsString());
                        message = "Tu IP es: " + json.getString("ip");
                    }

                    @Override
                    public void failed(Throwable t) {
                        message = "Error: " + t.getMessage();
                    }

                    @Override
                    public void cancelled() {
                        message = "Llamada HTTP cancelada";
                    }
                });
            }
        });

        stage.addActor(button);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        font.draw(spriteBatch, message, 100, 150); // Adjust as needed to center or place the text
        spriteBatch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
        stage.dispose();
    }
}
