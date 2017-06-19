package com.example.iyashwant.sqlite_image;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    //Button addImage;
    ArrayList<Image> imageArry = new ArrayList<Image>();
    ImageAdapter imageAdapter;
    private static final int CAMERA_REQUEST = 1;
    private static final int PICK_FROM_GALLERY = 2;
    ListView dataList;
    byte[] imageName;
    int imageId;
    String image_caption;
    EditText captionbox;
    Bitmap theImage;
    DataBaseHandler db;

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // captionbox= (EditText)findViewById(R.id.editcaption);

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton4 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item4);

        dataList = (ListView) findViewById(R.id.list);

        db = new DataBaseHandler(this);

        List<Image> images = db.getAllImages();
        for (Image cn : images) {
            String log = "ID:" + cn.getID() + " Name: " + cn.getName()
                    + " ,Image: " + cn.getImage();

            Log.d("Result: ", log);

            imageArry.add(cn);

        }


        imageAdapter = new ImageAdapter(this, R.layout.screen_list, imageArry);
        dataList.setAdapter(imageAdapter);

        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                imageName = imageArry.get(position).getImage();
                imageId = imageArry.get(position).getID();
                image_caption= imageArry.get(position).getName();


                Log.d("Before Send:****", imageName + "-" + imageId);
                // convert byte to bitmap
                ByteArrayInputStream imageStream = new ByteArrayInputStream(
                        imageName);
                theImage = BitmapFactory.decodeStream(imageStream);
                Intent intent = new Intent(MainActivity.this,
                        DisplayImageActivity.class);
                intent.putExtra("imagename", theImage);
                intent.putExtra("imageid", imageId);
                intent.putExtra("imagecaption",image_caption);
                startActivity(intent);

            }
        });

        final String[] option = new String[] { "Take from Camera",
                "Select from Gallery" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Option");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Log.e("Selected Item", String.valueOf(which));
                if (which == 0) {
                    callCamera();
                }
                if (which == 1) {
                    callGallery();
                }

            }
        });
        final AlertDialog dialog = builder.create();



        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.show();


            }


        });

        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.delete_all,null);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(view2).setPositiveButton("Sure !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db.deleteall();
                        Intent i = new Intent(MainActivity.this,
                                MainActivity.class);
                        startActivity(i);

                        Toast.makeText(MainActivity.this, "Deleted All", Toast.LENGTH_SHORT).show();






                    }
                }).setNegativeButton("cancel",null).setCancelable(true);

                AlertDialog alertDialog= builder.create();
                alertDialog.show();





            }
        });


     /*   addImage = (Button) findViewById(R.id.btnAdd);

        addImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show();
            }
        });
        */

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    protected void onPostResume() {
       // callBox();
        super.onPostResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        if (resultCode != RESULT_OK)
            return;



        switch (requestCode) {
            case CAMERA_REQUEST:

                Bundle extras = data.getExtras();

                if (extras != null) {

                    callCameraBox(extras);

                }
                break;


            case PICK_FROM_GALLERY:


                Bundle extras2 = data.getExtras();

                if (extras2 != null) {

                    callGalleryBox(extras2);

                }

                break;
        }
    }


    public void callCamera() {
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra("crop", "true");
        cameraIntent.putExtra("aspectX", 0);
        cameraIntent.putExtra("aspectY", 0);
        cameraIntent.putExtra("outputX", 200);
        cameraIntent.putExtra("outputY", 150);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

        

    }



    public void callGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                PICK_FROM_GALLERY);

    }

    public void callCameraBox(final Bundle extras)
    {
        View view2 = LayoutInflater.from(this).inflate(R.layout.add_caption,null);

        captionbox =(EditText)view2.findViewById(R.id.editText_caption);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view2).setPositiveButton("Add !", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                image_caption = captionbox.getText().toString();

                Bitmap yourImage = extras.getParcelable("data");
                // convert bitmap to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte imageInByte[] = stream.toByteArray();
                Log.e("output before conv", imageInByte.toString());
                // Inserting Images
                Log.d("Insert: ", "Inserting ..");

                db.addImage(new Image(imageId,image_caption, imageInByte));
                Intent i = new Intent(MainActivity.this,
                        MainActivity.class);
                startActivity(i);
                finish();


            }
        }).setNegativeButton("cancel",null).setCancelable(true);

        AlertDialog alertDialog= builder.create();
        alertDialog.show();



    }

    public void callGalleryBox(final Bundle extras2)
    {
        View view2 = LayoutInflater.from(this).inflate(R.layout.add_caption,null);

        captionbox =(EditText)view2.findViewById(R.id.editText_caption);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view2).setPositiveButton("Add !", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                image_caption = captionbox.getText().toString();
                Bitmap yourImage = extras2.getParcelable("data");
                // convert bitmap to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte imageInByte[] = stream.toByteArray();
                Log.e("output before conv", imageInByte.toString());
                // Inserting Images
                Log.d("Insert: ", "Inserting ..");
                db.addImage(new Image(imageId,image_caption, imageInByte));
                Intent i = new Intent(MainActivity.this,
                        MainActivity.class);
                startActivity(i);
                finish();

            }
        }).setNegativeButton("cancel",null).setCancelable(true);

        AlertDialog alertDialog= builder.create();
        alertDialog.show();



    }


}
