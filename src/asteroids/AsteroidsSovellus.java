package asteroids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AsteroidsSovellus extends Application {
    
    public static int LEVEYS = 300;
    public static int KORKEUS = 200;
    
    @Override
    public void start(Stage stage) throws Exception {
        Pane ruutu = new Pane();
        Text text = new Text(10, 20, "Points: 0");
        ruutu.setPrefSize(LEVEYS, KORKEUS);
        ruutu.getChildren().add(text);
        
        AtomicInteger pisteet = new AtomicInteger();
        
        Alus alus = new Alus(LEVEYS / 2, KORKEUS / 2);
        List<Asteroidi> asteroidit = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Random rnd = new Random();
            Asteroidi asteroidi = new Asteroidi(rnd.nextInt(LEVEYS/3), rnd.nextInt(KORKEUS));
            asteroidit.add(asteroidi);
        }
        List<Ammus> ammukset = new ArrayList<>();
        
        ruutu.getChildren().add(alus.getHahmo());
        asteroidit.forEach(asteroidi -> ruutu.getChildren().add(asteroidi.getHahmo()));
        
        Scene scene = new Scene(ruutu);
        
        Map<KeyCode, Boolean> painetutNapit = new HashMap<>();
        
        scene.setOnKeyPressed(event-> {
            painetutNapit.put(event.getCode(), Boolean.TRUE);
        });
        scene.setOnKeyReleased(event-> {
            painetutNapit.put(event.getCode(), Boolean.FALSE);
        });
        
        Point2D liike = new Point2D(1, 0);
        
        new AnimationTimer() {
            @Override
            public void handle(long nykyhetki) {
                if (painetutNapit.getOrDefault(KeyCode.LEFT, false)) {
                    alus.kaannaVasemmalle();
                }
                if (painetutNapit.getOrDefault(KeyCode.RIGHT, false)) {
                    alus.kaannaOikealle();
                }
                if (painetutNapit.getOrDefault(KeyCode.UP, false)) {
                    alus.kiihdyta();
                }
                if (painetutNapit.getOrDefault(KeyCode.SPACE, false) && ammukset.size() < 3) {
                    Ammus ammus = new Ammus((int) alus.getHahmo().getTranslateX(), (int) alus.getHahmo().getTranslateY());
                    ammus.getHahmo().setRotate(alus.getHahmo().getRotate());
                    ammukset.add(ammus);
                    
                    ammus.kiihdyta();
                    ammus.setLiike(ammus.getLiike().normalize().multiply(3));
                    
                    ruutu.getChildren().add(ammus.getHahmo());
                    
                }
                alus.liiku();
                asteroidit.forEach(asteroidi -> asteroidi.liiku());
                ammukset.forEach(ammus -> ammus.liiku());
                
                ammukset.forEach(ammus -> {
                    asteroidit.forEach(asteroidi -> {
                        if (ammus.tormaa(asteroidi)) {
                            ammus.setElossa(false);
                            asteroidi.setElossa(false);
                        }
                    });
                    
                    if (!ammus.isElossa()) {
                        text.setText("Points: " + pisteet.addAndGet(1000));
                    }
                });
                
                ammukset.stream().filter(ammus -> !ammus.isElossa())
                        .forEach(ammus -> ruutu.getChildren().remove(ammus.getHahmo()));
                ammukset.removeAll(ammukset.stream().filter(ammus -> !ammus.isElossa())
                        .collect(Collectors.toList()));
                asteroidit.stream().filter(asteroidi -> !asteroidi.isElossa())
                        .forEach(asteroidi -> ruutu.getChildren().remove(asteroidi.getHahmo()));
                asteroidit.removeAll(asteroidit.stream().filter(asteroidi -> !asteroidi.isElossa())
                        .collect(Collectors.toList()));
                
                asteroidit.forEach(asteroidi -> {
                    if(alus.tormaa(asteroidi)) {
                        stop();
                    }
                });
                
                if (Math.random() < 0.005) {
                    Asteroidi asteroidi = new Asteroidi(LEVEYS, KORKEUS);
                    if (!asteroidi.tormaa(alus)) {
                        asteroidit.add(asteroidi);
                        ruutu.getChildren().add(asteroidi.getHahmo());
                    }
                }
            }
        }.start();
        
        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(AsteroidsSovellus.class);
        System.out.println("Hei maailma!");
    }

    public static int osiaToteutettu() {
        return 4;
    }

}
