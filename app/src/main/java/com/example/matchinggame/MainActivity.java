package com.example.matchinggame;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int[] imageCardId = {
            R.id.imageView1, R.id.imageView2, R.id.imageView3,
            R.id.imageView4, R.id.imageView5, R.id.imageView6,
            R.id.imageView7, R.id.imageView8, R.id.imageView9,
            R.id.imageView10, R.id.imageView11, R.id.imageView12
    };

    private final int[] availableCardId = {
            R.drawable.bear, R.drawable.cat, R.drawable.dog,
            R.drawable.frog, R.drawable.fox, R.drawable.gorilla
    };

    private cardInfo firstCard = null;
    private int flipCount = 0;
    private boolean isBusy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.reset).setOnClickListener(v -> arrangeGame());
        findViewById(R.id.button).setOnClickListener(v -> finish());

        arrangeGame();
    }

    private void arrangeGame() {
        ArrayList<Integer> newCards = new ArrayList<>();

        for (int availableCard : availableCardId) {
            newCards.add(availableCard);
            newCards.add(availableCard);
        }
        Collections.shuffle(newCards);

        for (int i = 0; i < newCards.size(); i++) {
            int imageViewId = imageCardId[i];
            int availableId = newCards.get(i);
            cardInfo card = new cardInfo(imageViewId, availableId);
            ImageView imageView = findViewById(imageViewId);
            imageView.setImageResource(R.drawable.unflipped);
            imageView.setOnClickListener(this);
            imageView.setTag(card);
            imageView.setScaleX(1f);
            imageView.setScaleY(1f);
            imageView.setRotationY(0f);
        }

        firstCard = null;
        flipCount = 0;
        isBusy = false;
    }

    @Override
    public void onClick(View view) {
        if (isBusy) return;

        ImageView imageView = (ImageView) view;
        cardInfo currentCard = (cardInfo) imageView.getTag();

        if (currentCard.isFlipped() || currentCard.isMatched()) return;

        flipCard(imageView, currentCard.getAvailableId());
        currentCard.setFlipped(true);

        if (firstCard == null) {
            firstCard = currentCard;
        } else {
            isBusy = true;

            if (firstCard.getAvailableId() == currentCard.getAvailableId()) {
                // Match
                firstCard.setMatched(true);
                currentCard.setMatched(true);
                flipCount++;

                view.postDelayed(() -> {
                    animateMatch(findViewById(firstCard.getImageViewId()));
                    animateMatch(findViewById(currentCard.getImageViewId()));
                    firstCard = null;
                    isBusy = false;

                    if (flipCount == 6) {
                        showWinDialog();
                    }
                }, 400);

            } else {
                // No match
                view.postDelayed(() -> {
                    flipCard(findViewById(firstCard.getImageViewId()), R.drawable.unflipped);
                    flipCard(imageView, R.drawable.unflipped);
                    firstCard.setFlipped(false);
                    currentCard.setFlipped(false);
                    firstCard = null;
                    isBusy = false;
                }, 800);
            }
        }
    }

    private void flipCard(final ImageView imageView, final int imageResId) {
        imageView.animate()
                .rotationY(70f)
                .setDuration(100)
                .setInterpolator(new android.view.animation.LinearInterpolator())
                .withEndAction(() -> {
                    imageView.setImageResource(imageResId);
                    imageView.setRotationY(-80f);
                    imageView.animate()
                            .rotationY(0f)
                            .setDuration(50)
                            .setInterpolator(new android.view.animation.LinearInterpolator())
                            .start();
                })
                .start();
    }

    private void animateMatch(ImageView imageView) {
        imageView.animate()
                .scaleX(1.15f)
                .scaleY(1.15f)
                .setDuration(100)
                .setInterpolator(new android.view.animation.LinearInterpolator())
                .withEndAction(() -> {
                    imageView.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(50)
                            .setInterpolator(new android.view.animation.LinearInterpolator())
                            .start();
                })
                .start();
    }


    private void showWinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("🎉 Congratulations!")
                .setMessage("You matched all the cards!")
                .setCancelable(false)
                .setPositiveButton("Play Again", (dialog, which) -> arrangeGame())
                .setNegativeButton("Exit", (dialog, which) -> finish());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
