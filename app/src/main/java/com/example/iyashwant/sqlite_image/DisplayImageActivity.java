package com.example.iyashwant.sqlite_image;

/**
 * Created by iyashwant on 17/6/17.
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class DisplayImageActivity extends Activity {
    Button btnDelete;
    ImageView imageDetail;
    int imageId;
    String image_caption;
    Bitmap theImage;

    FloatingActionButton floatingActionButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
       // btnDelete = (Button) findViewById(R.id.btnDelete);
      //  materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        imageDetail = (ImageView) findViewById(R.id.imageView1);

        Intent intnt = getIntent();
        theImage = (Bitmap) intnt.getParcelableExtra("imagename");
        imageId = intnt.getIntExtra("imageid", 20);
        image_caption = intnt.getStringExtra("imagecaption");
        Log.d("Image ID:****", String.valueOf(imageId));
        imageDetail.setImageBitmap(theImage);
        TextView textView = (TextView)findViewById(R.id.caption_display);
        textView.setText(image_caption);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item);
        floatingActionButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

               final DataBaseHandler db = new DataBaseHandler(
                        DisplayImageActivity.this);

                Log.d("Delete Image: ", "Deleting.....");



                        View view2 = LayoutInflater.from(DisplayImageActivity.this).inflate(R.layout.deletesingle,null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayImageActivity.this);
                        builder.setView(view2).setPositiveButton("Sure !", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                db.deleteImage(new Image(imageId));
                                // /after deleting data go to main page
                                Intent i = new Intent(DisplayImageActivity.this,
                                        MainActivity.class);
                                startActivity(i);
                                finish();


                            }
                        }).setNegativeButton("cancel",null).setCancelable(true);

                        AlertDialog alertDialog= builder.create();
                        alertDialog.show();

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(DisplayImageActivity.this,
                MainActivity.class);
        startActivity(i);
    }
}
