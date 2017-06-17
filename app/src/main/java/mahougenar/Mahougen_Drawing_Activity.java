package mahougenar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.vuforia.samples.VuforiaSamples.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Mahougen_Drawing_Activity extends Activity {

    private MahougenView mahougenView;
    private Button btnReset,btnShare,btnSummon;
    private SeekBar sbMP;
    private TextView tvMP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahougen__drawing_);

        // ask permission to read and write sdcard
        askPermissions();
        // find the views
        mahougenView = (MahougenView)findViewById(R.id.mahougenView);
        btnReset = (Button)findViewById(R.id.buttonReset);
        btnShare = (Button)findViewById(R.id.buttonShare);
        btnSummon = (Button)findViewById(R.id.buttonSummon);
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
      /*  btnSave=(Button)findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSaveClicked();
            }
        });*/
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

            File file = new File(path,
                    "Mahougen.png");
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
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pictureFile));
        startActivity(Intent.createChooser(shareIntent,getString(R.string.share)));
    }

    public void OnSummonClick(View v)
    {
        Intent i = new Intent();
        i.setClassName("com.vuforia.samples", "com.vuforia.samples.app.UserDefinedTargets.UserDefinedTargets");
        startActivity(i);
        System.out.println("jump");
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
}


