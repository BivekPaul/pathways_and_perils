package alive;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Enemy extends Sprite {

	public enum INIT { UP, DOWN, LEFT, RIGHT}

	
	private World world ;
	private Body enemBody;
	private INIT curr_dir ; 
	public String name = "enemy"  ;
	public final static byte inner_byte = 2 ;
	public final static byte outer_byte = 4 ;
	
	public Enemy(World world, float x, float y, INIT dir) {
		
		super(new Texture("Dungeon Crawl Stone Soup Supplemental/Dungeon Crawl Stone Soup Supplemental/monster/demons/blue_devil.png")) ;
		this.world = world ; 
		this.curr_dir = dir;
		
		defineBody(x, y) ;
		setBounds(0, 0, 32, 32) ;
		setPosition(enemBody.getPosition().x - getWidth() / 2, enemBody.getPosition().y - getHeight() /  2 ) ;

	}
	
	public void defineBody(float x, float y) {
		BodyDef bdef = new BodyDef() ; 
		CircleShape cshape = new CircleShape() ;
		FixtureDef enemfix = new FixtureDef() ; 
		
		bdef.type = BodyDef.BodyType.DynamicBody ; 
		bdef.position.set(x, y) ; 
		enemBody = this.world.createBody(bdef) ; 
		
		cshape.setRadius(30.0f);
		enemfix.shape = cshape ; 
		enemfix.restitution = 2.0f ;
		enemfix.filter.categoryBits = inner_byte ;
		enemBody.createFixture(enemfix).setUserData(this); 
		
		CircleShape pDetecShape = new CircleShape() ;
		pDetecShape.setRadius(100.0f);
		enemfix.shape = pDetecShape ;
		enemfix.isSensor = true ;
		enemfix.filter.categoryBits = outer_byte ;
		
		enemBody.createFixture(enemfix).setUserData(this); 
		
	}

	public void moveBody() {
		if(enemBody.getLinearVelocity().x ==  0 && enemBody.getLinearVelocity().y ==  0) {

		//System.out.println("here");

			switch(curr_dir) {

				case UP:
					enemBody.setLinearVelocity(0f, 400f);
				break;
				case DOWN:
					enemBody.setLinearVelocity(0f, -400f);
				break ;
				case RIGHT:
		          	enemBody.applyLinearImpulse(new Vector2(10000f, 0f), enemBody.getWorldCenter(), true ) ;
				break ;
				case LEFT:
		          	enemBody.applyLinearImpulse(new Vector2(-10000f, 0f), enemBody.getWorldCenter(), true ) ;
				break ;
			}
		}
	}
	
	public void update(float delta) {
		moveBody() ;
//		System.out.println(enemBody.getLinearVelocity());
//		System.out.println(enemBody.getPosition());
//		enemBody.setLinearVelocity(0f, 400f);
		
		setPosition(enemBody.getPosition().x - getWidth() / 2, enemBody.getPosition().y - getHeight() /  2 ) ;
	}


	public void invert_dir() {
		switch(curr_dir) { 
			case UP:
				curr_dir = INIT.DOWN ; 
			break ; 
			case DOWN:
				curr_dir = INIT.UP  ; 
			break ;
			case LEFT:
				curr_dir = INIT.RIGHT ;
			break ; 
			case RIGHT:
				curr_dir = INIT.LEFT ;
			break ;
		}
	}
	
	public Vector2 calcPath(Vector2 playerPosition) {
		Vector2 enemPosition = this.enemBody.getPosition() ;
		Vector2 enemVelocity = playerPosition.add(enemPosition.scl(-1)) ;
		this.enemBody.setLinearVelocity(enemVelocity);
		return enemVelocity ;
	}
	
}
