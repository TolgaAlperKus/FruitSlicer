package com.tolgaalperkus.fruitslicer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

//BU KISIMDA GEREKLİ GDX KOMUTLARI BULUNMAKTA OYUNUN TEMELİ BURASI

//inputların takip edilebilmesi için InputProcessor interface'ini implement ediyoruz
public class FruitSlicer extends ApplicationAdapter implements InputProcessor {
	int gameState = 0;
	SpriteBatch batch;
	//Kullanacağımız imajları burada Texture sınıfında tanımlıyoruz.
	Texture background,carrot,cherry,apple,strawberry,bomb,time;
	//Menü başlangıcı ve skor kısımlarındaki yazı türünü belirtiyorum
	BitmapFont font;
	FreeTypeFontGenerator fontGen;

	@Override
	public void create () {
		batch = new SpriteBatch();
		//Texture sınıfında tanımladığımız nesnelere görsellerin adreslerini işaret ediyoruz.
		background = new Texture("kitchenBg.png");
		carrot = new Texture("carrot.png");
		cherry = new Texture("cherry.png");
		apple = new Texture("apple.png");
		strawberry = new Texture("strawberry.png");
		bomb = new Texture("piecesBomb.png");
		time = new Texture("piecesTime.png");
		//Buradaki aktivitenin kullanıcıdan gelen girdileri işleyeceğini belirtiyoruz.
		Gdx.input.setInputProcessor(this);
		//Aşağıda kullanacağımız fontu ve onun özelliklerini FreeType eklentisi sayesinde
		//istediğimiz gibi belirtiyoruz
		fontGen = new FreeTypeFontGenerator(Gdx.files.internal("robotobold.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.color = Color.BLACK;
		params.borderWidth=2;
		params.borderColor=Color.RED;
		params.size = 100;
		font = fontGen.generateFont(params);
	}

	@Override
	public void render () {
		//bu kısımda çizilecek olan nesneleri ekleyebiliriz
		batch.begin();
		//Gdx.graphics.getWidth() ve Gdx.graphics.getHeight() komutlarıyla cihaz ekranı boyutlarını çağırıyoruz
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		//ekrana yazacağımız yazıları ekliyoruz
		if(gameState==0){
			font.draw(batch,"Cut To Play",Gdx.graphics.getWidth()/2f-250,Gdx.graphics.getHeight()/2f);
		}
		else if(gameState==1){
			font.draw(batch,"Score :",50,100);
		}
		font.draw(batch,"Cut To Play",Gdx.graphics.getWidth()/2f-250,Gdx.graphics.getHeight()/2f);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		fontGen.dispose();
		}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	/*
	Bu uygulamada genellikle kullanacağımız kısım burası
	Bu kısım ekrandan parmağın ayrılmadan sürüklendiğinde yapacağı işlemleri barındırır.
	*/
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}
}
