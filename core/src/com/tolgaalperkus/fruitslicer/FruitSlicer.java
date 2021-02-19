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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

//BU KISIMDA GEREKLİ GDX KOMUTLARI BULUNMAKTA OYUNUN TEMELİ BURASI

//inputların takip edilebilmesi için InputProcessor interface'ini implement ediyoruz
public class FruitSlicer extends ApplicationAdapter implements InputProcessor {

	SpriteBatch batch;
	//Kullanacağımız imajları burada Texture sınıfında tanımlıyoruz.
	Texture background,pizza,apple,bomb,time,coin;
	//Menü başlangıcı ve skor kısımlarındaki yazı türünü belirtiyorum
	BitmapFont font;
	FreeTypeFontGenerator fontGen;

	float zorluk = 0f;
	int lives = 0;
	int score = 0;

	float genCounter=0;
	private final float startGenSpeed = 1.1f;
	float genSpeed = startGenSpeed;

	private double currentTime = 0;
	private double gameOverTime = -1.0f;
	Random random = new Random();
	Array<Fruit> fruitArray = new Array<Fruit>();

	@Override
	public void create () {
		batch = new SpriteBatch();
		//Texture sınıfında tanımladığımız nesnelere görsellerin adreslerini işaret ediyoruz.
		background = new Texture("kitchenBg.png");
		apple = new Texture("apple.png");
		bomb = new Texture("piecesBomb.png");
		time = new Texture("piecesTime.png");
		pizza = new Texture("pizza.png");
		coin = new Texture("piecesCoin.png");
		//telefonun yatay veya dikey kullanıldığını anlamak için yaptık
		Fruit.radius = Math.max(Gdx.graphics.getHeight(),Gdx.graphics.getWidth()) /18f;

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

		//farklı cihazlarda saniye başına düşen frame sayısı farklı olacağından kendi timerımızı oluşturuyoruz.
		double newTime = TimeUtils.millis()/1000.0;
		double frameTime = Math.min(newTime - currentTime,0.3);
		float deltaTime = (float)frameTime;
		currentTime=newTime;

		if (lives <= 0 && gameOverTime == 0f)
		{
			//oyun bitimi
			gameOverTime = currentTime;
		}
		if(lives > 0)
		{
			//oyun çalışırken yapılacaklar

			genSpeed-=deltaTime*0.015f;

			if (genCounter <= 0f){
				genCounter = genSpeed;
				addItem();
			} else{
				genCounter -=deltaTime;
			}

			for (int i=0;i<lives;i++){
				batch.draw(coin,30+(i*100),Gdx.graphics.getHeight()-110f,100f,100f);
			}

			for (Fruit fruit:fruitArray){
				fruit.update(deltaTime);
				switch (fruit.type){
					case REGULAR:
						batch.draw(apple,fruit.getPos().x,fruit.getPos().y,Fruit.radius,Fruit.radius);
						break;
					case BOMB:
						batch.draw(bomb,fruit.getPos().x,fruit.getPos().y,Fruit.radius,Fruit.radius);
						break;
					case EXTRA:
						batch.draw(pizza,fruit.getPos().x,fruit.getPos().y,Fruit.radius,Fruit.radius);
						break;
					case COIN:
						batch.draw(coin,fruit.getPos().x,fruit.getPos().y,Fruit.radius,Fruit.radius);
						break;
				}
			}

			/*
			Ekran dışına çıkan elmaların canı azaltmasını sağladık.
			ve ekranda o sırada bulunan diğer elmaların ekran dışına çıkması
			durumunda canı azaltmasını engelledik böylelikle kullanıcı aynı anda
			birden fazla elma kaçırdığında sadece bir can kaybedecek.
			*/
			boolean holdlives = false;

			Array<Fruit>toRemove =new Array<Fruit>();
			for (Fruit fruit:fruitArray){
				if (fruit.outOfScreen()){
					toRemove.add(fruit);

					if (fruit.living&& fruit.type == Fruit.Type.REGULAR){
						lives--;
						holdlives=true;
						break;
					}
				}
			}
			if(holdlives){
				for(Fruit f:fruitArray){
					f.living = false;
				}
			}
			for (Fruit f:toRemove){
				fruitArray.removeValue(f,true);
			}

		}

		//ekrana yazacağımız yazıları ekliyoruz
		font.draw(batch,"Score : "+score,50,100);
		if(lives <= 0){
			font.draw(batch,"Cut To Play",Gdx.graphics.getWidth()/2f-250,Gdx.graphics.getHeight()/2f);
		}
		batch.end();
	}

	private void addItem(){

		float pos = random.nextFloat() * Math.max(Gdx.graphics.getHeight(),Gdx.graphics.getWidth());

		Fruit item = new Fruit(new Vector2(pos,-Fruit.radius),new Vector2((Gdx.graphics.getWidth()*0.5f-pos)*(0.3f+(random.nextFloat()-0.5f)),Gdx.graphics.getHeight()*0.5f));

		// bu kısımda gelecek nesnelerin ihtimallerini belirtiyorum
		float type = random.nextFloat();
		if(type>0.98f) item.type = Fruit.Type.COIN;
		else if(type>0.88f) item.type = Fruit.Type.EXTRA;
		else if(type+zorluk>0.78f) item.type = Fruit.Type.BOMB;
		if(fruitArray.size<15) fruitArray.add(item);
		else if(zorluk<0.28) zorluk=zorluk+0.01f;

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
		if(lives <= 0 && currentTime-gameOverTime>2f){
			//oyun henüz menüde
			gameOverTime = 0f;
			score = 0;
			lives = 4;
			genSpeed = startGenSpeed;
			fruitArray.clear();
		}else{
			//oyun devam ederken
			Array<Fruit> toRemove = new Array<Fruit>();
			Vector2 pos = new Vector2(screenX,Gdx.graphics.getHeight()-screenY);
			int plusScore = 0;
			//bu kısımda dokunulan nesnelerin türüne göre score ve lives değiştiriliyor.
			for (Fruit f: fruitArray){

				if (f.clicked(pos)){
					toRemove.add(f);

					switch (f.type){
						case REGULAR:
							plusScore++;
							break;
						case EXTRA:
							plusScore+=5;
							break;
						case BOMB:
							lives--;
							break;
						case COIN:
							lives++;
							break;
					}
				}
			}
			score +=plusScore;
			//bu kısımda fruit dizisinden dokunulan fruitları çıkaracağız
			for (Fruit f:toRemove){
				fruitArray.removeValue(f,true);
			}
		}
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
