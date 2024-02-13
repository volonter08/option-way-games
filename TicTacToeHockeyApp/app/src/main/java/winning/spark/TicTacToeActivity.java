package winning.spark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TicTacToeActivity extends AppCompatActivity {
    private final Logic logic = new Logic();
    private final List<ImageButton> buttons = new ArrayList<>();
    private SwitchMaterial switchSide;

    private final Map<String,Integer> mapResourceId = new HashMap<>();
    private final List<String> listLockedImageButton = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapResourceId.put("X",R.drawable.hockey_stick);
        mapResourceId.put("O",R.drawable.washer);
        setContentView(R.layout.activity_tic_tac_toe);
        LinearLayout layoutForButtons = findViewById(R.id.LayoutForButtons);
        for (int i = 0; i < layoutForButtons.getChildCount(); i++) {
            LinearLayout layout = findViewById(layoutForButtons.getChildAt(i).getId());
            for (int j = 0; j < layout.getChildCount(); j++) {
                ImageButton button = findViewById(layout.getChildAt(j).getId());
                buttons.add(button);
            }
        }

        switchSide = findViewById(R.id.switchSide);
        switchSide.setOnClickListener(this::clickSwitchSide);

        Button restart = findViewById(R.id.restart);
        restart.setOnClickListener(this::clickOnRestart);
        restart.setText("Restart");
    }

    public boolean isWin() {
        Toast toast = Toast.makeText(
                getApplicationContext(), "",
                Toast.LENGTH_SHORT
        );
        toast.setGravity(Gravity.CENTER, 0, 0);
        if (this.logic.checkWin("X")) {
            toast.setText("Crosses won! Start a new game!");
            toast.show();
            return true;
        } else if (this.logic.checkWin("O")) {
            toast.setText("Naughts won! Start a new game!");
            toast.show();
            return true;
        } else if (this.logic.isFilled()) {
            toast.setText("Draw! Start a new Game!");
            toast.show();
            return true;
        }
        return false;
    }

    public void clickOnButton(View view) {
        ImageButton clicked = findViewById(view.getId());
        if (clicked.getDrawable()==null && !isWin()) {
            clicked.setImageResource(mapResourceId.get(logic.getValue()));
            logic.clickOnButton((String) clicked.getTag());
            if (!logic.isFilled() && !logic.checkWin(Logic.firstMark) && logic.getTurn() % 2 == 1) {
                ImageButton button = buttons.get(logic.clickOnButtonWithAI());
                button.setImageResource(mapResourceId.get(logic.getValue()));
                logic.setTurn(logic.getTurn() + 1);
            }
            isWin();
        }
    }

    public void clickOnRestart(View view) {
        for (ImageButton v : buttons) {
            v.setImageDrawable(null);
        }
        logic.clearMatrix();
        if (switchSide.isChecked()) {
            logic.changeSide("O");
        } else logic.changeSide("X");
    }

    public void clickSwitchSide(View view) {
        logic.changeSide();
    }
}