
package asteroids;

import java.util.Random;
import javafx.scene.shape.Polygon;


public class Asteroidi extends Hahmo {
    private double pyorimisliike;
    
    public Asteroidi(int x, int y) {
        super(new MonikulmioTehdas().luoMonikulmio(), x, y);
        
        Random rnd = new Random();
        
        super.getHahmo().setRotate(rnd.nextInt(360));
        
        int kiihdytysteyMaara = 1 + rnd.nextInt(10);
        for (int i = 0; i < kiihdytysteyMaara; i++) {
            kiihdyta();
        }
        
        this.pyorimisliike = 0.5 - rnd.nextDouble();
    }
    
    @Override
    public void liiku() {
        super.liiku();
        super.getHahmo().setRotate(super.getHahmo().getRotate() + pyorimisliike);
    }
}
