package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Program3 extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	ShapeRenderer shapeRenderer;
	int radius = 0;
	int x = 0;
	int y = 0;
	int score = 0;
	int remainingCircles = 4;
	ArrayList<ArrayList<Integer>> targets = new ArrayList<ArrayList<Integer>>();
	ArrayList<ArrayList<Integer>> obstacles = new ArrayList<ArrayList<Integer>>();

	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		Random random = new Random();
		for (int i = 0; i < 50; i++) {
			ArrayList<Integer> array = new ArrayList<Integer>();
			array.add(random.nextInt(Gdx.graphics.getWidth()));
			array.add(random.nextInt(Gdx.graphics.getHeight()));
			targets.add(array);
		}
		for (int i = 0; i < 20; i++) {
			ArrayList<Integer> array = new ArrayList<Integer>();
			array.add(random.nextInt(Gdx.graphics.getWidth()));
			array.add(random.nextInt(Gdx.graphics.getHeight()));
			obstacles.add(array);
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		Gdx.gl.glLineWidth(32);
		if(Gdx.input.isTouched()){
			if(x == 0 && y == 0){
				x = Gdx.input.getX();
				y = Gdx.graphics.getHeight() - Gdx.input.getY();
			}
			radius += 1;
			shapeRenderer.circle(x, y, radius);
		} else {
			radius = 0;
			x = 0;
			y = 0;
			remainingCircles--;
		}
		for(ArrayList<Integer> list : targets){
			int diffX = x - list.get(0);
			int diffy = y - (Gdx.graphics.getHeight() - list.get(1));
			
			shapeRenderer.circle(list.get(0), Gdx.graphics.getHeight() - list.get(1), 5);
		}
		shapeRenderer.setColor(Color.RED);
		for(ArrayList<Integer> list : obstacles){
			shapeRenderer.circle(list.get(0), Gdx.graphics.getHeight() - list.get(1), 5);
		}
		shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
