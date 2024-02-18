

package com.example.skill_swapping;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ImageView;

public class userhome extends AppCompatActivity {
    ImageView im1,im2,im3,im4,im5,im6,im7,im8,im9,im10,im11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        im1=(ImageView) findViewById(R.id.myskill);

        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),user_add_skills.class));
            }
        });

        im2=findViewById(R.id.v_user);
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),user_view_other_user.class));

            }
        });
        im3=(ImageView) findViewById(R.id.addpro);

        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),user_add_project.class));
            }
        });
        im4=(ImageView) findViewById(R.id.oth_pro);

        im4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),user_view_other_project.class));
            }
        });
        im5=(ImageView) findViewById(R.id.sndcomp);

        im5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),user_send_complaint.class));
            }
        });

        im7=(ImageView) findViewById(R.id.other_req);

        im7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),user_view_other_request.class));
            }
        });
        im8=(ImageView) findViewById(R.id.search);

        im8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),user_search_skill.class));
            }
        });
        im9=(ImageView) findViewById(R.id.logout);
        im9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });




        im10=(ImageView) findViewById(R.id.card_image4);
        im11=(ImageView) findViewById(R.id.card_image5);
        im10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),user_view_request.class));
            }
        });
        im11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),user_sended_request.class));

            }
        });



    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), userhome.class));
    }
}