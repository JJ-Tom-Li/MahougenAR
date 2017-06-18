package mahougenar;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.vuforia.samples.VuforiaSamples.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.util.ArrayList;

public class Mahougen_Drawing_Activity extends AppCompatActivity{

    private MahougenView mahougenView;
    private SeekBar sbMP;
    private TextView tvMP;
    private ArrayList<String> images = new ArrayList<String>();
    private String imageName;
    ArrayAdapter<String> imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahougen__drawing_);

        // ask permission to read and write sdcard
        askPermissions();
        // find the views
        mahougenView = (MahougenView)findViewById(R.id.mahougenView);
        sbMP = (SeekBar)findViewById(R.id.seekBarMP);
        tvMP=(TextView)findViewById(R.id.textView);

        //set the textView tMP
        tvMP.setText("MP:10");
        //set the seekBar
        sbMP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progess=0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvMP.setText("MP:"+(progress+3));
                mahougenView.changeMP(progress+3);
                mahougenView.clear();
                mahougenView=(MahougenView)findViewById(R.id.mahougenView);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //update image list
        updateImageList();
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void askPermissions() {
        /**ask the permission for R/W sdcard.*/
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    public void OnResetClick(View v)
    {
        mahougenView.clear();
        Toast.makeText(this,"Clear", Toast.LENGTH_LONG)
                .show();
    }
    public File OnSaveClicked()
    {
        try{
            /*create the dir if no exists*/
            File sdFile = android.os.Environment.getExternalStorageDirectory();
            String path = sdFile.getPath() + File.separator + "mahougens";
            File dirFile = new File(path);
            if(!dirFile.exists())//如果資料夾不存在
                dirFile.mkdir();//建立資料夾

            imageName= System.currentTimeMillis()+".png";
            File file = new File(path, imageName);
            OutputStream stream = new FileOutputStream(file);
            mahougenView.saveBitmap(stream);
            stream.close();
            //send broadcast to Media to update data
            /*Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
            intent.setData(Uri.fromFile(Environment
                    .getExternalStorageDirectory()));
            sendBroadcast(intent);*/

            Toast.makeText(this,"save success", Toast.LENGTH_LONG)
            .show();
            return file;
        }catch(Exception e){
            Toast.makeText(this,"save failed", Toast.LENGTH_LONG)
            .show();
            e.printStackTrace();
        }
        return null;
    }

    public void OnShareClick(View v)
    {
        // get file directory.
        final File pictureFile = OnSaveClicked();
        // invoke an intent with ACTION_SEND
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/png");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pictureFile));
        startActivity(Intent.createChooser(shareIntent,getString(R.string.share)));
    }

    public void OnSummonClick(View v) {
        final String[] chioce = new String[]{"用這張!", "從資料夾選取"};
        AlertDialog showChoice = new AlertDialog.Builder(Mahougen_Drawing_Activity.this)
                .setTitle("選擇魔法陣來源")
                .setItems(chioce, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (chioce[i] == "用這張!") {
                            OnSaveClicked();//save mahougen
                            showTutorial();
                        } else if (chioce[i] == "從資料夾選取") {
                            AlertDialog imageChioce = new AlertDialog.Builder(Mahougen_Drawing_Activity.this)
                                    .setAdapter(imageAdapter, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            imageName = imageAdapter.getItem(i).toString();
                                            System.out.println(imageName);
                                            showTutorial();
                                        }
                                    })
                                    .show();
                        }

                    }
                })
                .show();

        //System.out.println("jump");
    }

    public void showTutorial()
    {
        AlertDialog showTheTutorial = new AlertDialog.Builder(Mahougen_Drawing_Activity.this)
                .setTitle("即將生成魔法陣")
                .setMessage("生成魔法陣時，請依照以下步驟:\n" +
                        "1.找一張辨識度高的相片或卡片(悠遊卡)，做為目標物\n" +
                        "2.將相機畫面對準對焦至目標物，盡量填滿整個相機畫面\n" +
                        "3.按下正下方的相機圖示，魔法陣將會生成\n" +
                        "4.成為大魔法師吧!\n")
                .setPositiveButton("生成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Mahougen_Drawing_Activity.this, "即將生成，請稍等...", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setClassName(getPackageName(), getPackageName() + ".app.UserDefinedTargets.UserDefinedTargets");
                        intent.putExtra("imageName",imageName);
                        startActivity(intent); //go to the AR ui
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Mahougen_Drawing_Activity.this, "取消", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        /** select to change background color or line color*/
        SubMenu subMenuBackground = menu.addSubMenu(Menu.NONE, Menu.FIRST, Menu.NONE, "改變背景顏色");
        subMenuBackground.add(Menu.NONE, Menu.FIRST+1, Menu.NONE,"黑色");
        subMenuBackground.add(Menu.NONE, Menu.FIRST+2, Menu.NONE,"白色");
        subMenuBackground.add(Menu.NONE, Menu.FIRST+3, Menu.NONE,"灰色");
        subMenuBackground.add(Menu.NONE, Menu.FIRST+4, Menu.NONE,"藍色");
        subMenuBackground.add(Menu.NONE, Menu.FIRST+5, Menu.NONE,"紅色");

        SubMenu subMenuLine = menu.addSubMenu(Menu.NONE+1, Menu.FIRST+6, Menu.NONE, "改變線條顏色");
        subMenuLine.add(Menu.NONE+1, Menu.FIRST+7, Menu.NONE,"黑色");
        subMenuLine.add(Menu.NONE+1, Menu.FIRST+8, Menu.NONE,"白色");
        subMenuLine.add(Menu.NONE+1, Menu.FIRST+9, Menu.NONE,"灰色");
        subMenuLine.add(Menu.NONE+1, Menu.FIRST+10, Menu.NONE,"藍色");
        subMenuLine.add(Menu.NONE+1, Menu.FIRST+11, Menu.NONE,"紅色");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
            switch(item.getItemId()){
                case Menu.FIRST+1:
                    mahougenView.changeBackgroundColor("BLACK");
                    break;
                case Menu.FIRST+2:
                    mahougenView.changeBackgroundColor("WHITE");
                    break;
                case Menu.FIRST+3:
                    mahougenView.changeBackgroundColor("GRAY");
                    break;
                case Menu.FIRST+4:
                    mahougenView.changeBackgroundColor("BLUE");
                    break;
                case Menu.FIRST+5:
                    mahougenView.changeBackgroundColor("RED");
                    break;
                case Menu.FIRST+7:
                    mahougenView.changeLineColor("BLACK");
                    break;
                case Menu.FIRST+8:
                    mahougenView.changeLineColor("WHITE");
                    break;
                case Menu.FIRST+9:
                    mahougenView.changeLineColor("GRAY");
                    break;
                case Menu.FIRST+10:
                    mahougenView.changeLineColor("BLUE");
                    break;
                case Menu.FIRST+11:
                    mahougenView.changeLineColor("RED");
                    break;
            }
            if(item.getItemId()!= Menu.FIRST&&item.getItemId()!= Menu.FIRST+6)
                Toast.makeText(this,"顏色已修改", Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }


    public void updateImageList() {
        /** load the song list form sdcard*/
        File home = new File(Environment.getExternalStorageDirectory().getPath()+"/mahougens/");
        /*check if there is file*/
        if (home.listFiles( new FileExtensionFilter()).length > 0) {
            for (File file : home.listFiles( new FileExtensionFilter())) {
                /*add png file to image list*/
                System.out.println(file.getName());
                images.add(file.getName());
            }

            /*put the image list into ListView*/
            imageAdapter = new ArrayAdapter<String>
                    (this,R.layout.support_simple_spinner_dropdown_item,images);

        }
    }

    public class FileExtensionFilter implements FilenameFilter {
        /** check if file is end with ".png" or ".PNG"*/
        public boolean accept(File dir, String name) {
            return (name.endsWith(".png") || name.endsWith(".PNG")||name.endsWith(".jpg")||name.endsWith(".JPG"));
        }
    }
}


