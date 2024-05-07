package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends Game {
	private SpriteBatch batch;
	private Texture img;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		this.setScreen(new GameScreen());  // Cambia a la pantalla GameScreen al iniciar
	}

	@Override
	public void render() {
		super.render();  // Importante para delegar el renderizado a la pantalla activa
	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}
}
