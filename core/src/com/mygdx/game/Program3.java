package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Program3 extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	ShapeRenderer shapeRenderer;
	int radius = 0;
	int x = 0;
	int y = 0;
	boolean touched = false;
	int score = 0;
	int remainingCircles = 4;
	boolean failed = false;
	ArrayList<ArrayList<Integer>> targets = new ArrayList<ArrayList<Integer>>();
	ArrayList<ArrayList<Integer>> obstacles = new ArrayList<ArrayList<Integer>>();
	private BitmapFont font;
	private boolean ignore;

	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		font = new BitmapFont();
		generateTargets();
	}

	public void generateTargets(){
		targets.clear();
		obstacles.clear();
		Random random = new Random();
		for (int i = 0; i < 50; i++) {
			ArrayList<Integer> array = new ArrayList<Integer>();
			array.add(random.nextInt(Gdx.graphics.getWidth()));
			array.add(random.nextInt(Gdx.graphics.getHeight()-50));
			targets.add(array);
		}
		for (int i = 0; i < 20; i++) {
			ArrayList<Integer> array = new ArrayList<Integer>();
			array.add(random.nextInt(Gdx.graphics.getWidth()));
			array.add(random.nextInt(Gdx.graphics.getHeight()-50));
			obstacles.add(array);
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		font.setColor(Color.BLACK);
		font.getData().setScale(5, 5);
		font.draw(batch, "Score: " + score, 50, 75);
		font.draw(batch, "Circles: " + remainingCircles, 1750, 75);
		batch.end();
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		Gdx.gl.glLineWidth(32);
		if(remainingCircles == 0){
			failed = true;
		}
		if(!failed){
			if(Gdx.input.isTouched()){
				renderPlayerCircle();
			} else {
				resetPlayerCircle();
			}
			renderTargets();
			renderObstacles();
		} else{
			renderEndScreen();
		}
		shapeRenderer.end();
	}

	public void renderPlayerCircle(){
		if(!ignore){
			if(touched == false){
				x = Gdx.input.getX();
				y = Gdx.graphics.getHeight() - Gdx.input.getY();
				touched = true;
			}
			radius += 1;
			shapeRenderer.circle(x, y, radius);
		}
	}

	public void resetPlayerCircle(){
		radius = 0;
		x = 0;
		y = 0;
		if(!ignore){
			if(touched == true){
				remainingCircles--;
			}
		} else{
			ignore = false;
		}
		touched = false;
	}

	public void renderTargets(){
		Iterator<ArrayList<Integer>> iter = targets.iterator();
		while(iter.hasNext()){
			ArrayList<Integer> list = iter.next();
			if(touched){
				int diffX = x - list.get(0);
				int diffY = y - (Gdx.graphics.getHeight() - list.get(1));
				int square = (diffX * diffX) + (diffY * diffY);
				if(square <= ((radius + 5) * (radius + 5))){
					score++;
					iter.remove();
					continue;
				}
			}
			shapeRenderer.circle(list.get(0), Gdx.graphics.getHeight() - list.get(1), 5);
		}
	}

	public void renderObstacles(){
		shapeRenderer.setColor(Color.RED);
		for(ArrayList<Integer> list : obstacles){
			if(touched){
				int diffX = x - list.get(0);
				int diffY = y - (Gdx.graphics.getHeight() - list.get(1));
				int square = (diffX * diffX) + (diffY * diffY);
				if(square <= ((radius + 5) * (radius + 5))){
					failed = true;
					ignore = true;
					break;
				}
			}
			shapeRenderer.circle(list.get(0), Gdx.graphics.getHeight() - list.get(1), 5);
		}
	}

	public void renderEndScreen(){
		batch.begin();
		font.draw(batch, "Game Over! Tap to restart", 300, 300);
		if(Gdx.input.isTouched()){
			if(!ignore) {
				generateTargets();
				failed = false;
				score = 0;
				remainingCircles = 4;
				ignore = true;
			}
		} else{
			ignore = false;
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		shapeRenderer.dispose();
		font.dispose();
	}
}
