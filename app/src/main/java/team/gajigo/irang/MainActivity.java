package team.gajigo.irang;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import team.gajigo.irang.information.SubActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);


        ImageButton btn1 = (ImageButton) findViewById(R.id.fun);
        btn1.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(),CategoryActivity.class);
                        intent.putExtra("category", "f");
                        startActivity(intent);
                    }
                }
        );

        ImageButton btn2 = (ImageButton) findViewById(R.id.learn);
        btn2.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(),CategoryActivity.class);
                        intent.putExtra("category", "l");
                        startActivity(intent);
                    }
                }
        );

        ImageButton btn3 = (ImageButton) findViewById(R.id.eat);
        btn3.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(),CategoryActivity.class);
                        intent.putExtra("category", "e");
                        startActivity(intent);
                    }
                }
        );

        ImageButton btn4 = (ImageButton) findViewById(R.id.course);
        btn4.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(),CourseActivity.class);
                        startActivity(intent);
                    }
                }
        );

        ImageButton btn5 = (ImageButton) findViewById(R.id.home);
        btn5.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
        );

        ImageButton btn6 = (ImageButton) findViewById(R.id.info);
        btn6.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(), SubActivity.class);
                        startActivity(intent);
                    }
                }
        );

    }



}
